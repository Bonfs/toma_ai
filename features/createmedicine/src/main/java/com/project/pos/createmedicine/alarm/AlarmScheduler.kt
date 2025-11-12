package com.project.pos.createmedicine.alarm

import java.time.LocalTime

interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(id: Int)
}

data class AlarmItem(
    val id: Int,
    val time: LocalTime,
    val message: String
)