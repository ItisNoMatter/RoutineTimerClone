package jp.itIsNoMatter.routineTimerClone.core.ext

import androidx.lifecycle.SavedStateHandle

inline fun <reified T> SavedStateHandle.require(key: String): T {
    return this[key] ?: error("SavedStateHandle does not contain key '$key'")
}
