package com.example.neugelb.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class CombinedLiveData<F, S, R>(
    private val first: LiveData<F>,
    private val second: LiveData<S>,
    private val combineData: (CombinedLiveData<F, S, R>, F, S) -> Unit
) : MediatorLiveData<R>() {

    private var firstEmitted = false
    private var secondEmitted = false

    init {
        addSource(first) {
            firstEmitted = true
            combineWhenDone()
        }
        addSource(second) {
            secondEmitted = true
            combineWhenDone()
        }
    }

    private fun combineWhenDone() {
        if (firstEmitted && secondEmitted) {
            combineData(this, first.value as F, second.value as S)
        }
    }
}