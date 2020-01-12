package net.frozendevelopment.bridgeio.services.polling

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.frozendevelopment.bridgeio.BridgeContext
import net.frozendevelopment.bridgeio.ClientFactory
import net.frozendevelopment.bridgeio.clients.GroupClient
import net.frozendevelopment.bridgeio.dtos.GroupDTO
import net.frozendevelopment.bridgeio.services.*

class RoomPollingService private constructor() :
    BridgeService<Map<Int, GroupDTO>> {

    private val client = ClientFactory.buildService(GroupClient::class.java)

    private var isPolling: Boolean = false

    override fun getRunningStatus(): Boolean = isPolling

    override fun start(): Flow<BridgeServiceResult<Map<Int, GroupDTO>>> = flow {
        if (isPolling) {
            emit(
                BridgeServiceResult(
                    error = ServiceAlreadyRunningException()
                )
            )
            return@flow
        }

        isPolling = true

        while (isPolling) {
            if (BridgeContext.host.isNullOrBlank()) {
                emit(
                    BridgeServiceResult(
                        error = InvalidBridgeHostException()
                    )
                )
                delay(5000)
                continue
            }

            if (BridgeContext.token.isNullOrBlank()) {
                emit(
                    BridgeServiceResult(
                        error = InvalidBridgeTokenException()
                    )
                )
                delay(5000)
                continue
            }

            try {
                emit(
                    BridgeServiceResult(
                        data = client.getGroups()
                    )
                )
            } catch (e: Exception) {
                emit(
                    BridgeServiceResult(
                        error = e
                    )
                )
            }
            delay(1000)
        }
    }

    override fun stop() {
        isPolling = false
    }

    companion object {
        private var instance: RoomPollingService? = null

        fun getInstance(): RoomPollingService {
            if (instance != null) {
                return instance!!
            }

            instance =
                RoomPollingService()
            return instance!!
        }
    }
}
