package net.frozendevelopment.frozenhue.modules.lights

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import kotlinx.coroutines.launch
import net.frozendevelopment.cache.models.LightModel
import net.frozendevelopment.frozenhue.extensions.toLightState

import net.frozendevelopment.frozenhue.infrustructure.StatefulViewModel
import net.frozendevelopment.frozenhue.servicehandlers.LightPollingServiceHandler

class LightsListViewModel(
    private val pollingServiceHandler: LightPollingServiceHandler
) : StatefulViewModel<LightsListState>(), LifecycleObserver {

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    private val changeListener = RealmChangeListener<RealmResults<LightModel>> { results ->
        state = state.copy(lights = results.map { it.toLightState() })
    }

    override fun getDefaultState(): LightsListState = LightsListState()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun subscribeToRealm() {
        realm.where(LightModel::class.java).findAllAsync().addChangeListener(changeListener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unsubscribeFromRealm() { realm.removeChangeListener(changeListener as RealmChangeListener<Realm>) }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startPollBridge() = viewModelScope.launch { pollingServiceHandler.start(::handlePollingException) }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopPollingBridge() = pollingServiceHandler.stop()

    private fun handlePollingException(e: Exception) {
    }
}
