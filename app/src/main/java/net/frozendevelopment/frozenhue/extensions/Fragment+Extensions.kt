package net.frozendevelopment.frozenhue.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <TState> Fragment.observeLiveEvents(source: LiveData<TState>, onUpdate: (TState) -> Unit) {
    source.observe(this, Observer {
        it ?: return@Observer
        onUpdate.invoke(it)
    })
}

fun <TState> Fragment.observeLiveEvents(source: MutableLiveData<TState>, onUpdate: (TState) -> Unit) {
    source.observe(this, Observer {
        it ?: return@Observer
        onUpdate.invoke(it)
    })
}
