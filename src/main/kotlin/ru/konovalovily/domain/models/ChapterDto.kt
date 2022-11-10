package ru.konovalovily.domain.models

import kotlinx.serialization.Serializable
import ru.konovalovily.data.models.Chapter

@Serializable
internal data class ChapterDto(
    val id: String? = null,
    val title: String? = null,
)

internal fun Chapter.mapToDomain() = ChapterDto(
    id = id,
    title = title
)
