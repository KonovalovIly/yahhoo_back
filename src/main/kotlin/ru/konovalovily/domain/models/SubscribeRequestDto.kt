package ru.konovalovily.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class SubscribeRequestDto(
    val mangaId: String,
    val lastChapter: String
)