package com.example.registerlogin

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var etUserName: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button
    private lateinit var sharedPref: SharedPreferences
    private lateinit var btnTest: Button
    private lateinit var btnClear: Button
    private lateinit var tvDebugTest: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        etUserName = findViewById(R.id.etUserName)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)
        sharedPref = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        btnTest = findViewById(R.id.btnTest)
        btnClear = findViewById(R.id.btnClear)
        tvDebugTest = findViewById(R.id.tvDebugTest)

        fun clearFields(){
            etUserName.text.clear()
            etPassword.text.clear()
        }
        val editor = sharedPref.edit()

        btnClear.setOnClickListener {
            editor.clear()
            editor.apply()
        }

        btnTest.setOnClickListener {
            val allUsers = sharedPref.all
            tvDebugTest.setText("All Users: $allUsers")
        }
        btnRegister.setOnClickListener {
            if(sharedPref.getString("user_${etUserName.text.toString()}", null) != null){
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
            }
            else{
                    registerUser(editor, etUserName.text.toString(), etPassword.text.toString())
                    Toast.makeText(this, "User Registered Successfully ", Toast.LENGTH_SHORT).show()
                    clearFields()
            }
        }

        btnLogin.setOnClickListener {
            val userName = etUserName.text.toString()
            val password = etPassword.text.toString()
            val registeredPassword = sharedPref.getString("user_${userName}", null)
            if (registeredPassword == null){
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }else if (password == registeredPassword){
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                clearFields()
            }else{
                Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
        private fun registerUser(editor: SharedPreferences.Editor, userName: String, password: String) {
            if (userName.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return
            }
            editor.putString("user_${userName}", password)
            editor.apply()
        }
}