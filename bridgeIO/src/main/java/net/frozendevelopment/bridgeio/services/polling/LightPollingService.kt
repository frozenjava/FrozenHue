package net.frozendevelopment.bridgeio.services.polling

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.frozendevelopment.bridgeio.BridgeContext
import net.frozendevelopment.bridgeio.ClientFactory
import net.frozendevelopment.bridgeio.clients.LightClient
import net.frozendevelopment.bridgeio.dtos.LightDTO
import net.frozendevelopment.bridgeio.services.*
import java.lang.Exception

interface LightPollingServiceType : BridgeService<Map<Int, LightDTO>>

class LightPollingService private constructor() : LightPollingServiceType {

    private val client = ClientFactory.buildService(LightClient::class.java)

    private var isPolling: Boolean = false

    override fun getRunningStatus(): Boolean = isPolling

    override fun start(): Flow<BridgeServiceResult<Map<Int, LightDTO>>> = flow {
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
                        data = client.getLights()
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
        private var instance: LightPollingService? = null

        fun getInstance(): LightPollingService {
            if (instance != null) {
                return instance!!
            }

            instance =
                LightPollingService()
            return instance!!
        }
    }
}
