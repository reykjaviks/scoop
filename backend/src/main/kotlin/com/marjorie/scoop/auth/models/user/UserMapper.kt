package com.marjorie.scoop.auth.models.user

import com.marjorie.scoop.auth.models.user.dto.UserDTO
import com.marjorie.scoop.auth.models.user.dto.UserSlimDTO
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface UserMapper {
    fun mapToUserDTO(userEntitiy: UserEntity): UserDTO
    fun mapToUserEntity(userDTO: UserDTO): UserEntity
    fun mapToUserSlimDTO(userEntitiy: UserEntity): UserSlimDTO
    fun mapToUserEntity(userSlimDTO: UserSlimDTO): UserEntity
}