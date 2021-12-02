package com.practice.planter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practice.planter.model.Plant
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    val BASE_URL : String = "https://leafy-app-backend.herokuapp.com/"

    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val client = OkHttpClient()
    private val plantListType = Types.newParameterizedType(List::class.java, Plant::class.java)
    private val plantListJsonAdapter : JsonAdapter<List<Plant>> = moshi.adapter(plantListType)

    private lateinit var recyclerView : RecyclerView
    private lateinit var layoutManager : RecyclerView.LayoutManager
    private lateinit var adapter : RecyclerView.Adapter<*>
    private lateinit var addButton : ImageView
    private lateinit var testText: TextView

    private val plants: MutableList<Plant> = mutableListOf()
//    private val plantList = mutableListOf<Plant>(
//        Plant(1, 1, 1234, "Cactus", 123456, Date(), Date(), "https://images.unsplash.com/photo-1555051932-24675e24a454?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=702&q=80"),
//        Plant(1, 1, 1234, "Monstera", 123456, Date(), Date(), "https://images.unsplash.com/photo-1508013861974-9f6347163ebe?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1752&q=80"),
//        Plant(1, 1, 1234, "Fig tree", 123456, Date(), Date(), "https://images.unsplash.com/photo-1453904300235-0f2f60b15b5d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=772&q=80"),
//        Plant(1, 1, 1234, "Bonsai", 123456, Date(), Date(), "https://images.unsplash.com/photo-1512428813834-c702c7702b78?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=774&q=80"),
//        Plant(1, 1, 1234, "Leafy boi", 123456, Date(), Date(), "https://images.unsplash.com/photo-1533038590840-1cde6e668a91?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=774&q=80"),
//
//    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref = getSharedPreferences("authPreferences", Context.MODE_PRIVATE)
        val session_token = sharedPref.getString("session_token", "blah")
        val refresh_token = sharedPref.getString("refresh_token", "blah")
        val session_expiration = sharedPref.getString("session_expiration", "blah")
        val refresh_expiration = sharedPref.getString("refresh_expiration", "blah")

        recyclerView = findViewById(R.id.allPlantsList)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        if (session_token != null) {
            Log.d("tokens", "$session_token, $session_expiration, $refresh_token, $refresh_expiration")
        }
        val requestGetPlants = Request.Builder()
            .url(BASE_URL + "plants/")
            .addHeader("Authorization", "Bearer " + session_token)
            .build()
        client.newCall(requestGetPlants).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        Log.d("request failed", it.message)
                        throw IOException(response.message)
                    }
                    val plantsResponse = response.body!!.string()
                    Log.d("plants response", plantsResponse )
                    val responseJSON = JSONObject(plantsResponse)

                    val plantList = plantListJsonAdapter.fromJson(responseJSON.get("plants").toString())
                    if (plantList != null) {
                        for (plant in plantList) {
                            plants.add(plant)
                        }
                    }
                    adapter = PlantsAdapter(plants)
                    runOnUiThread {
                        recyclerView.adapter = adapter
                    }
                }
            }

        })

        addButton = findViewById(R.id.addPlantIcon)
        addButton.setOnClickListener {
            val addIntent = Intent(this, AddPlantActivity::class.java)
            startActivity(addIntent)
        }



        testText = findViewById(R.id.testText)
        testText.text = getSharedPreferences("authPreferences", Context.MODE_PRIVATE).getString("session_token", "blah")

    }
}