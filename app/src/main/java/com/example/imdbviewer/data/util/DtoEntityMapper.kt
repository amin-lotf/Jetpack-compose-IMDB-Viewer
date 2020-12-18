package com.example.imdbviewer.data.util

interface DtoEntityMapper<Entity, Dto> {
    fun mapToDto(entity: Entity): Dto
    fun mapToEntity(dto: Dto): Entity?
}