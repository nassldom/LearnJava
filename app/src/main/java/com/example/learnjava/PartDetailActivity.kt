package com.example.learnjava

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class PartDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Statusbar-Farbe auf dunkelblau setzen
        window.statusBarColor = Color.parseColor("#1A1A2E")
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = false

        val partTitle = intent.getStringExtra("part_title") ?: "Part"
        val partNumber = intent.getIntExtra("chapterNumber", 1)

        val rawText  = loadRawText(partNumber)
        val chapters = ChapterParser.parse(partNumber, rawText)

        // ── Root-Layout
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#F0F4F8"))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        // ── Header (dunkelblau wie Logo) + extra Padding oben fuer Statusbar
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#1A1A2E"))
            // paddingTop 56dp: 24dp Statusbar + 32dp eigenes Padding
            setPadding(dpToPx(20), dpToPx(56), dpToPx(20), dpToPx(20))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val tvTitle = TextView(this).apply {
            text = partTitle
            textSize = 22f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.WHITE)
            gravity = Gravity.START
        }
        header.addView(tvTitle)

        // Blauer Unterstrich-Balken
        val accent = android.view.View(this).apply {
            setBackgroundColor(Color.parseColor("#4F8EF7"))
            layoutParams = LinearLayout.LayoutParams(dpToPx(48), dpToPx(3)).apply {
                topMargin = dpToPx(10)
            }
        }
        header.addView(accent)
        root.addView(header)

        // ── Scrollbarer Inhalt
        val scrollView = ScrollView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0, 1f
            )
        }

        val contentLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12))
        }

        if (chapters.isEmpty()) {
            val tv = TextView(this).apply {
                text = rawText
                textSize = 15f
                setTextColor(Color.parseColor("#1A1A2E"))
            }
            contentLayout.addView(tv)
        } else {
            for (chapter in chapters) {
                // Kapitel-Karte: weisser Hintergrund, linker blauer Balken
                val card = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    setBackgroundColor(Color.WHITE)
                    elevation = dpToPx(2).toFloat()
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply { setMargins(dpToPx(2), dpToPx(6), dpToPx(2), dpToPx(6)) }
                    isClickable = true
                    isFocusable = true
                    setOnClickListener {
                        val i = Intent(this@PartDetailActivity, ChapterDetailActivity::class.java)
                        i.putExtra("chapterNumber", chapter.number)
                        i.putExtra("chapterTitle", chapter.title)
                        i.putExtra("chapterContent", chapter.content)
                        startActivity(i)
                    }
                }

                // Linker blauer Akzentbalken
                val bar = android.view.View(this).apply {
                    setBackgroundColor(Color.parseColor("#4F8EF7"))
                    layoutParams = LinearLayout.LayoutParams(dpToPx(5),
                        LinearLayout.LayoutParams.MATCH_PARENT).apply {
                        marginEnd = dpToPx(14)
                    }
                }
                card.addView(bar)

                // Text-Container
                val textBox = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity     = Gravity.CENTER_VERTICAL
                    setPadding(0, dpToPx(14), dpToPx(14), dpToPx(14))
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }

                // Kapitel-Titel (dunkel auf weissem Hintergrund) mit HTML-Parsing
                val tvChTitle = TextView(this).apply {
                    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(chapter.title, Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        @Suppress("DEPRECATION")
                        Html.fromHtml(chapter.title)
                    }
                    textSize = 16f
                    setTypeface(null, Typeface.BOLD)
                    setTextColor(Color.parseColor("#1A1A2E"))
                    gravity = Gravity.START
                }
                textBox.addView(tvChTitle)

                // Zusammenfassung (grau, kleiner) mit HTML-Parsing
                if (chapter.summary.isNotBlank()) {
                    val tvSummary = TextView(this).apply {
                        text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(chapter.summary, Html.FROM_HTML_MODE_LEGACY)
                        } else {
                            @Suppress("DEPRECATION")
                            Html.fromHtml(chapter.summary)
                        }
                        textSize = 13f
                        setTextColor(Color.parseColor("#555555"))
                        gravity = Gravity.START
                        setPadding(0, dpToPx(4), 0, 0)
                    }
                    textBox.addView(tvSummary)
                }

                card.addView(textBox)
                contentLayout.addView(card)
            }
        }

        scrollView.addView(contentLayout)
        root.addView(scrollView)
        setContentView(root)
    }

    private fun dpToPx(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()

    private fun loadRawText(partNumber: Int): String {
        val resId = resources.getIdentifier("part$partNumber", "raw", packageName)
        return if (resId != 0)
            resources.openRawResource(resId).bufferedReader().use { it.readText() }
        else ""
    }
}
