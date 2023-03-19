package ru.konovalovily.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.konovalovily.domain.MangaRepository
import ru.konovalovily.domain.models.SubscribeRequestDto

internal fun Application.configureRouting(mangaRepository: MangaRepository) {

    routing {
        get("/") {
            runCatching { call.respond(mangaRepository.lastUpdatedMangas()) }
                .onSuccess { call.respond(it) }
        }

        get("/search") {
            val name = call.request.queryParameters["name"].orEmpty()

            runCatching { call.respond(mangaRepository.findMangasByName(name)) }
                .onSuccess { call.respond(it) }
        }

        get("/manga/{id}") {
            val id = call.parameters["id"].orEmpty()

            runCatching { mangaRepository.getMangaById(id) }
                .onSuccess { call.respond(it) }
        }

        get("/manga/{mangaId}/{chapterId}") {
            val mangaId = call.parameters["mangaId"].orEmpty()
            val chapterId = call.parameters["chapterId"].orEmpty()

            runCatching { call.respond(mangaRepository.getMangaChapter(mangaId, chapterId)) }
                .onSuccess { call.respond(it) }
        }

        get("/mangas/{category}/{page}") {
            val category = call.parameters["category"].orEmpty()
            val page = call.parameters["page"]?.toInt() ?: 1

            runCatching { call.respond(mangaRepository.getMangasByCategoryId(category, page)) }
                .onSuccess { call.respond(it) }
        }

        get("/mangas/{page}") {
            val page = call.parameters["page"]?.toInt() ?: 1

            runCatching { call.respond(mangaRepository.getMangaByPopularity(page)) }
                .onSuccess { call.respond(it) }
        }

        post("/subscribe") {
            val data = call.receive<List<SubscribeRequestDto>>()

            runCatching { mangaRepository.getSubscribedCountChapters(data) }
                .onSuccess { call.respond(it) }
        }
    }
}

/**
 * private val userAgentRandomizer = " ${Random().nextInt().absoluteValue}"
private fun headersBuilder(): Document = Jsoup
.connect("https://mangalib.me/")
.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/78.0$userAgentRandomizer")
.get()
 */
