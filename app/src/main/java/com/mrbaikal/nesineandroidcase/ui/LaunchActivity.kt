package com.mrbaikal.nesineandroidcase.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mrbaikal.nesineandroidcase.databinding.ActivityLaunchBinding
import com.mrbaikal.nesineandroidcase.ui.compose.MainComposeActivity
import com.mrbaikal.nesineandroidcase.ui.views.MainViewActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {

    private val binding: ActivityLaunchBinding by lazy {
        ActivityLaunchBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupLayout()
    }

    private fun setupLayout() {
        binding.buttonAndroidViews.setOnClickListener {
            val intent = Intent(this, MainViewActivity::class.java)
            startActivity(intent)
        }
        binding.buttonCompose.setOnClickListener {
            val intent = Intent(this, MainComposeActivity::class.java)
            startActivity(intent)
        }
    }
}