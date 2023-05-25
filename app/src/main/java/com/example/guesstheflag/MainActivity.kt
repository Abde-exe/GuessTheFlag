package com.example.guesstheflag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.guesstheflag.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        replaceFragment(Home())

        _binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId) {
                R.id.home -> replaceFragment(Home())
                R.id.about -> replaceFragment(About())
                R.id.list -> replaceFragment(List())

                else -> {

                }
            }

            true

        }
    }

    private fun replaceFragment(fragment: Fragment) {
        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.FrameLayout, fragment)
        fragmentTransaction.commit()
    }
}