package ru.konovalovily.domain.models

import kotlinx.serialization.Serializable
import ru.konovalovily.data.models.Chapter

@Serializable
internal data class ChapterDto(
    val id: String? = null,
    val downloadLink: String? = null,
    val title: String? = null,
    val date: String? = null,
    val link: String? = null,
)

internal fun Chapter.mapToDomain() = ChapterDto(
    id = id,
    downloadLink = downloadLink,
    date = date,
    link = link,
    title = title
)
