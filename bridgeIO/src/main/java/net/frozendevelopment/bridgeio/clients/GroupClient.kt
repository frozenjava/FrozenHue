package net.frozendevelopment.bridgeio.clients

import net.frozendevelopment.bridgeio.dtos.GroupDTO
import retrofit2.http.GET

interface GroupClient {

    @GET("groups")
    suspend fun getGroups() : Map<Int, GroupDTO>

}
