package net.frozendevelopment.bridgeio.services

data class BridgeServiceResult<T>(
    val error: Exception? = null,
    val data: T? = null
)
