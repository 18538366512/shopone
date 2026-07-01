package com.xby.shop624.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.xby.shop624.databinding.ActivityLoginBinding
import com.xby.shop624.Shop624App
import com.xby.shop624.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val phone = binding.etPhone.text?.toString().orEmpty()
            val password = binding.etPassword.text?.toString().orEmpty()
            binding.progressBar.visibility = View.VISIBLE
            viewModel.login(phone, password)
        }

        binding.btnRegister.setOnClickListener {
            val phone = binding.etPhone.text?.toString().orEmpty()
            val password = binding.etPassword.text?.toString().orEmpty()
            binding.progressBar.visibility = View.VISIBLE
            viewModel.register(phone, password)
        }

        viewModel.loginResult.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            result.onSuccess {
                (application as Shop624App).sessionManager.saveLogin(it.phone)
                Toast.makeText(this, "登录成功，欢迎 ${it.nickname}", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.onFailure {
                Toast.makeText(this, it.message ?: "登录失败", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.registerResult.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            result.onSuccess {
                Toast.makeText(this, "注册成功，请登录", Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(this, it.message ?: "注册失败", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
