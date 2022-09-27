package com.example.kotlinmigration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmigration.API.PostsJsonItem
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

        var list = listOf<PostsJsonItem>()


        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServiceAPI::class.java)

        val troll = retrofit.getPosts()
        troll.enqueue(object: Callback<PostsJsonItem>{
            override fun onResponse(call: Call<PostsJsonItem>, response: Response<PostsJsonItem>) {
                if(!response.isSuccessful){ return }

                list = MyAdapter(list,applicationContext)

            }

            override fun onFailure(call: Call<PostsJsonItem>, t: Throwable) {
                Toast.makeText(applicationContext,"Failure",Toast.LENGTH_SHORT).show()

            }

        })

    }
}