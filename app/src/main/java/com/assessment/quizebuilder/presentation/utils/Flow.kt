package com.assessment.quizebuilder.presentation.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <T> Flow<T>.collectWhenStarted(
    owner: LifecycleOwner,
    crossinline action: suspend (T) -> Unit
) {
    owner.lifecycleScope.launch {
        owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collect {
                action(it)
            }
        }
    }
}

inline fun <T> Flow<T>.collectWhenResumed(
    owner: LifecycleOwner,
    crossinline action: suspend (T) -> Unit
) {
    owner.lifecycleScope.launch {
        owner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            collect {
                action(it)
            }
        }
    }
}

inline fun <T> Flow<T>.collectWhenCreated(
    owner: LifecycleOwner,
    crossinline action: suspend (T) -> Unit
) {
    owner.lifecycleScope.launch {
        owner.repeatOnLifecycle(Lifecycle.State.CREATED) {
            collect {
                action(it)
            }
        }
    }
}