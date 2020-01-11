package net.frozendevelopment.frozenhue.modules.setupbridge

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.frozendevelopment.frozenhue.infrustructure.StatefulViewModel
import net.frozendevelopment.frozenhue.servicehandlers.BridgeSetupHandler


class SetupBridgeViewModel(private val bridgeHandler: BridgeSetupHandler): StatefulViewModel<SetupBridgeState>(), LifecycleObserver {

    override fun getDefaultState(): SetupBridgeState = SetupBridgeState()

    var host: String?
        get() = _state.host
        set(value) {
            _state = _state.copy(host=value)
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startDiscovery() = viewModelScope.launch(Dispatchers.IO) {
        if (_state.setupStage != BridgeSetupStage.LOOKING_FOR_BRIDGE) {
            return@launch
        }

        _state = _state.copy(discovering = true, label = "Looking for bridge...")

        bridgeHandler.discover(this@SetupBridgeViewModel::handleDiscoveryError) {
            bridgeHandler.stopDiscovery()
            _state = _state.copy(
                host = it.host,
                discovering = bridgeHandler.isDiscovering,
                label = "Bridge Discovered!",
                setupStage = BridgeSetupStage.WAITING_FOR_TOKEN)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopDiscovery() {
        bridgeHandler.stopDiscovery()
        _state = _state.copy(discovering = false)
    }

    fun attemptBridgeAuth() = viewModelScope.launch(Dispatchers.IO) {

    }

    private fun handleDiscoveryError(exception: Exception) {}

}
