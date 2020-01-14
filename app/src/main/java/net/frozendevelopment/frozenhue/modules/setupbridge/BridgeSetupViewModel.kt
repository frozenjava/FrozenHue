package net.frozendevelopment.frozenhue.modules.setupbridge

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.frozendevelopment.bridgeio.BridgeContext
import net.frozendevelopment.frozenhue.infrustructure.StatefulViewModel
import net.frozendevelopment.frozenhue.servicehandlers.BridgeSetupHandler

class BridgeSetupViewModel(private val bridgeHandler: BridgeSetupHandler) : StatefulViewModel<BridgeSetupState>() {

    override fun getDefaultState(): BridgeSetupState = BridgeSetupState()

    var discoveryState: BridgeDiscoveryState
        get() = state.discoveryState
        set(value) {
            state = state.copy(discoveryState = value)
        }

    var discoveryHost: String?
        get() = discoveryState.host
        set(value) { discoveryState = discoveryState.copy(host = value) }

    var linkState: BridgeLinkState
        get() = state.linkState
        set(value) {
            state.linkState = value
        }

    fun goToNext() {
        val next = when (state.stage) {
            BridgeSetupStage.DISCOVERY -> BridgeSetupStage.LINKING
            BridgeSetupStage.LINKING -> BridgeSetupStage.SAVE
            BridgeSetupStage.SAVE -> BridgeSetupStage.DONE
            BridgeSetupStage.DONE -> BridgeSetupStage.DISCOVERY
        }
        state = state.copy(stage = next)
    }

    fun startDiscovery() = viewModelScope.launch(Dispatchers.IO) {
        if (state.stage != BridgeSetupStage.DISCOVERY) {
            return@launch
        }

        discoveryState = discoveryState.copy(
            discovering = true,
            discoveryLabel = "Discovering Bridge..."
        )

        bridgeHandler.discover(this@BridgeSetupViewModel::handleDiscoveryError) {
            this@BridgeSetupViewModel.stopDiscovery()
            discoveryState = discoveryState.copy(
                host = it.host,
                discoveryLabel = "Bridge discovered."
            )
        }
    }

    fun stopDiscovery() {
        if (state.stage != BridgeSetupStage.DISCOVERY) {
            return
        }

        bridgeHandler.stopDiscovery()
        discoveryState.discovering = bridgeHandler.isDiscovering
        discoveryState.discoveryLabel = "Discovery stopped."
    }

    fun validateHostAndContinue() {
        BridgeContext.host = discoveryState.host
        goToNext()
    }

    private fun handleDiscoveryError(exception: Exception) {
        discoveryState.discovering = bridgeHandler.isDiscovering
        discoveryState.discoveryLabel = exception.localizedMessage ?: "An error occurred."
    }

    fun startLinking() = viewModelScope.launch(Dispatchers.IO) {
        if (state.stage != BridgeSetupStage.LINKING) {
            return@launch
        }

        linkState = linkState.copy(pairing = true)

        bridgeHandler.link(this@BridgeSetupViewModel::handleLinkingError) {
            linkState = linkState.copy(pairing = false, label = "Linked!")
        }
    }

    fun stopLinking() {
        if (state.stage != BridgeSetupStage.LINKING) {
            return
        }

        bridgeHandler.stopLinking()
        linkState = linkState.copy(pairing = false, label = "Stopped Linking.")
    }

    private fun handleLinkingError(exception: Exception) {
        linkState = linkState.copy(
            label = exception.localizedMessage,
            pairing = bridgeHandler.isLinking
        )
    }
}
