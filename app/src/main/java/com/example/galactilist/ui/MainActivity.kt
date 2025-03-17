package com.example.galactilist.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.galactilist.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Set the title of the toolbar
        binding.toolbarLayout.toolbarTitle.text = "Welcome to Galactilist!"

        // Hide the back button on the toolbar
        binding.toolbarLayout.toolbarBackButton.visibility = View.GONE

        // Set a click listener on the back button to handle back navigation
        binding.toolbarLayout.toolbarBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}