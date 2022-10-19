package com.example.kotlinmigration.viewmodels


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kotlinmigration.app.App
import com.example.kotlinmigration.app.App.Companion.retrofit
import com.example.kotlinmigration.database.Postdb
import com.example.kotlinmigration.database.dao.PostDao
import com.example.kotlinmigration.database.dto.PostDto
import com.example.kotlinmigration.models.API.PostsJson
import com.example.kotlinmigration.models.API.PostsJsonItem
import com.example.kotlinmigration.models.API.ServiceAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://jsonplaceholder.typicode.com/"

class MainViewModel: ViewModel() {

    sealed class RetrofitEvent {
        object Idle : RetrofitEvent()
        object Running : RetrofitEvent()
        data class Successful(val response: List<PostsJsonItem>?) : RetrofitEvent()
        data class Failed(val msg: String?) : RetrofitEvent()
    }

    private val _retrofitState = MutableStateFlow<RetrofitEvent>(RetrofitEvent.Idle)
    val retrofitState: StateFlow<RetrofitEvent> = _retrofitState


    fun makeApiCall() {

        viewModelScope.launch(Dispatchers.IO) {

            val serviceApi = retrofit.create(ServiceAPI::class.java)

            val call = serviceApi.getPosts()
            _retrofitState.emit(RetrofitEvent.Running)

            call.enqueue(object : Callback<List<PostsJsonItem>> {
                override fun onResponse(
                    call: Call<List<PostsJsonItem>>,
                    response: Response<List<PostsJsonItem>>
                ) {
                    if (!response.isSuccessful) {
                        val codeStr = response.code().toString()
                        _retrofitState.tryEmit(RetrofitEvent.Failed(codeStr))
                        return
                    }
                    _retrofitState.tryEmit(RetrofitEvent.Successful(response.body()))

                    insertDataToDatabase() //insert API data into room


                }

                override fun onFailure(call: Call<List<PostsJsonItem>>, t: Throwable) {
                    _retrofitState.tryEmit(RetrofitEvent.Failed(t.message))
                }
            })
        }
    }

    fun insertDataToDatabase() {

        viewModelScope.launch(Dispatchers.IO) {


            _retrofitState.collect() {

                when (it) {

                    is RetrofitEvent.Successful -> {
                        if (it.response != null) {
                            for (item in it.response) {
                                    val convertedData = PostDto(
                                        Body = item.body,
                                        ID = item.id,
                                        UserID = item.userId,
                                        Title = item.title
                                    )

                                App.room.postDao().addPost(convertedData)
                            }
                        }

                    }
                    is RetrofitEvent.Failed -> {}
                    RetrofitEvent.Idle -> {}
                    RetrofitEvent.Running -> {}


                }


            }

        }

    }


    fun deleteData(){

        viewModelScope.launch(Dispatchers.IO) {

            App.room.postDao().deleteAllData()

        }
    }

    fun readData(){

        viewModelScope.launch(Dispatchers.IO) {


            //checking if PostDto is populated by observing a flow.. had to make readAllData() not a suspend fnc. not sure if correct.
            App.room.postDao().readAllData().collect(){

                if(it.isNotEmpty()){
                    //insert code here after adding toolbar to populate new (activity?) with data
                }
            }
        }
    }

}







//collect from retrofitState
//when successful, take it.response: List<PostsJsonItem>?
//convert to List<PostDto>
/*
          for(item in it.response)
                    {
                        PostDto(
                            Body = item.body,
                            ID = item.id
                        )

                    }
 */
//call PostDao.add with that list
//ignore other states


//UNRELATED TO THIS FN
//we need some way to delete PostDtos from DB

//we're gonna need a second function called, readFromDB, that mainActivity can call like
//viewmodel.readFromDB, this may need to return a flow
