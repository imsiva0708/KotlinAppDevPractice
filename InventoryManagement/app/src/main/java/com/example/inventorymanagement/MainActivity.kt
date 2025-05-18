package com.example.inventorymanagement

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    private lateinit var btnRefresh: Button
    private lateinit var btnAddNewData: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InventoryAdapter
    private val inventoryItems = mutableListOf<InventoryItem>()

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_item, null)

        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etQuantity = dialogView.findViewById<EditText>(R.id.etQuantity)
        val etPrice = dialogView.findViewById<EditText>(R.id.etPrice)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Item")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = etName.text.toString()
                val quantity = etQuantity.text.toString().toIntOrNull() ?: 0
                val price = etPrice.text.toString().toDoubleOrNull() ?: 0.0
                println("Name: $name, Quantity: $quantity, Price: $price")
                val url =
                    "https://script.google.com/macros/s/AKfycbz-Ya7j6PcT3tceJZD8Mfs-RMJ5CH_6raEn6oZMXttuI4HApXOAEy6Ir3nN6TGGqeCM/exec?action=create&name=$name&quantity=$quantity&price=$price"
//                println(url)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val conn = URL(url).openConnection() as HttpURLConnection
                        conn.requestMethod = "GET"
                        val responseCode = conn.responseCode
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            withContext(Dispatchers.Main) {
                                fetchInventoryFromGoogleSheet()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun handleEdit(item: InventoryItem) {
        showEditDialog(item)
    }

    private fun showEditDialog(item: InventoryItem) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_item, null)

        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etQuantity = dialogView.findViewById<EditText>(R.id.etQuantity)
        val etPrice = dialogView.findViewById<EditText>(R.id.etPrice)

        // Pre-fill with current item data
        val id = item.id
        etName.setText(item.name)
        etQuantity.setText(item.quantity.toString())
        etPrice.setText(item.price.toString())

        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Item")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newName = etName.text.toString()
                val newQuantity = etQuantity.text.toString().toIntOrNull() ?: 0
                val newPrice = etPrice.text.toString().toDoubleOrNull() ?: 0.0
//                println("New Name: $newName, New Quantity: $newQuantity, New Price: $newPrice")
                val url =
                    "https://script.google.com/macros/s/AKfycbz-Ya7j6PcT3tceJZD8Mfs-RMJ5CH_6raEn6oZMXttuI4HApXOAEy6Ir3nN6TGGqeCM/exec?action=update&id=$id&name=$newName&quantity=$newQuantity&price=$newPrice"
//                println(url)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val conn = URL(url).openConnection() as HttpURLConnection
                        conn.requestMethod = "GET"
                        val responseCode = conn.responseCode
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            withContext(Dispatchers.Main) {
                                fetchInventoryFromGoogleSheet()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun handleDelete(item: InventoryItem) {
        AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete ${item.name}?")
            .setPositiveButton("Delete") { _, _ ->
                val url =
                    "https://script.google.com/macros/s/AKfycbz-Ya7j6PcT3tceJZD8Mfs-RMJ5CH_6raEn6oZMXttuI4HApXOAEy6Ir3nN6TGGqeCM/exec?action=delete&id=${item.id}"
                println(url)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val conn = URL(url).openConnection() as HttpURLConnection
                        conn.requestMethod = "GET"
                        val responseCode = conn.responseCode
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            withContext(Dispatchers.Main) {
                                fetchInventoryFromGoogleSheet()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun fetchInventoryFromGoogleSheet() {
        val urlString =
            "https://script.google.com/macros/s/AKfycbz-Ya7j6PcT3tceJZD8Mfs-RMJ5CH_6raEn6oZMXttuI4HApXOAEy6Ir3nN6TGGqeCM/exec?action=read"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(urlString)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val response = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                reader.close()

                val inventoryList = mutableListOf<InventoryItem>()
                val jsonArray = JSONArray(response.toString())

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)

                    val id = obj.getInt("id")
                    val name = obj.getString("name")
                    val quantity = obj.getInt("quantity")
                    val price = obj.getDouble("price")

                    inventoryList.add(InventoryItem(id, name, quantity, price))
                }

                withContext(Dispatchers.Main) {
                    adapter.updateList(inventoryList)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnAddNewData = findViewById(R.id.btnAddNewData)

        btnRefresh.setOnClickListener()
        {
            fetchInventoryFromGoogleSheet()
        }

        btnAddNewData.setOnClickListener()
        {
            showAddDialog()
        }

        recyclerView = findViewById(R.id.rvList)
        adapter = InventoryAdapter(
            inventoryItems,
            onEditClick = { item -> handleEdit(item) },
            onDeleteClick = { item -> handleDelete(item) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchInventoryFromGoogleSheet()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

