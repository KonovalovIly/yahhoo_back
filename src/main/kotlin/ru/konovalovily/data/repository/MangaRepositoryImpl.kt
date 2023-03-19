package ru.konovalovily.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import ru.konovalovily.data.api.MangaApi
import ru.konovalovily.domain.models.*
import ru.konovalovily.domain.models.MangaDto
import ru.konovalovily.domain.MangaRepository
import ru.konovalovily.domain.models.UpdatedMangaDto
import ru.konovalovily.domain.models.mapLastUpdatedManga

internal class MangaRepositoryImpl(private val mangaApi: MangaApi) : MangaRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override suspend fun lastUpdatedMangas(): List<UpdatedMangaDto> = with(Dispatchers.IO) {
        mangaApi.lastUpdated().map { it.mapLastUpdatedManga() }
    }

    override suspend fun findMangasByName(name: String): List<UpdatedMangaDto> = with(Dispatchers.IO) {
        mangaApi.findMangasByName(name).map { it.mapLastUpdatedManga() }
    }

    override suspend fun getMangaById(id: String): MangaDto = with(Dispatchers.IO) {
        mangaApi.getMangaById(id).mapToDomain()
    }

    override suspend fun getMangaChapter(mangaId: String?, chapterId: String?): List<String> = with(Dispatchers.IO) {
        mangaApi.getMangaChapterByLink(mangaId, chapterId)
    }

    override suspend fun getMangasByCategoryId(categoryId: String, page: Int): SearchResponseDto =
        with(Dispatchers.IO) {
            mangaApi.getMangasByCategoryId(categoryId, page).mapToDomain()
        }

    override suspend fun getMangaByPopularity(page: Int): SearchResponseDto = with(Dispatchers.IO) {
        mangaApi.getMangaByPopularity(page).mapToDomain()
    }

    override suspend fun getSubscribedCountChapters(data: List<SubscribeRequestDto>): List<SubscribeResponseDto> =
        with(Dispatchers.IO) {
            data.map { coroutineScope.async { mangaApi.getSubscribedCountChapters(it) } }.awaitAll()
        }
}