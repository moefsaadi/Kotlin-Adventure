package com.example.kotlinmigration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmigration.app.App
import com.example.kotlinmigration.database.dao.PostDao
import com.example.kotlinmigration.database.dto.PostDto
import com.example.kotlinmigration.databinding.ActivityMainBinding
import com.example.kotlinmigration.models.API.PostsJsonItem
import com.example.kotlinmigration.models.API.ServiceAPI
import com.example.kotlinmigration.viewmodels.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.progress.visibility = View.INVISIBLE

        //Button click runs API call below
        binding.button.setOnClickListener {
            binding.button.visibility = View.INVISIBLE
            binding.progress.visibility = View.VISIBLE
            binding.mainTitle.visibility = View.INVISIBLE
            binding.img.visibility = View.INVISIBLE
            binding.developed.visibility = View.INVISIBLE

            viewModel.makeApiCall()
            observeRetrofitState()
        }

        val actionBar = supportActionBar

        actionBar?.setDisplayShowCustomEnabled(true) //continue implementing from here
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuRead -> Toast.makeText(this,"Pulling data from Room",Toast.LENGTH_SHORT).show()
            R.id.menuDelete -> Toast.makeText(this,"Deleting data from Room",Toast.LENGTH_SHORT).show()
            R.id.menuTheme -> Toast.makeText(this,"Switching themes!",Toast.LENGTH_SHORT).show()
        }
        return true
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
                            binding.progress.visibility = View.INVISIBLE
                            binding.recyclerView.adapter = MyAdapter(it.response)

                            viewModel.insertDataToDatabase(it.response)

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