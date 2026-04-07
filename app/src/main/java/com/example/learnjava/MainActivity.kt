package com.example.learnjava

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chapters = mutableListOf(
            Chapter(1,  "Part 1 – Hardware & Software",       "Theorie: Grundlagen zu Hardware und Software (kein Code)"),
            Chapter(2,  "Part 2 – Erste Java-Programme",      "Kap. 5–7: System.out.println, erste Programme (Hello.java, HelloPlanets.java, Fantasy.java)"),
            Chapter(3,  "Part 3 – Daten & Ein-/Ausgabe",      "Kap. 8–15: Variablen, Datentypen, Scanner, String-Methoden, printf, Wrapper-Klassen"),
            Chapter(4,  "Part 4 – Verzweigungen & Schleifen", "Kap. 16–25: if/else, while, for, Sentinel-Loops, Result-Loops, verschachtelte Schleifen"),
            Chapter(5,  "Part 5 – Weitere Java-Features",     "Kap. 30–35: Random, Inkrement/Dekrement, switch, ?:-Operator, do-while"),
            Chapter(6,  "Part 6 – OOP Grundlagen",            "Kap. 40–54: Klassen, Objekte, Konstruktoren, Encapsulation, Static Methods"),
            Chapter(7,  "Part 7 – Arrays",                    "Kap. 60–68: Arrays, Algorithmen (Min/Max/Suche), Array of Objects, 2D-Arrays, StringBuffer"),
            Chapter(8,  "Part 8 – File I/O",                  "Kap. 70–73: Dateien lesen/schreiben mit Scanner und PrintWriter"),
            Chapter(9,  "Part 9 – Fortgeschrittene OOP",      "Kap. 80–86: Vererbung, Abstract Classes, Interfaces, Comparable, ArrayList, BigInteger"),
            Chapter(10, "Part 10 – Rekursion",                "Kap. 90–96: Fakultät, Fibonacci, Rekursion mit Strings und Grafik, Fraktale"),
            Chapter(11, "Part 11 – Exceptions",               "Kap. 100–106: try/catch, eigene Exceptions, finally, Binary Files, File-Klasse"),
            Chapter(12, "Part 12 – Sortieralgorithmen",       "Kap. 110–112: Selection Sort, Insertion Sort, Arrays.sort(), Objekte sortieren"),
            Chapter(13, "Part 13 – JavaFX Grafik",            "Kap. 120–121: JavaFX Shapes, Linien, Polygone, Text, Emojis, Rotation, Fraktale"),
            Chapter(14, "Part 14 – Linked Lists",             "Kap. 130–133: Node, LinkedList, OrderedLinkedList, GenericNode, GenericLinkedList"),
            Chapter(-1, "Nach Begriff suchen: ", "Suche nach Inhalten in allen Teilen")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChapterAdapter(chapters) { chapter ->
            if (chapter.number == -1) {
                // Starte die Such-Aktivität
                startActivity(Intent(this, SearchActivity::class.java))
            } else {
                // Normaler Part-Aufruf
                val intent = Intent(this, PartDetailActivity::class.java)
                intent.putExtra("part_title", chapter.title)
                intent.putExtra("chapterNumber", chapter.number)
                startActivity(intent)
            }
        }
    }
}
