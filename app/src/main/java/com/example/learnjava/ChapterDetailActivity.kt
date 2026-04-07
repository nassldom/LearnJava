package com.example.learnjava

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChapterDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter_detail)

        val title = intent.getStringExtra("chapterTitle") ?: ""
        val content = intent.getStringExtra("chapterContent") ?: ""

        findViewById<TextView>(R.id.tvDetailTitle).text = title
        buildContent(content)
    }

    private fun buildContent(content: String) {
        val layout = findViewById<LinearLayout>(R.id.contentLayout)
        layout.removeAllViews()

        // Inhalte werden durch [CODE]...[/CODE] für Code-Blöcke getrennt.
        val parts = content.split("[CODE]", "[/CODE]")

        parts.forEachIndexed { index, part ->
            if (part.isBlank()) return@forEachIndexed

            val tv = TextView(this)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            if (index % 2 == 1) {
                // ── Code-Block ──
                tv.text = part.trim()
                tv.typeface = Typeface.MONOSPACE
                tv.textSize = 13f
                tv.setTextColor(Color.parseColor("#E8EAF6"))
                tv.setBackgroundColor(Color.parseColor("#1A1A2E"))
                tv.setPadding(28, 24, 28, 24)
                lp.setMargins(0, 16, 0, 16)
            } else {
                // ── Normaler Text / HTML ──
                var processed = part.trim()
                
                // Unterstützung für **Markdown Bold** falls in Textdateien vorhanden
                while (processed.contains("**")) {
                    processed = processed.replaceFirst("**", "<b>").replaceFirst("**", "</b>")
                }
                
                // Unterstützung für <code> (falls als Tag genutzt)
                processed = processed.replace("<code>", "<font color='#4F8EF7'><i>").replace("</code>", "</i></font>")

                // Konvertiere einfache Zeilenumbrüche in <br>, damit sie von Html.fromHtml nicht ignoriert werden
                processed = processed.replace("
", "<br>")

                val htmlText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(processed, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    @Suppress("DEPRECATION")
                    Html.fromHtml(processed)
                }
                
                tv.text = htmlText
                tv.textSize = 15f
                tv.setTextColor(Color.parseColor("#1A1A2E"))
                tv.setPadding(0, 8, 0, 8)
                lp.setMargins(0, 4, 0, 4)
            }
            layout.addView(tv, lp)
        }
    }
}
