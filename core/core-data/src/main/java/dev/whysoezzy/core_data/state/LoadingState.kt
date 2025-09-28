package dev.whysoezzy.core_data.state

import androidx.compose.runtime.Immutable

@Immutable
sealed class LoadingState {
    @Immutable
    data object Idle : LoadingState()

    @Immutable
    data object Loading : LoadingState()

    @Immutable
    data class LoadingMore(val currentItems: Int) : LoadingState()

    @Immutable
    data object Refreshing : LoadingState()

    @Immutable
    data object Success : LoadingState()

    @Immutable
    data class Error(
        val message: String,
        val isRetry: Boolean = true,
        val hasExistingData: Boolean = false
    ) : LoadingState()

}

val LoadingState.isLoading: Boolean
    get() = this is LoadingState.Loading

val LoadingState.isLoadingMore: Boolean
    get() = this is LoadingState.LoadingMore

val LoadingState.isRefreshing: Boolean
    get() = this is LoadingState.Refreshing

val LoadingState.hasError: Boolean
    get() = this is LoadingState.Error

val LoadingState.isSuccess: Boolean
    get() = this is LoadingState.Success

val LoadingState.isIdle: Boolean
    get() = this is LoadingState.Idle

/**
 * Получение ошибки если состояние Error
 */
val LoadingState.error: LoadingState.Error?
    get() = this as? LoadingState.Error

/**
 * Получение сообщения об ошибке
 */
val LoadingState.errorMessage: String?
    get() = error?.message

/**
 * Можно ли показать контент (есть данные или загружается больше)
 */
fun LoadingState.canShowContent(hasData: Boolean): Boolean =
    hasData || isLoadingMore || (hasError && error?.hasExistingData == true)

/**
 * Нужно ли показать только индикатор загрузки
 */
fun LoadingState.shouldShowLoadingOnly(hasData: Boolean): Boolean =
    isLoading && !hasData