package net.frozendevelopment.bridgeio.clients

import net.frozendevelopment.bridgeio.dtos.LightDTO
import net.frozendevelopment.bridgeio.dtos.LightStateDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface LightClient {

    @GET("lights")
    suspend fun getLights(): Map<Int, LightDTO>

    @GET("lights/{id}")
    suspend fun getLightById(
        @Path("id") id: Int
    ): LightDTO?

    @PUT("lights/{id}/state")
    suspend fun updateLight(
        @Path("id") id: Int,
        @Body light: LightStateDTO
    ): List<Map<String, Any>>
}
