package com.raywenderlich.tasksapp.tools

import java.util.*

object IDGenerator {
    fun generateID(): Long {
        val timestamp = System.currentTimeMillis()
        val random = Random().nextInt(1000)
        return timestamp * 1000 + random
    }
}