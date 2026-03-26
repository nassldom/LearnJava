package com.example.learnjava

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChapterDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter_detail)

        val title   = intent.getStringExtra("chapterTitle") ?: ""
        val content = intent.getStringExtra("chapterContent") ?: ""

        findViewById<TextView>(R.id.tvDetailTitle).text = title

        buildContent(content)
    }

    private fun buildContent(content: String) {
        val layout = findViewById<LinearLayout>(R.id.contentLayout)
        layout.removeAllViews()

        val parts = content.split("[CODE]", "[/CODE]")

        parts.forEachIndexed { index, part ->
            if (part.isBlank()) return@forEachIndexed

            val tv = TextView(this)
            tv.text = part.trim()

            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            if (index % 2 == 1) {
                // Code-Block
                tv.typeface = Typeface.MONOSPACE
                tv.textSize = 12f
                tv.setTextColor(Color.parseColor("#DDDDDD"))
                tv.setBackgroundColor(Color.parseColor("#1E1E1E"))
                tv.setPadding(24, 20, 24, 20)
                lp.setMargins(0, 12, 0, 12)
            } else {
                // Normaler Text
                tv.textSize = 15f
                tv.setTextColor(Color.parseColor("#333333"))
                tv.setPadding(0, 8, 0, 8)
                lp.setMargins(0, 4, 0, 4)
            }

            layout.addView(tv, lp)
        }
    }
}