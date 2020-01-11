package net.frozendevelopment.frozenhue.modules.setupbridge

data class SetupBridgeState(
    val setupStage: BridgeSetupStage = BridgeSetupStage.LOOKING_FOR_BRIDGE,
    val label: String? = null,
    val host: String? = null,
    val previousHosts: List<String>? = null,
    val discovering: Boolean = false
)

enum class BridgeSetupStage {
    LOOKING_FOR_BRIDGE,
    WAITING_FOR_TOKEN,
    DONE
}
