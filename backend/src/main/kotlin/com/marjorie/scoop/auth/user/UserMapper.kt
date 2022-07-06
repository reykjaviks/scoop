package com.marjorie.scoop.auth.user

import com.marjorie.scoop.auth.user.dto.UserDTOIdUsername
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface UserMapper {
    fun userEntityToSimplestUserDTO(userEntitiy: User): UserDTOIdUsername
    fun simplestUserDTOToUserEntity(userDTOIdUsername: UserDTOIdUsername): User
}