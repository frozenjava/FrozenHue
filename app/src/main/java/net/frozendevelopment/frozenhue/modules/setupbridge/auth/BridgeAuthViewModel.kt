package net.frozendevelopment.frozenhue.modules.setupbridge.auth

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.frozendevelopment.frozenhue.infrustructure.StatefulViewModel
import net.frozendevelopment.frozenhue.servicehandlers.BridgeSetupHandler

class BridgeAuthViewModel(private val bridgeHandler: BridgeSetupHandler): StatefulViewModel<BridgeAuthState>(), LifecycleObserver {

    override fun getDefaultState(): BridgeAuthState = BridgeAuthState()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun waitForToken() = viewModelScope.launch(Dispatchers.IO) {
        _state = _state.copy(isBusy = true)

        bridgeHandler.authenticate(this@BridgeAuthViewModel::handleAuthError) {
            _state = _state.copy(isBusy = false, label = "Connected!")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopWaitingForToken() {
        bridgeHandler.stopAuthenticating()
        _state = _state.copy(isBusy = false)
    }

    private fun handleAuthError(exception: Exception) {
        _state = _state.copy(label = exception.localizedMessage)
    }

}
