package com.example.imdbviewer.data.util

interface DomainMapper<T,DomainModel> {
    fun mapToDomainModel(from: T):DomainModel
    fun mapFromDomainModel(from: DomainModel):T?
}