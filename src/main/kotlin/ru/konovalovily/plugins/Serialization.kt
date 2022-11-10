package ru.konovalovily.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*

internal fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
