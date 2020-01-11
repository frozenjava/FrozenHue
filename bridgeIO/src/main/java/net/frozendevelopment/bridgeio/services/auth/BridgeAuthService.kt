package net.frozendevelopment.bridgeio.services.auth

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.runBlocking
import net.frozendevelopment.bridgeio.BridgeContext
import net.frozendevelopment.bridgeio.ClientFactory
import net.frozendevelopment.bridgeio.clients.RegistrationClient
import net.frozendevelopment.bridgeio.clients.registerResponseType
import net.frozendevelopment.bridgeio.dtos.RegistrationInputDTO
import net.frozendevelopment.bridgeio.services.BridgeService
import net.frozendevelopment.bridgeio.services.BridgeServiceResult
import net.frozendevelopment.bridgeio.services.ServiceAlreadyRunningException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BridgeAuthService private constructor(): BridgeService<String> {

    private val client = ClientFactory.buildService(RegistrationClient::class.java)
    private var isPolling: Boolean = false

    override fun getRunningStatus(): Boolean = isPolling

    override fun start(): Flow<BridgeServiceResult<String>> = channelFlow {
        if (isPolling) {
            send(BridgeServiceResult(error=ServiceAlreadyRunningException()))
            return@channelFlow
        }

        isPolling = true

        while (isPolling) {
            val x = BridgeContext.host
            attemptAuth({ exception ->
                send(BridgeServiceResult(error=exception))
            }, { token ->
                send(BridgeServiceResult(data = token))
                this@BridgeAuthService.stop()
            })
            delay(5000)
        }
    }

    override fun stop() {
        isPolling = false
    }

    private suspend fun attemptAuth(onError: suspend (e: Exception) -> Unit, onSuccess: suspend (String) -> Unit) {
        client.register(RegistrationInputDTO()).enqueue(object: Callback<registerResponseType> {
            override fun onFailure(call: Call<registerResponseType>, t: Throwable) = runBlocking {
                onError(Exception(t.localizedMessage))
            }

            override fun onResponse(call: Call<registerResponseType>, response: Response<registerResponseType>) = runBlocking {
                val payload = response.body()?.firstOrNull()

                if (payload == null) {
                    onError(Exception("Unable to read bridge response."))
                    return@runBlocking
                }

                if (payload.get("error") != null) {
                    val error = payload.get("error") as Map<String, Any>
                    val err: String = error.get("description") as? String ?: "Unknown error."
                    onError(Exception(err))
                    return@runBlocking
                }

                if (payload.get("success") != null) {
                    val successBody = payload.get("success") as Map<String, Any>
                    val token = successBody.get("username") as? String
                    if (token != null) {
                        onSuccess(token)
                    } else {
                        onError(Exception("Unable to read bridge response."))
                    }
                    return@runBlocking
                }

                onError(Exception("Unable to read bridge response."))
            }
        })
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
