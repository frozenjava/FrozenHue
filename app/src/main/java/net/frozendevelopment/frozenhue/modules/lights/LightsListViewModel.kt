package net.frozendevelopment.frozenhue.modules.lights

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import io.realm.Realm
import kotlinx.coroutines.launch
import net.frozendevelopment.cache.models.LightModel

import net.frozendevelopment.frozenhue.infrustructure.StatefulViewModel

class LightsListViewModel : StatefulViewModel<LightsListState>(), LifecycleObserver {

    override fun getDefaultState(): LightsListState = LightsListState()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun startObserving() = viewModelScope.launch {
        val realm = Realm.getDefaultInstance()

        realm.where(LightModel::class.java).findAllAsync().addChangeListener { results ->
            state = state.copy(lights = results.toList())
        }
    }
}