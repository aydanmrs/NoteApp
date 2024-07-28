package com.example.noteapp

import android.annotation.SuppressLint
import android.content.Intent
import android.telecom.Call
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.NoteRecyclerBinding

class NoteAdapter(private val noteList: ArrayList<Note>) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    class ViewHolder(val binding: NoteRecyclerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.ViewHolder {
        val binding = NoteRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resources = holder.itemView.context.resources
        holder.binding.title.text = noteList[position].title
        holder.binding.notes.text = noteList[position].notes
        holder.binding.notesBg.background.setTint(resources.getColor(noteList[position].color))

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("noteId", noteList[position].id)
            intent.putExtra("info", "old")
            holder.itemView.context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener {
            alertDialog(holder)
            true
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    private fun alertDialog(holder: ViewHolder) {
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Delete Note")
            .setMessage("Would you like to delete this note?")
            .setPositiveButton("Yes") { _, _ ->
                deleteNote(holder)
            }
            .setNegativeButton("No", null)
            .show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteNote(holder: ViewHolder) {
        try {
            val database = holder.itemView.context.openOrCreateDatabase(
                "Notes",
                AppCompatActivity.MODE_PRIVATE,
                null
            )
            database.execSQL("CREATE TABLE IF NOT EXISTS Notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, detail VARCHAR)")
            database.execSQL("DELETE FROM Notes WHERE id = ?", arrayOf(noteList[holder.adapterPosition].id))
            database.close()
        } catch (e: Exception) {
//            Toast.makeText(holder.itemView.context, "Database not found", Toast.LENGTH_SHORT).show()
        }
        noteList.removeAt(holder.adapterPosition)
        notifyDataSetChanged()
    }

}