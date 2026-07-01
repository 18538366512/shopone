package com.xby.shop624.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.xby.shop624.databinding.ActivityWelcomeBinding
import com.xby.shop624.ui.login.LoginActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private var hasNavigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEnter.setOnClickListener {
            navigateToLogin()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToLogin()
        }, 2500)
    }

    private fun navigateToLogin() {
        if (hasNavigated) return
        hasNavigated = true
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
