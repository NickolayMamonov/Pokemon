package dev.whysoezzy.core_data.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Базовый класс для ViewModels с правильным паттерном загрузки данных
 *
 * Решает проблемы:
 * - Загрузка данных в init {} блоке (антипаттерн)
 * - Race conditions с UI
 * - Сложности с тестированием
 * - Отсутствие контроля над свежестью данных
 *
 * @param State - тип состояния UI
 * @param Intent - пользовательские намерения/действия
 * @param Trigger - внутренние триггеры для изменения состояния
 */
abstract class ViewModelLoader<State : Any, Intent : Any, Trigger : Any> : ViewModel() {

    private val _trigger by lazy { MutableSharedFlow<Trigger>() }

    /**
     * Создает StateFlow для загрузки данных с умным кэшированием
     *
     * @param initialState - начальное состояние
     * @param loadData - функция первоначальной загрузки данных
     * @param triggerData - функция обработки триггеров (refresh, user actions)
     * @param timeout - время кэширования в миллисекундах (0 = без кэша)
     */
    protected fun <T> loadData(
        initialState: T,
        loadData: suspend FlowCollector<T>.(currentState: T) -> Unit,
        triggerData: (suspend FlowCollector<T>.(currentState: T, triggerParams: Trigger) -> Unit)? = null,
        timeout: Long = 5000L // Соответствует Android ANR timeout
    ): StateFlow<T> {
        var latestValue = initialState

        return flow {
            // Всегда эмитим текущее состояние первым (для кэширования)
            emit(latestValue)

            // Выполняем первоначальную загрузку
            loadData(latestValue)

            // Обрабатываем триггеры если они есть
            if (triggerData != null) {
                _trigger.collect { triggerParams ->
                    triggerData(this, latestValue, triggerParams)
                }
            }
        }
            .distinctUntilChanged() // Предотвращаем дублирующиеся эмиссии
            .onEach { latestValue = it } // Сохраняем последнее состояние для кэширования
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(timeout),
                initialValue = latestValue
            )
    }

    /**
     * Упрощенная версия без триггеров для простых случаев
     */
    protected fun <T> loadData(
        initialState: T,
        loadData: suspend FlowCollector<T>.(currentState: T) -> Unit,
        timeout: Long = 5000L
    ): StateFlow<T> = loadData(initialState, loadData, null, timeout)

    /**
     * Основное состояние ViewModel - должно быть переопределено в наследниках
     */
    abstract val state: StateFlow<State>

    /**
     * Текущее состояние для удобного доступа
     */
    val currentState: State get() = state.value

    /**
     * Обработка пользовательских намерений - переопределяется в наследниках
     */
    open fun onIntent(intent: Intent) {}

    /**
     * Отправка внутренних триггеров для изменения состояния
     */
    protected fun sendTrigger(trigger: Trigger) {
        viewModelScope.launch {
            _trigger.emit(trigger)
        }
    }
}

/**
 * Интерфейс для ViewModels с обработкой намерений
 */
interface IntentHandler<Intent> {
    fun onIntent(intent: Intent)
}