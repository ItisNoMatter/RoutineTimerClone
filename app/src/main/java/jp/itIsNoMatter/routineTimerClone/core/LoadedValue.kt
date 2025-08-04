package jp.itIsNoMatter.routineTimerClone.core

sealed interface LoadedValue<out T> {
    data class Done<T>(val value: T) : LoadedValue<T>

    data object Loading : LoadedValue<Nothing>

    data class Error(val throwable: Throwable? = null) : LoadedValue<Nothing>
}

fun <T> LoadedValue<T>.getOrNull(): T? {
    return when (this) {
        is LoadedValue.Done -> value
        is LoadedValue.Error -> null
        is LoadedValue.Loading -> null
    }
}
