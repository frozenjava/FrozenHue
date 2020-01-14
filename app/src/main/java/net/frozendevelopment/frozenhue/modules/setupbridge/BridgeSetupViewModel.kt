package net.frozendevelopment.frozenhue.modules.setupbridge

import androidx.lifecycle.viewModelScope
import io.realm.Case
import io.realm.Realm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.frozendevelopment.bridgeio.BridgeContext
import net.frozendevelopment.cache.models.BridgeModel
import net.frozendevelopment.frozenhue.infrustructure.StatefulViewModel
import net.frozendevelopment.frozenhue.servicehandlers.BridgeSetupHandler

class BridgeSetupViewModel(private val bridgeHandler: BridgeSetupHandler) : StatefulViewModel<BridgeSetupState>() {

    override fun getDefaultState(): BridgeSetupState = BridgeSetupState()

    var discoveryState: BridgeDiscoveryState
        get() = state.discoveryState
        set(value) { state = state.copy(discoveryState = value) }

    var linkState: BridgeLinkState
        get() = state.linkState
        set(value) { state = state.copy(linkState = value) }

    var saveState: BridgeSaveState
        get() = state.saveState
        set(value) { state = state.copy(saveState = value) }

    var discoveryHost: String?
        get() = discoveryState.host
        set(value) { discoveryState = discoveryState.copy(host = value) }

    var bridgeName: String?
        get() = saveState.bridgeName
        set(value) { saveState = saveState.copy(bridgeName = value) }

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
        discoveryState = discoveryState.copy(
            discovering = bridgeHandler.isDiscovering,
            discoveryLabel = "Discovery stopped."
        )
    }

    fun validateHostAndContinue() {
        BridgeContext.host = discoveryState.host
        goToNext()
    }

    private fun handleDiscoveryError(exception: Exception) {
        discoveryState = discoveryState.copy(
            discovering = bridgeHandler.isDiscovering,
            discoveryLabel = exception.localizedMessage ?: "An error occurred."
        )
    }

    fun startLinking() = viewModelScope.launch(Dispatchers.IO) {
        if (state.stage != BridgeSetupStage.LINKING) {
            return@launch
        }

        linkState = linkState.copy(pairing = true, label = "Press Link button on Bridge.")

        bridgeHandler.link(this@BridgeSetupViewModel::handleLinkingError) { token ->
            stopLinking()
            linkState = linkState.copy(pairing = false, label = "Bridge Linked!", token = token)
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

    fun save() {
        if (saveState.bridgeName.isNullOrEmpty()) {
            saveState = saveState.copy(error = "Please enter a name.")
            return
        }

        val realm = Realm.getDefaultInstance()

        val existingModel = realm.where(BridgeModel::class.java)
            .equalTo("name", saveState.bridgeName, Case.INSENSITIVE)
            .findFirst()

        if (existingModel != null) {
            saveState = saveState.copy(error = "A bridge already exists with name.")
            return
        }

        saveState = saveState.copy(error = null)

        realm.executeTransactionAsync {
            it.insertOrUpdate(BridgeModel(
                name = saveState.bridgeName!!,
                host = discoveryState.host!!,
                token = linkState.token!!
            ))
        }

        goToNext()
    }
}
