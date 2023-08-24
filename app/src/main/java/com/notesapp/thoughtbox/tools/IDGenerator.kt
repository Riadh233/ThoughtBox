package com.notesapp.thoughtbox.tools

import java.util.*

object IDGenerator {
    fun generateID(): Long {
        val timestamp = System.currentTimeMillis()
        val random = Random().nextInt(1000)
        return timestamp * 1000 + random
    }
}