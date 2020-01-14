package net.frozendevelopment.frozenhue.infrustructure

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class StatefulViewModel<TState> : ViewModel() {

    val liveState: MutableLiveData<TState> = MutableLiveData(getDefaultState())

    var state: TState
        get() = liveState.value ?: getDefaultState()
        set(value) {
            liveState.postValue(value)
        }

    protected abstract fun getDefaultState(): TState
}
