package com.example.imdbviewer.data.network.util

interface EntityMapper<Entity,DomainModel> {
    fun mapFromEntity(entity: Entity):DomainModel
    fun mapToEntity(domainModel: DomainModel):Entity
}