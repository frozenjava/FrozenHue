package net.frozendevelopment.frozenhue.modules.setupbridge.auth

data class BridgeAuthState(
    val isBusy: Boolean = false,
    val label: String? = null,
    val token: String? = null
)