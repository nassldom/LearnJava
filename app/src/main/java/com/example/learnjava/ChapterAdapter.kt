package com.example.learnjava

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChapterAdapter(
    private val chapters: List<Chapter>,
    private val onClick: (Chapter) -> Unit
) : RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitle)
        val summary: TextView = view.findViewById(R.id.tvSummary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapter = chapters[position]
        holder.title.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(chapter.title, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(chapter.title)
        }

        // Vorschautext generieren
        val rawText = when {
            chapter.summary.isNotBlank() -> chapter.summary
            chapter.content.isNotBlank() -> {
                chapter.content
                    .replace(Regex("\\[CODE\\][\\s\\S]*?\\[/CODE\\]"), "")
                    .trim()
                    .take(150) // Nimm die ersten 150 Zeichen für die Vorschau
            }
            else -> ""
        }

        var processed = rawText.replace("
", " ") // Zeilenumbrüche in Leerzeichen für die einzeilige/kurze Vorschau
        
        // Formate in HTML rendern
        while (processed.contains("**")) {
            processed = processed.replaceFirst("**", "<b>").replaceFirst("**", "</b>")
        }

        val htmlText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(processed, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(processed)
        }

        holder.summary.text = htmlText
        holder.itemView.setOnClickListener { onClick(chapter) }
    }

    override fun getItemCount() = chapters.size
}
