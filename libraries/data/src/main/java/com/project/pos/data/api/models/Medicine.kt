package com.project.pos.data.api.models

import java.time.LocalTime

data class Medicine(
    val id: String? = null,
    val name: String,
    val time: LocalTime,
    val createdAt: Long,
)