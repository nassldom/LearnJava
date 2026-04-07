package com.example.learnjava

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private lateinit var adapter: ChapterAdapter
    private val allParts = mutableListOf<Chapter>()
    private val filteredResults = mutableListOf<Chapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#F0F4F8"))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        val searchInput = EditText(this).apply {
            hint = "Nach Begriff suchen..."
            setPadding(40, 40, 40, 40)
            setBackgroundColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(20, 20, 20, 20)
            }
        }
        root.addView(searchInput)

        val rvResults = RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0, 1f
            )
        }
        root.addView(rvResults)
        setContentView(root)

        setupData()

        adapter = ChapterAdapter(filteredResults) { chapter ->
            val intent = Intent(this, PartDetailActivity::class.java)
            intent.putExtra("part_title", chapter.title)
            intent.putExtra("chapterNumber", chapter.number)
            startActivity(intent)
        }
        rvResults.adapter = adapter

        searchInput.addTextChangedListener { text ->
            filterContent(text.toString())
        }
    }

    private fun setupData() {
        allParts.addAll(listOf(
            Chapter(1,  "Part 1 – Hardware & Software",       "Theorie: Grundlagen zu Hardware und Software (kein Code)"),
            Chapter(2,  "Part 2 – Erste Java-Programme",      "Kap. 5–7: System.out.println..."),
            Chapter(3,  "Part 3 – Daten & Ein-/Ausgabe",      "Kap. 8–15: Variablen, Datentypen..."),
            Chapter(4,  "Part 4 – Verzweigungen & Schleifen", "Kap. 16–25: if/else..."),
            Chapter(5,  "Part 5 – Weitere Java-Features",     "Kap. 30–35: Random..."),
            Chapter(6,  "Part 6 – OOP Grundlagen",            "Kap. 40–54: Klassen..."),
            Chapter(7,  "Part 7 – Arrays",                    "Kap. 60–68: Arrays..."),
            Chapter(8,  "Part 8 – File I/O",                  "Kap. 70–73: Dateien..."),
            Chapter(9,  "Part 9 – Fortgeschrittene OOP",      "Kap. 80–86: Vererbung..."),
            Chapter(10, "Part 10 – Rekursion",                "Kap. 90–96: Fakultät..."),
            Chapter(11, "Part 11 – Exceptions",               "Kap. 100–106: try/catch..."),
            Chapter(12, "Part 12 – Sortieralgorithmen",       "Kap. 110–112: Selection Sort..."),
            Chapter(13, "Part 13 – JavaFX Grafik",            "Kap. 120–121: JavaFX..."),
            Chapter(14, "Part 14 – Linked Lists",             "Kap. 130–133: Node...")
        ))
    }

    private fun filterContent(query: String) {
        filteredResults.clear()
        if (query.length >= 2) {
            allParts.forEach { part ->
                val content = loadRawText(part.number)
                if (part.title.contains(query, true) || 
                    part.summary.contains(query, true) || 
                    content.contains(query, true)) {
                    filteredResults.add(part)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun loadRawText(partNumber: Int): String {
        val resId = resources.getIdentifier("part$partNumber", "raw", packageName)
        return if (resId != 0) resources.openRawResource(resId).bufferedReader().readText() else ""
    }
}
