package ru.konovalovily.domain.models

import kotlinx.serialization.Serializable
import ru.konovalovily.data.models.Category

@Serializable
internal data class CategoryDto(
    val id: String? = null,
    val name: String? = null
)

internal fun Category.mapToDomain() = CategoryDto(
    id = id,
    name = name
)