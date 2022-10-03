package com.example.kotlinmigration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmigration.API.PostsJsonItem
import com.example.kotlinmigration.API.ServiceAPI
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://jsonplaceholder.typicode.com/"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myButton: Button = findViewById(R.id.button)
        val myProgress : ProgressBar = findViewById(R.id.progress)
        val myTitle : TextView = findViewById(R.id.mainTitle)
        val myImg : ImageView = findViewById(R.id.img)
        val myFooter : TextView = findViewById(R.id.developed)


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        myProgress.visibility = View.INVISIBLE

        //Button click runs API call below
        myButton.setOnClickListener {
            myButton.visibility = View.INVISIBLE
            myProgress.visibility = View.VISIBLE
            myTitle.visibility = View.INVISIBLE
            myImg.visibility = View.INVISIBLE
            myFooter.visibility = View.INVISIBLE



            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


            val serviceApi = retrofit.create(ServiceAPI::class.java)

            val call = serviceApi.getPosts()
            call.enqueue(object : Callback<List<PostsJsonItem>> {
                override fun onResponse(
                    call: Call<List<PostsJsonItem>>,
                    response: Response<List<PostsJsonItem>>
                ) {
                    if (!response.isSuccessful) {
                        return
                    }

                    myProgress.visibility = View.INVISIBLE

                    recyclerView.adapter = MyAdapter(response.body()!!)
                }

                override fun onFailure(call: Call<List<PostsJsonItem>>, t: Throwable) {
                    Toast.makeText(applicationContext, "Failure", Toast.LENGTH_SHORT).show()
                }
            })

        }
    }

}