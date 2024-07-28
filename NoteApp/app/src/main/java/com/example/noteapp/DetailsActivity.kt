package com.example.noteapp


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }

        val intent = intent
        val id = intent.getStringExtra("noteId")
        var info = intent.getStringExtra("info")

        binding.save.setOnClickListener {
            loadToDatabase(info, id)
            info = "old"
            listOf(binding.title, binding.notes).forEach {
                it.clearFocus()
            }
        }

        binding.back.setOnClickListener {
            finish()
        }

        listOf(binding.title, binding.notes).forEach {
            it.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.save.visibility = View.VISIBLE
                    binding.back.visibility = View.INVISIBLE
                } else {
                    binding.save.visibility = View.INVISIBLE
                    binding.back.visibility = View.VISIBLE
                }
            }
        }

        if (info == "old") {
            try {
                val database = this.openOrCreateDatabase("Notes", MODE_PRIVATE, null)
                val cursor = database.rawQuery("SELECT * FROM Notes WHERE id =?", arrayOf(id))
                val titleIx = cursor.getColumnIndex("title")
                val noteIx = cursor.getColumnIndex("detail")
//                Toast.makeText(this, "Loaded $id", Toast.LENGTH_SHORT).show()

                while (cursor.moveToNext()) {
                    val title = cursor.getString(titleIx).toString()
                    val note = cursor.getString(noteIx).toString()
                    binding.title.setText(title)
                    binding.notes.setText(note)
                }
                cursor.close()
            } catch (e: Exception) {
//                Toast.makeText(this, "Database not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadToDatabase(info: String?, id: String?) {
        try {
            val database = this.openOrCreateDatabase("Notes", MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS Notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, detail VARCHAR)")
            val titleText = binding.title.text.toString()
            val noteText = binding.notes.text.toString()

            if (titleText.isEmpty() && noteText.isEmpty()) {
                return
            }

            if (info == "old") {
                database.execSQL(
                    "UPDATE Notes SET title =?, detail =? WHERE id =?",
                    arrayOf(titleText, noteText, id)
                )
//                Toast.makeText(this, "Updated $id", Toast.LENGTH_SHORT).show()
            } else {
                database.execSQL(
                    "INSERT INTO Notes (title, detail) VALUES (?,?)",
                    arrayOf(titleText, noteText)
                )
//                Toast.makeText(this, "Added $id", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
//            Toast.makeText(this, "Database not found", Toast.LENGTH_SHORT).show()
        }
    }
}
