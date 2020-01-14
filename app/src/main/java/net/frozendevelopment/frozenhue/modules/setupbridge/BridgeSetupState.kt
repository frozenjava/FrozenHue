package net.frozendevelopment.frozenhue.modules.setupbridge

data class BridgeSetupState(
    val stage: BridgeSetupStage = BridgeSetupStage.DISCOVERY,
    val discoveryState: BridgeDiscoveryState = BridgeDiscoveryState(),
    val linkState: BridgeLinkState = BridgeLinkState(),
    val saveState: BridgeSaveState = BridgeSaveState()
)

enum class BridgeSetupStage(val label: String) {
    DISCOVERY("Connect to a Bridge"),
    LINKING("Link with the Bridge"),
    SAVE("Almost Done"),
    DONE("")
}

data class BridgeDiscoveryState(
    val host: String? = null,
    val previousHosts: List<String>? = null,
    val discovering: Boolean = false,
    val discoveryLabel: String? = null
)

data class BridgeLinkState(
    val pairing: Boolean = false,
    val label: String? = null,
    val token: String? = null
)

data class BridgeSaveState(
    val label: String = "Save the Bridge.",
    val bridgeName: String? = null,
    val error: String? = null,
    val saving: Boolean = false
)
