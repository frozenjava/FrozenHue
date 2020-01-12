package net.frozendevelopment.bridgeio.services

import kotlinx.coroutines.flow.Flow

interface BridgeService<TFlowResult> {

    fun getRunningStatus(): Boolean

    fun start(): Flow<BridgeServiceResult<TFlowResult>>

    fun stop()
}
