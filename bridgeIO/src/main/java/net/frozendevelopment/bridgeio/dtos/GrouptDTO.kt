package net.frozendevelopment.bridgeio.dtos

data class GroupDTO(
    val id: Int,
    val name: String,
    val lights: List<Int>,
    val type: String,
    val state: GroupStateDTO,
    val recycle: Boolean,
    val `class`: String,
    val action: GroupActionDTO
)
