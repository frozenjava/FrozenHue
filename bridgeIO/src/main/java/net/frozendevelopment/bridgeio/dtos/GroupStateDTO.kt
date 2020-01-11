package net.frozendevelopment.bridgeio.dtos

import com.google.gson.annotations.SerializedName

data class GroupStateDTO(
    @SerializedName("all_on") val allOn: Boolean,
    @SerializedName("any_on") val anyOn: Boolean
)