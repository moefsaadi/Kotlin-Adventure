package com.example.kotlinmigration

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
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
    var light = AppCompatDelegate.MODE_NIGHT_NO
    var dark = AppCompatDelegate.MODE_NIGHT_YES
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp : SharedPreferences = App.applicationContext().getSharedPreferences("MySharedPref", MODE_PRIVATE)

         //val theme = sp.getInt("light",dark)
        val theme = sp.getInt("dark",light)

        if(theme == 1){
        AppCompatDelegate.setDefaultNightMode(theme)}
        else{
            AppCompatDelegate.setDefaultNightMode(light)
        }





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



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

      var editor =  App.sharedPreferences.edit()

        when(item.itemId){
            R.id.menuRead ->
            {
                Toast.makeText(this,"Pulling data from Room",Toast.LENGTH_SHORT).show()
            }

            R.id.menuDelete ->
            {
                Toast.makeText(this,"Deleting data from Room",Toast.LENGTH_SHORT).show()
                viewModel.deleteData()
            }
            R.id.lightTheme ->
            {
                Toast.makeText(this,"Switching to Light Theme!",Toast.LENGTH_SHORT).show()
                AppCompatDelegate.setDefaultNightMode(light)

                editor.putInt("light", light)
                editor.apply()

            }

            R.id.darkTheme ->
            {
                Toast.makeText(this,"Switching to Dark Theme!",Toast.LENGTH_SHORT).show()
                AppCompatDelegate.setDefaultNightMode(dark)

                editor.putInt("dark", dark)
                editor.apply()

            }

        }
//        editor.commit()

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

    private fun loadSharedPreferences(){


    }
}