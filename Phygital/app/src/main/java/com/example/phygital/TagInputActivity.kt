package com.example.phygital

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TagInputActivity : AppCompatActivity() {

    private lateinit var layoutLinks: LinearLayout
    private lateinit var layoutImages: LinearLayout
    private lateinit var btnAddLink: Button
    private lateinit var btnAddImage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_input)

        layoutLinks = findViewById(R.id.linkContainer)
        layoutImages = findViewById(R.id.imageContainer)
        btnAddLink = findViewById(R.id.btnAddLink)
        btnAddImage = findViewById(R.id.btnAddImage)

        btnAddLink.setOnClickListener {
            addLinkRow()
        }

        btnAddImage.setOnClickListener {
            addImageRow()
        }
    }

    private fun addLinkRow() {
        val inflater = layoutInflater
        val row = inflater.inflate(R.layout.link_row, null)

        // Setup delete button
        val btnDelete = row.findViewById<ImageButton>(R.id.btnDeleteLink)
        btnDelete.setOnClickListener {
            layoutLinks.removeView(row)
        }

        // Setup edit button (optional for now)

        layoutLinks.addView(row)
    }

    private fun addImageRow() {
        val inflater = layoutInflater
        val row = inflater.inflate(R.layout.image_row, null)

        val btnDelete = row.findViewById<ImageButton>(R.id.btnDeleteImage)
        btnDelete.setOnClickListener {
            layoutImages.removeView(row)
        }

        // Setup edit button (optional for now)

        layoutImages.addView(row)
    }
}
