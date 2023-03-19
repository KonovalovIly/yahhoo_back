package ru.konovalovily.data.api

import ru.konovalovily.data.models.Manga
import ru.konovalovily.data.models.SearchResponce
import ru.konovalovily.domain.models.SubscribeRequestDto
import ru.konovalovily.domain.models.SubscribeResponseDto

internal interface MangaApi {

    /**
     * Получить последние обновленные манги
     */
    suspend fun lastUpdated(): List<Manga>

    /**
     * Поиск манги по названию
     *
     * @param name название
     */
    suspend fun findMangasByName(name: String): List<Manga>

    /**
     * Поиск манги по id
     *
     * @param id идентификатор
     */
    suspend fun getMangaById(id: String): Manga

    /**
     * Получить главу манги
     *
     * @param mangaId идентификатор манги
     * @param chapterId идентификатор главы
     */
    suspend fun getMangaChapterByLink(mangaId: String?, chapterId: String?): List<String>

    /**
     * Поиск манги по идентификатору категории
     *
     * @param categoryId идентификатор категории
     * @param page страница
     */
    suspend fun getMangasByCategoryId(categoryId: String, page: Int): SearchResponce

    /**
     * Получить список манги по популярности
     */
    suspend fun getMangaByPopularity(page: Int): SearchResponce


    suspend fun getSubscribedCountChapters(data: SubscribeRequestDto): SubscribeResponseDto
}