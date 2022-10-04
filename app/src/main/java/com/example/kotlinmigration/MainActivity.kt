package com.example.kotlinmigration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmigration.models.API.PostsJsonItem
import com.example.kotlinmigration.models.API.ServiceAPI
import com.example.kotlinmigration.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


//const val BASE_URL = "https://jsonplaceholder.typicode.com/"

class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModels()
    val myProgress : ProgressBar = findViewById(R.id.progress)
    val recyclerView: RecyclerView = findViewById(R.id.recyclerView)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myButton: Button = findViewById(R.id.button)
     //   val myProgress : ProgressBar = findViewById(R.id.progress)
        val myTitle : TextView = findViewById(R.id.mainTitle)
        val myImg : ImageView = findViewById(R.id.img)
        val myFooter : TextView = findViewById(R.id.developed)
        
        recyclerView.layoutManager = LinearLayoutManager(this)

        myProgress.visibility = View.INVISIBLE

        //Button click runs API call below
        myButton.setOnClickListener {
            myButton.visibility = View.INVISIBLE
            myProgress.visibility = View.VISIBLE
            myTitle.visibility = View.INVISIBLE
            myImg.visibility = View.INVISIBLE
            myFooter.visibility = View.INVISIBLE

            viewModel.initRetrofit()
        }
    }

    fun observeRetrofitState()
    {
        lifecycleScope.launch {
            viewModel.retrofitState.observe(this@MainActivity){
                when(it)
                {
                    MainViewModel.RetrofitStates.IDLE -> {

                    }
                    MainViewModel.RetrofitStates.RUNNING -> {

                    }
                    MainViewModel.RetrofitStates.SUCCESSFUL -> {
                        if (viewModel.retrofitResponse != null)
                        {
                            myProgress.visibility = View.INVISIBLE
                            recyclerView.adapter = MyAdapter(viewModel.retrofitResponse!!)
                        }
                    }
                    MainViewModel.RetrofitStates.FAILED -> {
                        Toast.makeText(applicationContext, "Failure", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}