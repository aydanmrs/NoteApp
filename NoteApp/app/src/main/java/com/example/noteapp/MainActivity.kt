package com.example.noteapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private var databaseNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }


        binding.fab.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("info", "new")
//            Toast.makeText(this, databaseNotes.size.plus(1).toString(), Toast.LENGTH_SHORT).show()
            intent.putExtra("noteId", "${databaseNotes.size + 1}")
            startActivity(intent)
        }

        val staggeredGrid = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerNotes.layoutManager = staggeredGrid
        staggeredGrid.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        noteAdapter = NoteAdapter(databaseNotes)
        noteAdapter.notifyItemInserted(databaseNotes.size - 1)
        binding.recyclerNotes.adapter = noteAdapter


        binding.recyclerdate.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val dateAdapter = DateAdapter(Objects().datalist())
        binding.recyclerdate.adapter = dateAdapter


        binding.recyclerCatgeories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val categoryAdapter = CategoryAdapter(Objects().categorylist())
        binding.recyclerCatgeories.adapter = categoryAdapter

    }


    private fun getDatabaseNotes() {
        try {
            databaseNotes.clear()
            val bgColors = listOf(
                R.color.Red,
                R.color.Green,
                R.color.Blue,
                R.color.Yellow,
                R.color.Pink,
                R.color.Purple,
                R.color.Orange
            ).shuffled().toMutableList()

            val database = this.openOrCreateDatabase("Notes", MODE_PRIVATE, null)
            val cursor = database.rawQuery("SELECT * FROM Notes", null)
            val idIx = cursor.getColumnIndex("id")
            val titleIx = cursor.getColumnIndex("title")
            val noteIx = cursor.getColumnIndex("detail")

            while (cursor.moveToNext()) {
                val id = cursor.getInt(idIx).toString()
                val title = cursor.getString(titleIx).toString()
                val note = cursor.getString(noteIx).toString()

                if (bgColors.isEmpty()) {
                    bgColors.addAll(
                        listOf(
                            R.color.Red,
                            R.color.Green,
                            R.color.Blue,
                            R.color.Yellow,
                            R.color.Pink,
                            R.color.Purple,
                            R.color.Orange
                        ).shuffled()
                    )
                }
                val bgColor = bgColors.removeAt(0)
                databaseNotes.add(Note(id, title, note, bgColor))
            }
            cursor.close()

        } catch (e: Exception) {
//            Toast.makeText(this, "Database not found", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        getDatabaseNotes()
        noteAdapter.notifyDataSetChanged()
    }

}

