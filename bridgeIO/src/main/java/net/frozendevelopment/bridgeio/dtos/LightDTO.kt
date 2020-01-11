package net.frozendevelopment.bridgeio.dtos

import com.google.gson.annotations.SerializedName

data class LightDTO(
    val id: Int,
    val state: LightStateDTO,
    val name: String?,
    val type: String?,
    @SerializedName("modelid") val modelId: String?,
    @SerializedName("uniqueid") val uniqueId: String?,
    @SerializedName("productid") val productId: String?,
    @SerializedName("productname") val productName: String?,
    val manufacturer: String?,
    @SerializedName("swversion") val swVersion: String?,
    @SerializedName("swconfig") val swConfig: String?
)
