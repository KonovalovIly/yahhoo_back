package ru.konovalovily.domain.models

import kotlinx.serialization.Serializable
import ru.konovalovily.data.models.SearchResponce

@Serializable
internal data class SearchResponseDto(
    val mangas: List<UpdatedMangaDto>,
    val pagesMax: Int
)

internal fun SearchResponce.mapToDomain() = SearchResponseDto(
    mangas = mangas.map { it.mapLastUpdatedManga() },
    pagesMax = pagesMax
)
