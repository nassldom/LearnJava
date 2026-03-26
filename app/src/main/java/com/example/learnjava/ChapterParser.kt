package com.example.learnjava

data class Chapter(
    val number: Int,
    val title: String,
    val summary: String,
    val content: String = ""
)

object ChapterParser {

    fun parse(partNumber: Int, text: String): List<Chapter> {
        val chapters = mutableListOf<Chapter>()
        val lines = text.lines()
        var currentTitle = ""
        var currentNumber = 0
        val currentContent = StringBuilder()

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.matches(Regex("Kapitel \\d+.*"))) {
                if (currentTitle.isNotEmpty()) {
                    chapters.add(
                        Chapter(currentNumber, currentTitle, "", currentContent.toString().trim())
                    )
                    currentContent.clear()
                }
                val numStr = trimmed.removePrefix("Kapitel ").split(" ").firstOrNull() ?: "0"
                currentNumber = numStr.toIntOrNull() ?: 0
                currentTitle = trimmed
            } else {
                currentContent.appendLine(line)
            }
        }

        if (currentTitle.isNotEmpty()) {
            chapters.add(
                Chapter(currentNumber, currentTitle, "", currentContent.toString().trim())
            )
        }

        return chapters
    }
}