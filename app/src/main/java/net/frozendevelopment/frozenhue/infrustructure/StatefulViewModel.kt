package net.frozendevelopment.frozenhue.infrustructure

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class StatefulViewModel<TState>: ViewModel() {

    val state: MutableLiveData<TState> = MutableLiveData(getDefaultState())

    protected var _state: TState
        get() = state.value ?: getDefaultState()
        set(value) { state.postValue(value) }

    protected abstract fun getDefaultState(): TState

}
