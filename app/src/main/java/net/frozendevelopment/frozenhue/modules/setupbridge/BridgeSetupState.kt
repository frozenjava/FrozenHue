package net.frozendevelopment.frozenhue.modules.setupbridge

data class BridgeSetupState(
    var stage: BridgeSetupStage = BridgeSetupStage.DISCOVERY,
    var discoveryState: BridgeDiscoveryState = BridgeDiscoveryState(),
    var linkState: BridgeLinkState = BridgeLinkState()
)

enum class BridgeSetupStage(val label: String) {
    DISCOVERY("Connect to a Bridge"),
    LINKING("Link with the Bridge"),
    SAVE("Almost Done"),
    DONE("")
}

data class BridgeDiscoveryState(
    var host: String? = null,
    var previousHosts: List<String>? = null,
    var discovering: Boolean = false,
    var discoveryLabel: String? = null
)

data class BridgeLinkState(
    var pairing: Boolean = false,
    var label: String? = null,
    var token: String? = null
)
