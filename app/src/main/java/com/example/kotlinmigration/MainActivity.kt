package com.example.kotlinmigration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmigration.API.ServiceAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://jsonplaceholder.typicode.com/"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServiceAPI::class.java)


        //might not need this stuff down here after implementing recycler view
        GlobalScope.launch(Dispatchers.IO){
            val response = retrofit.getPosts().awaitResponse()
            if(response.isSuccessful){
                val postList = response.body()
                val adapter = MyAdapter


    }

//    fun getData(){
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ServiceAPI::class.java)
//
//
//        //might not need this stuff down here after implementing recycler view
//        GlobalScope.launch(Dispatchers.IO){
//            val response = retrofit.getPosts().awaitResponse()
//            if(response.isSuccessful){
//                val postList = response.body()
//                val adapter = RecyclerViewPostAdapter(postList, MainActivity.this)
//
//
//
//            }
//        }
//    }
}