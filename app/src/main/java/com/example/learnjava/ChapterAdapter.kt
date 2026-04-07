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
        holder.title.text = chapter.title

        // Zeige summary wenn vorhanden, sonst ersten Satz aus content
        val rawText = when {
            chapter.summary.isNotBlank() -> chapter.summary
            chapter.content.isNotBlank() -> {
                chapter.content
                    .replace(Regex("\\[CODE\\][\\s\\S]*?\\[/CODE\\]"), "")
                    .trim()
                    .lines()
                    .firstOrNull { it.isNotBlank() }
                    ?.take(120)
                    ?: ""
            }
            else -> ""
        }

        // Markdown-artige Formate in HTML umwandeln (für Vorschau)
        var processed = rawText
        while (processed.contains("**")) {
            processed = processed.replaceFirst("**", "<b>").replaceFirst("**", "</b>")
        }
        processed = processed.replace("<code>", "<font color='#4F8EF7'><i>").replace("</code>", "</i></font>")

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
