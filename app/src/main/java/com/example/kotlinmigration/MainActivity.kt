package com.example.kotlinmigration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinmigration.app.App
import com.example.kotlinmigration.databinding.ActivityMainBinding
import com.example.kotlinmigration.viewmodels.MainViewModel
import kotlinx.coroutines.launch

const val KEY_NIGHT_MODE = "nightMode"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()
    private val light = AppCompatDelegate.MODE_NIGHT_NO
    private val dark = AppCompatDelegate.MODE_NIGHT_YES

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadSharedPreferences()

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

        val readTxt = "Pulling data from Room"
        val deleteTxt = "Deleting Data from Room"
        val lightTxt = "Switching to Light Theme!"
        val darkTxt = "Switching to Dark Theme!"

        val editor =  App.sharedPreferences.edit()

        when(item.itemId){
            R.id.menuRead ->
            {
                Toast.makeText(this,readTxt,Toast.LENGTH_SHORT).show()

                intent = Intent(App.applicationContext(), ReadDataAdapter::class.java)
                startActivity(intent)

            }
            R.id.menuDelete ->
            {
                Toast.makeText(this,deleteTxt,Toast.LENGTH_SHORT).show()
                viewModel.deleteData()
            }
            R.id.lightTheme ->
            {
                Toast.makeText(this,lightTxt,Toast.LENGTH_SHORT).show()
                AppCompatDelegate.setDefaultNightMode(light)

                editor.putInt(KEY_NIGHT_MODE, light)
                editor.apply()
            }
            R.id.darkTheme ->
            {
                Toast.makeText(this,darkTxt,Toast.LENGTH_SHORT).show()
                AppCompatDelegate.setDefaultNightMode(dark)

                editor.putInt(KEY_NIGHT_MODE, dark)
                editor.apply()
            }
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

    private fun loadSharedPreferences(){

        val sp = App.sharedPreferences
        val theme = sp.getInt(KEY_NIGHT_MODE,light)
        AppCompatDelegate.setDefaultNightMode(theme)

    }
}