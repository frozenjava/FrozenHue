package net.frozendevelopment.bridgeio.services.auth

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.frozendevelopment.bridgeio.ClientFactory
import net.frozendevelopment.bridgeio.clients.RegistrationClient
import net.frozendevelopment.bridgeio.dtos.RegistrationInputDTO
import net.frozendevelopment.bridgeio.services.BridgeService
import net.frozendevelopment.bridgeio.services.BridgeServiceResult
import net.frozendevelopment.bridgeio.services.ServiceAlreadyRunningException

class BridgeAuthService private constructor() : BridgeService<String> {

    private val client = ClientFactory.buildService(RegistrationClient::class.java)
    private var isPolling: Boolean = false

    override fun getRunningStatus(): Boolean = isPolling

    override fun start(): Flow<BridgeServiceResult<String>> = flow {
        if (isPolling) {
            emit(BridgeServiceResult(error = ServiceAlreadyRunningException()))
            return@flow
        }

        isPolling = true

        while (isPolling) {
            attemptAuth({ exception ->
                emit(BridgeServiceResult(error = exception))
            }, { token ->
                emit(BridgeServiceResult(data = token))
                this@BridgeAuthService.stop()
            })
            delay(5000)
        }
    }

    override fun stop() {
        isPolling = false
    }

    private suspend fun attemptAuth(
        onError: suspend (e: Exception) -> Unit,
        onSuccess: suspend (String) -> Unit
    ) {
        val response = try {
            client.register(RegistrationInputDTO())
        } catch (e: Exception) {
            onError(e)
            return
        }

        val payload = response.firstOrNull()

        if (payload == null) {
            onError(Exception("Unable to read bridge response."))
            return
        }

        if (payload["error"] != null) {
            val error = payload["error"] as Map<String, Any>
            val err: String = error["description"] as? String ?: "Unknown error."
            onError(Exception(err))
            return
        }

        if (payload["success"] != null) {
            val successBody = payload["success"] as Map<String, Any>
            val token = successBody["username"] as? String
            if (token != null) {
                onSuccess(token)
            } else {
                onError(Exception("Unable to read bridge response."))
            }
            return
        }

        onError(Exception("Unable to read bridge response."))
    }

    companion object {
        private var instance: BridgeAuthService? = null

        fun getInstance(): BridgeAuthService {
            if (instance == null) {
                instance = BridgeAuthService()
            }

            return instance!!
        }
    }
}
