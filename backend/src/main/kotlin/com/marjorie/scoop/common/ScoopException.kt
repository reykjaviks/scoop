package com.marjorie.scoop.common

class ScoopResourceAlreadyExistsException(message: String): Exception(message)
class ScoopResourceNotFoundException(message: String): Exception(message)
class ScoopBadRequestException(message: String): Exception(message)