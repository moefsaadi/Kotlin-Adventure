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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModels()
    private var myProgress : ProgressBar? = null
    private var recyclerView : RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myButton: Button = findViewById(R.id.button)
        val myTitle : TextView = findViewById(R.id.mainTitle)
        val myImg : ImageView = findViewById(R.id.img)
        val myFooter : TextView = findViewById(R.id.developed)

        myProgress = findViewById(R.id.progress)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView?.layoutManager = LinearLayoutManager(this)
        myProgress?.visibility = View.INVISIBLE

        //Button click runs API call below
        myButton.setOnClickListener {
            myButton.visibility = View.INVISIBLE
            myProgress?.visibility = View.VISIBLE
            myTitle.visibility = View.INVISIBLE
            myImg.visibility = View.INVISIBLE
            myFooter.visibility = View.INVISIBLE

            viewModel.initRetrofit()
            observeRetrofitState()
        }
    }

    private fun observeRetrofitState() {
        lifecycleScope.launch {
            viewModel.retrofitState.collect{
                when(it)
                {
                    MainViewModel.RetrofitEvent.Idle -> {}
                    MainViewModel.RetrofitEvent.Running -> {}
                    is MainViewModel.RetrofitEvent.Successful -> {
                        if(it.response != null)
                        {
                            myProgress?.visibility = View.INVISIBLE
                            recyclerView?.adapter = MyAdapter(it.response)
                        }
                    }
                    is MainViewModel.RetrofitEvent.Failed -> {
                        val text = "Failure: ${it.msg}"
                        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}