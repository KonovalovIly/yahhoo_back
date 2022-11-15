package ru.konovalovily.domain.models

import kotlinx.serialization.Serializable
import ru.konovalovily.data.models.Manga

@Serializable
internal data class MangaDto(
    val id: String? = null,
    val title: String? = null,
    val image: String? = null,
    val category: List<CategoryDto> = listOf(),
    val status: String? = null,
    val genre: String? = null,
    val anotherTitle: String? = null,
    val link: String? = null,
    val author: String? = null,
    val drawer: String? = null,
    val views: String? = null,
    val description: String? = null,
    val translator: String? = null,
    val chapters: List<ChapterDto> = listOf(),
)

internal fun Manga.mapToDomain() = MangaDto(
    id = id,
    title = title,
    image = image,
    category = category.map { it.mapToDomain() },
    status = status,
    genre = genre,
    anotherTitle = anotherTitle,
    link = link,
    author = author,
    drawer = drawer,
    views = views,
    description = description,
    translator = translator,
    chapters = chapters.map { it.mapToDomain() }
)
