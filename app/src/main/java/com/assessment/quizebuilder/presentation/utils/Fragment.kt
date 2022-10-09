package com.assessment.quizebuilder.presentation.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController

fun <T> Fragment.getBackStackDataFromDialog(
    fragmentNavId: Int,
    key: String,
    singleCall: Boolean = true,
    resultCallback: (T) -> (Unit)
) {
    val navBackStackEntry =
        findNavController().getBackStackEntry(fragmentNavId)

    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            if (navBackStackEntry.savedStateHandle.contains(key)) {
                val result =
                    navBackStackEntry.savedStateHandle.get<T>(key)
                if (singleCall)
                    navBackStackEntry.savedStateHandle.remove<T>(key)
                // Do something with the result
                result?.let {
                    resultCallback(it)
                }
            }
        }
    }

    navBackStackEntry.lifecycle.addObserver(observer)
    viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            navBackStackEntry.lifecycle.removeObserver(observer)
        }
    })
}

fun <T> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = false) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
    if (doBack)
        findNavController().popBackStack()
}