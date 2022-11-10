package ru.konovalovily.domain

import ru.konovalovily.domain.models.MangaDto
import ru.konovalovily.domain.models.SearchResponseDto
import ru.konovalovily.domain.models.UpdatedMangaDto

internal interface MangaRepository {

    /** Получить последние обновленные манги */
    suspend fun lastUpdatedMangas(): List<UpdatedMangaDto>

    /**
     * Поиск манги по названию
     *
     * @param name название
     */
    suspend fun findMangasByName(name: String): List<UpdatedMangaDto>

    /**
     * Поиск манги по id
     *
     * @param id идентификатор
     */
    suspend fun getMangaById(id: String): MangaDto

    /**
     * Получить главу манги
     *
     * @param mangaId идентификатор манги
     * @param chapterId идентификатор главы
     */
    suspend fun getMangaChapter(mangaId: String?, chapterId: String?): List<String>

    /**
     * Поиск манги по идентификатору категории
     *
     * @param categoryId идентификатор категории
     * @param page страница
     */
    suspend fun getMangasByCategoryId(categoryId: String, page: Int): SearchResponseDto

    /**
     * Получить список манги по популярности
     */
    suspend fun getMangaByPopularity(page: Int): SearchResponseDto
}