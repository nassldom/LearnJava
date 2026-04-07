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
            // Regex angepasst: Erlaubt optional führende HTML-Tags (z.B. <b>Kapitel 8...)
            if (trimmed.matches(Regex(".*Kapitel \\d+.*"))) {
                if (currentTitle.isNotEmpty()) {
                    chapters.add(
                        Chapter(currentNumber, currentTitle, "", currentContent.toString().trim())
                    )
                    currentContent.clear()
                }
                
                // Extrahiere Nummer aus dem Text (Kapitel \d+)
                val matchResult = Regex("Kapitel (\\d+)").find(trimmed)
                currentNumber = matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 0
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
