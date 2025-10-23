package com.example

import java.io.File

object Bootstrap {

	fun ensureDataDir() {
		val d = File("./data")
		if (!d.exists()) d.mkdirs()
	}

	init {
		ensureDataDir()
	}
}