package ru.konovalovily.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class SubscribeResponseDto(
    val mangaId: String,
    val countOfNewChapters: Int
)
