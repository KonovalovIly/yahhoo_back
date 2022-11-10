package ru.konovalovily

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import ru.konovalovily.data.api.MangaApiImpl
import ru.konovalovily.data.repository.MangaRepositoryImpl
import ru.konovalovily.plugins.*

internal fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

internal fun Application.module() {
    val mangaApi = MangaApiImpl()
    val mangaRepository = MangaRepositoryImpl(mangaApi)
    configureSerialization()
    configureRouting(mangaRepository)
}