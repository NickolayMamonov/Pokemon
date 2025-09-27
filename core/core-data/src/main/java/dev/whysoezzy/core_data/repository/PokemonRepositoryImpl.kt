package dev.whysoezzy.core_data.repository

import dev.whysoezzy.core_data.mappers.toDomainModel
import dev.whysoezzy.core_data.mappers.toEntity
import dev.whysoezzy.core_database.dao.PokemonDao
import dev.whysoezzy.core_network.PokemonApi
import dev.whysoezzy.domain.model.PaginatedData
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonListItem
import dev.whysoezzy.domain.repository.PokemonRepository
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.ConcurrentHashMap

class PokemonRepositoryImpl(
    private val api: PokemonApi,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    companion object {
        private const val CACHE_DURATION_MS = 24 * 60 * 60 * 1000L // 24 часа
        private const val BACKGROUND_REFRESH_THRESHOLD = 12 * 60 * 60 * 1000L // 12 часов
    }

    private val repositoryScope = CoroutineScope(
        Dispatchers.IO + SupervisorJob() + CoroutineName("PokemonRepository")
    )

    private val activeRequests = ConcurrentHashMap<String, Deferred<Result<Pokemon>>>()

    override suspend fun getPokemonList(
        limit: Int,
        offset: Int
    ): Result<PaginatedData<PokemonListItem>> {
        return try {
            Timber.e("Загружаем список покемонов: limit=$limit, offset=$offset")
            val cachedList = pokemonDao.getPokemonListPaginated(limit, offset)
            val cachedCount = pokemonDao.getPokemonListCount()

            if (cachedList.isNotEmpty() && isCacheValid(cachedList.first().lastUpdated)) {
                Timber.d("Используем кэшированный список ${cachedList.size} покемонов")
                val domainList = cachedList.map { it.toDomainModel() }
                val hasNextPage = (offset + limit) < cachedCount
                val currentPage = (offset + limit) + 1

                val paginatedData = PaginatedData(
                    items = domainList,
                    hasNextPage = hasNextPage,
                    currentPage = currentPage,
                    totalCount = cachedCount
                )
                return Result.success(paginatedData)
            }

            val response = api.getPokemonList(limit, offset)
            val paginatedData = response.toDomainModel(offset, limit)

            if (offset == 0 || cachedCount == 0) {
                repositoryScope.launch {
                    try {
                        val fullResponse = api.getPokemonList(1000, 0)
                        val fullList = fullResponse.results.map { it.toDomainModel().toEntity() }

                        pokemonDao.clearPokemonList()
                        pokemonDao.insertPokemonListItems(fullList)
                        Timber.d("Сохранен полный список в кэш: ${fullList.size} покемонов")
                    } catch (e: Exception) {
                        Timber.w(e, "Ошибка загрузки полного списка в фоне")
                    }
                }
            }
            Timber.d("Успешно загружен список из API: ${paginatedData.items.size} покемонов")
            Result.success(paginatedData)
        } catch (e: UnknownHostException) {
            Timber.e(e, "Ошибка подключения к интернету")
            val cachedList = pokemonDao.getPokemonListPaginated(limit, offset)
            val cachedCount = pokemonDao.getPokemonListCount()

            if (cachedList.isNotEmpty()) {
                Timber.d("Используем устаревший кэш из-за ошибки сети")
                val domainList = cachedList.map { it.toDomainModel() }
                val hasNextPage = (offset + limit) < cachedCount
                val currentPage = (offset + limit) + 1

                val paginatedData = PaginatedData(
                    items = domainList,
                    hasNextPage = hasNextPage,
                    currentPage = currentPage,
                    totalCount = cachedCount
                )
                Result.success(paginatedData)
            } else {
                Result.failure(Exception("Нет подключения к интернету. Проверьте соединение."))
            }
        } catch (e: IOException) {
            Timber.e(e, "Ошибка сети")
            Result.failure(Exception("Ошибка соединения с сервером. Попробуйте позже."))
        } catch (e: Exception) {
            Timber.e(e, "Неизвестная ошибка при загрузке списка покемонов")
            Result.failure(Exception("Ошибка загрузки: ${e.message}"))
        }
    }

    override suspend fun getPokemonDetails(id: String): Result<Pokemon> =
        withContext(Dispatchers.IO) {
            try {
                // Добавляем таймаут
                withTimeout(30_000) { // 30 секунд
                    // Проверяем, нет ли уже активного запроса для этого покемона
                    activeRequests[id]?.let { existingRequest ->
                        Timber.d("Переиспользуем существующий запрос для покемона $id")
                        return@withTimeout existingRequest.await()
                    }

                    val deferred = async {
                        try {
                            fetchPokemonWithCaching(id)
                        } finally {
                            activeRequests.remove(id)
                        }
                    }

                    activeRequests[id] = deferred
                    deferred.await()
                }
            } catch (e: TimeoutCancellationException) {
                Timber.e(e, "Timeout при загрузке покемона $id")
                Result.failure(Exception("Превышено время ожидания загрузки"))
            } catch (e: Exception) {
                Timber.e(e, "Ошибка загрузки покемона $id")
                Result.failure(e)
            }
        }

    private fun isCacheValid(lastUpdated: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastUpdated) < CACHE_DURATION_MS
    }

    override suspend fun clearCache() {
        pokemonDao.clearAllPokemon()
        pokemonDao.clearPokemonList()
    }

    override suspend fun getCachedPokemonCount(): Int {
        return pokemonDao.getPokemonListCount()
    }

    private suspend fun fetchPokemonWithCaching(id: String): Result<Pokemon> {
        val pokemonId = id.toInt()

        val cachedPokemon = pokemonDao.getPokemonById(pokemonId)
        if (cachedPokemon != null && isCacheValid(cachedPokemon.lastUpdated)) {
            val domainModel = cachedPokemon.toDomainModel()

            val cacheAge = System.currentTimeMillis() - cachedPokemon.lastUpdated
            if (cacheAge > BACKGROUND_REFRESH_THRESHOLD) {
                repositoryScope.launch {
                    refreshPokemonInBackground(id)
                }
            }

            return Result.success(domainModel)
        }

        return retryWithBackoff(maxAttempts = 3) {
            val response = api.getPokemonDetails(id)
            val domainModel = response.toDomainModel()

            repositoryScope.launch {
                pokemonDao.insertPokemon(domainModel.toEntity())
            }

            Result.success(domainModel)
        }

    }

    private suspend fun refreshPokemonInBackground(id: String) {
        try {
            val response = api.getPokemonDetails(id)
            val domainModel = response.toDomainModel()
            pokemonDao.insertPokemon(domainModel.toEntity())
            Timber.d("Фоновое обновление покемона $id завершено")
        } catch (e: Exception) {
            Timber.w(e, "Ошибка фонового обновления покемона $id")
        }
    }

    private suspend fun <T> retryWithBackoff(
        maxAttempts: Int = 3,
        initialDelay: Long = 1000,
        block: suspend () -> T
    ): T {
        var lastException: Exception? = null
        var delay = initialDelay

        repeat(maxAttempts) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                lastException = e
                if (attempt == maxAttempts - 1) {
                    // Это последняя попытка, выбрасываем исключение
                    throw lastException!!
                }

                Timber.w(e, "Попытка ${attempt + 1} неудачна, повторяем через ${delay}мс")
                delay(delay)
                delay *= 2 // Exponential backoff
            }
        }

        throw lastException ?: Exception("Все попытки исчерпаны")
    }


}