package net.frozendevelopment.bridgeio.clients

import net.frozendevelopment.bridgeio.dtos.RegistrationInputDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

typealias registerResponseType = List<Map<String, Map<String, Any>>>

interface RegistrationClient {

    @POST("/")
    suspend fun register(
        @Body input: RegistrationInputDTO
    ): registerResponseType

    @GET("{token}")
    suspend fun checkToken(
        @Path("token") token: String
    ): Any
}
