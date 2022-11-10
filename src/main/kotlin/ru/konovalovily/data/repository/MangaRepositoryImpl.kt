package ru.konovalovily.data.repository

import ru.konovalovily.data.api.MangaApi
import ru.konovalovily.domain.models.*
import ru.konovalovily.domain.models.MangaDto
import ru.konovalovily.domain.MangaRepository
import ru.konovalovily.domain.models.UpdatedMangaDto
import ru.konovalovily.domain.models.mapLastUpdatedManga

internal class MangaRepositoryImpl(private val mangaApi: MangaApi) : MangaRepository {

    override suspend fun lastUpdatedMangas(): List<UpdatedMangaDto> = mangaApi.lastUpdated().map { it.mapLastUpdatedManga() }

    override suspend fun findMangasByName(name: String): List<UpdatedMangaDto> = mangaApi.findMangasByName(name).map { it.mapLastUpdatedManga() }

    override suspend fun getMangaById(id: String): MangaDto = mangaApi.getMangaById(id).mapToDomain()

    override suspend fun getMangaChapter(mangaId: String?, chapterId: String?): List<String> = mangaApi.getMangaChapterByLink(mangaId, chapterId)

    override suspend fun getMangasByCategoryId(categoryId: String, page: Int): SearchResponseDto = mangaApi.getMangasByCategoryId(categoryId, page).mapToDomain()

    override suspend fun getMangaByPopularity(page: Int): SearchResponseDto = mangaApi.getMangaByPopularity(page).mapToDomain()
}