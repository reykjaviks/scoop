package com.marjorie.scoop.auth.user

import com.marjorie.scoop.auth.user.dto.UserSlimDTO
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface UserMapper {
    fun mapToUserSlimDTO(userEntitiy: UserEntity): UserSlimDTO
    fun mapToUserEntity(userSlimDTO: UserSlimDTO): UserEntity
}