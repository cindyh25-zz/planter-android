package com.practice.planter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView : RecyclerView
    private lateinit var layoutManager : RecyclerView.LayoutManager
    private lateinit var adapter : RecyclerView.Adapter<*>
    private lateinit var addButton : ImageView
    private val plantList = mutableListOf<Plant>(
        Plant(1, 1, 1234, "Cactus", 123456, Date(), Date(), "https://images.unsplash.com/photo-1555051932-24675e24a454?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=702&q=80"),
        Plant(1, 1, 1234, "Monstera", 123456, Date(), Date(), "https://images.unsplash.com/photo-1508013861974-9f6347163ebe?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1752&q=80"),
        Plant(1, 1, 1234, "Fig tree", 123456, Date(), Date(), "https://images.unsplash.com/photo-1453904300235-0f2f60b15b5d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=772&q=80"),
        Plant(1, 1, 1234, "Bonsai", 123456, Date(), Date(), "https://images.unsplash.com/photo-1512428813834-c702c7702b78?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=774&q=80"),
        Plant(1, 1, 1234, "Leafy boi", 123456, Date(), Date(), "https://images.unsplash.com/photo-1533038590840-1cde6e668a91?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=774&q=80"),

    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.allPlantsList)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = PlantsAdapter(plantList)
        recyclerView.adapter = adapter

        addButton = findViewById(R.id.addPlantIcon)
        addButton.setOnClickListener {
            val addIntent = Intent(this, AddPlantActivity::class.java)
            startActivity(addIntent)
        }

    }
}