package com.example.learnjava

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PartDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val partTitle = intent.getStringExtra("part_title") ?: "Part"
        val partNumber = intent.getIntExtra("chapterNumber", 1)

        title = partTitle

        val rawText = loadRawText(partNumber)
        val chapters = ChapterParser.parse(partNumber, rawText)

        val scrollView = ScrollView(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        val header = TextView(this).apply {
            text = partTitle
            textSize = 20f
            setPadding(0, 0, 0, 24)
        }
        layout.addView(header)

        if (chapters.isEmpty()) {
            val tv = TextView(this).apply {
                text = rawText
                textSize = 15f
            }
            layout.addView(tv)
        } else {
            for (chapter in chapters) {
                val btn = Button(this).apply {
                    text = chapter.title
                    textSize = 14f
                    setOnClickListener {
                        val i = Intent(this@PartDetailActivity, ChapterDetailActivity::class.java)
                        i.putExtra("chapterNumber", chapter.number)
                        i.putExtra("chapterTitle", chapter.title)
                        i.putExtra("chapterContent", chapter.content)
                        startActivity(i)
                    }
                }
                layout.addView(btn)
            }
        }

        scrollView.addView(layout)
        setContentView(scrollView)
    }

    private fun loadRawText(partNumber: Int): String {
        val resId = resources.getIdentifier("part$partNumber", "raw", packageName)
        return if (resId != 0) {
            resources.openRawResource(resId).bufferedReader().use { it.readText() }
        } else {
            ""
        }
    }
}