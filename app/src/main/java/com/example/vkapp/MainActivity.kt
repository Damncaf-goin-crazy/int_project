package com.example.vkapp


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vkapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
    }

}