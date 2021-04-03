package com.example.pronotebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.graphics.toColor
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.noteandroidlib.notebookProcessor.NoteManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.note_container.view.*
import kotlinx.android.synthetic.main.note_description_container.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NoteManager.addSection("Some stuff", resources.getColor(R.color.defaultBlue))
        NoteManager.addSection("Study", resources.getColor(R.color.defaultGreen))
        NoteManager.addSection("Memology", resources.getColor(R.color.defaultDarkGreen))
        NoteManager.addSection("Store", resources.getColor(R.color.defaultLightBlue))
        NoteManager.addSection("Business", resources.getColor(R.color.defaultRed))
        NoteManager.addSection("Outdoor activities", resources.getColor(R.color.defaultOrange))
        NoteManager.addNote(1, "Мемасики")
        NoteManager.addNote(1, "Мемосики")
        NoteManager.addNote(1, "Мемы")
        NoteManager.addNote(2, "Лол")
        NoteManager.addNote(2, "Мемасики")
        view_pager.adapter = MyAdapter()
    }

    class MyAdapter : RecyclerView.Adapter<PagerVH>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
            PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.note_container, parent, false))

        override fun getItemCount(): Int = NoteManager.sectionsAmount

        override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
            notes.removeAllViews()
            while (notes.childCount < NoteManager.getNotesAmount(position)) {
                val noteDescription = LayoutInflater.from(context).inflate(R.layout.note_description_container, null)
                notes.addView(noteDescription)
            }
            for (i in 0 until notes.childCount) {
                notes.getChildAt(i).findViewById<CardView>(R.id.main_description_container).setCardBackgroundColor(NoteManager.getSectionColor(position))
            }
            title.setTextColor(NoteManager.getSectionColor(position))
            title.text = NoteManager.getSectionName(position)

        }
    }

    class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun createSection(view: View) {
        NoteManager.addSection("New Section", resources.getColor(R.color.defaultBlue))
        val activeItem = view_pager.currentItem
        view_pager.adapter = MyAdapter()
        view_pager.currentItem = activeItem
    }

    fun deleteSection(view: View) {
        NoteManager.deleteSection(view_pager.currentItem)
        view_pager.adapter = MyAdapter()
    }

}
