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

        val rawText = when {
            chapter.summary.isNotBlank() -> chapter.summary
            chapter.content.isNotBlank() -> {
                chapter.content
                    .replace(Regex("\\[CODE\\][\\s\\S]*?\\[/CODE\\]"), "")
                    .trim()
                    .take(150)
            }
            else -> ""
        }

        // Korrektur: Zeilenumbrüche und Markdown-Formatierung
        var processed = rawText.replace("\n", "<br>")
        
        // **Fett** -> <b>Fett</b>
        processed = processed.replace(Regex("\\*\\*(.*?)\\*\\*"), "<b>$1</b>")
        // *Kursiv* -> <i>Kursiv</i>
        processed = processed.replace(Regex("\\*(.*?)\\*"), "<i>$1</i>")

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
