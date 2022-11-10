package ru.konovalovily.domain.models

import kotlinx.serialization.Serializable
import ru.konovalovily.data.models.Manga

@Serializable
internal data class UpdatedMangaDto(
    val id: String,
    val title: String,
    val imageUrl: String,
    val link: String,
)

internal fun Manga.mapLastUpdatedManga(): UpdatedMangaDto = UpdatedMangaDto(
    id = id.orEmpty(),
    title = title.orEmpty(),
    imageUrl = image.orEmpty(),
    link = link.orEmpty()
)
