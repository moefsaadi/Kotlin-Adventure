package com.example.kotlinmigration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmigration.API.PostsJsonItem
import com.example.kotlinmigration.API.ServiceAPI
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.create

const val BASE_URL = "https://jsonplaceholder.typicode.com/"

class MainActivity : AppCompatActivity() {

    private var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory()//TODO implement gson mf
            .build()


        val serviceAPI = retrofit.create(ServiceAPI::class.java)
        val call = serviceAPI.getPosts()







    }
}