package com.example.phygital

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var ibScan: ImageButton
    private lateinit var ibCreateTag: ImageButton
    private lateinit var etTag: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(TagApiService::class.java)

        etTag = findViewById(R.id.etTag)
        ibScan = findViewById(R.id.ibScan)
        ibCreateTag = findViewById(R.id.ibCreateTag)

        ibScan.setOnClickListener {
            Toast.makeText(this,"You Clicked Scan", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch {
                println("response")
                val response = api.getTag(etTag.text.toString())
                if(response.isSuccessful){
                    val tag = response.body()
                    if(tag!=null){

                        println(tag.author)
                        println(tag.tag)
                        println(tag.notes)

                        tag.links?.forEach {
                            println("Link URL: ${it.url}")
                            println("Link Description: ${it.description}")
                        }

                        tag.images?.forEach {
                            println("Image URL: ${it.url}")
                            println("Image Description: ${it.description}")
                        }

                    }
                    else{
                        println("Tag not found,response code ${response.code()}")
                    }
                }
                if(response.message()=="Bad Request"){
                    println("Couldn't Find Tag")
                }
//                println(response.message())
            }
        }

//        ibScan.setOnClickListener{
//            Toast.makeText(this,"You Clicked Scan", Toast.LENGTH_SHORT).show()
//        }

        ibCreateTag.setOnClickListener {
            Toast.makeText(this,"You Clicked Create Tag", Toast.LENGTH_SHORT).show()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}