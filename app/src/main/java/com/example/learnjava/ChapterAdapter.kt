package com.example.learnjava

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
        val title:   TextView = view.findViewById(R.id.tvTitle)
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
        val displayText = when {
            chapter.summary.isNotBlank() -> chapter.summary
            chapter.content.isNotBlank() -> {
                // Ersten sinnvollen Satz/Zeile aus content extrahieren (ohne [CODE]-Tags)
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
        holder.summary.text = displayText
        holder.itemView.setOnClickListener { onClick(chapter) }
    }

    override fun getItemCount() = chapters.size
}
