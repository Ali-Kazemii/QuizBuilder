package com.assessment.quizebuilder.core

import androidx.annotation.MainThread
import androidx.collection.ArraySet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

open class SingleLiveEvent<T> : MediatorLiveData<T>() {

    private val observers = ArraySet<ObserverWrapper<in T>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun observeForever(observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observeForever(wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        if (observers.remove(wrapper)) {
            super.removeObserver(wrapper)
            return
        }
        val iterator = observers.iterator()
        while (iterator.hasNext()) {
            val wrapperNext = iterator.next()
            if (wrapperNext.observer == observer) {
                iterator.remove()
                super.removeObserver(wrapperNext)
                break
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        observers.forEach { it.newValue() }
        super.setValue(t)
    }

    override fun postValue(value: T?) {
        observers.forEach { it.newValue() }
        super.postValue(value)
    }

    private class ObserverWrapper<T>(val observer: Observer<T>) : Observer<T> {

        private val mPending = AtomicBoolean(false)

        override fun onChanged(t: T?) {
            if (mPending.get()) {
                mPending.set(false)
                observer.onChanged(t)
            }
        }

        fun newValue() {
            mPending.set(true)
        }
    }
}