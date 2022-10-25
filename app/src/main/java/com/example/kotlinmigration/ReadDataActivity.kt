package com.example.kotlinmigration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmigration.databinding.SinglePostBinding

class ReadDataActivity : AppCompatActivity() {

    private lateinit var binding: SinglePostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}