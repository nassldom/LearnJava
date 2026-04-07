package com.example.learnjava

import android.os.Bundle
import android.widget.TextView
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this).apply {
            text = "Suche kommt bald..."
            gravity = Gravity.CENTER
            textSize = 20f
        }
        setContentView(tv)
        supportActionBar?.title = "Suche"
    }
}
