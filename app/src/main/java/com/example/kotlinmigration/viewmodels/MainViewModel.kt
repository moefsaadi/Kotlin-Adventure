package com.example.kotlinmigration.viewmodels


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinmigration.database.repository.PostRepository
import com.example.kotlinmigration.database.Postdb
import com.example.kotlinmigration.database.dto.PostDto
import com.example.kotlinmigration.models.API.PostsJsonItem
import com.example.kotlinmigration.models.API.ServiceAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://jsonplaceholder.typicode.com/"

class MainViewModel(): ViewModel() {

//    private val readAllData: LiveData<List<PostsJsonItem>>
//    private val repository: PostRepository
//
//    init {
//        val postDao = Postdb.getDatabase(application).postDao()
//        repository = PostRepository(postDao)
//        readAllData = repository.readAllData
//
//    }

    sealed class RetrofitEvent{
        object Idle: RetrofitEvent()
        object Running: RetrofitEvent()
        data class Successful(val response: List<PostsJsonItem>?): RetrofitEvent()
        data class Failed(val msg: String?) :RetrofitEvent()
    }

    private val _retrofitState = MutableStateFlow<RetrofitEvent>(RetrofitEvent.Idle)
    val retrofitState: StateFlow<RetrofitEvent> = _retrofitState


    fun initRetrofit() {

        viewModelScope.launch (Dispatchers.IO) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

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
                }

                override fun onFailure(call: Call<List<PostsJsonItem>>, t: Throwable) {
                    _retrofitState.tryEmit(RetrofitEvent.Failed(t.message))
                }
            })

           // insertDataToDatabase()
        }
    }

//    fun addPost(postdb: Postdb){
//        viewModelScope.launch (Dispatchers.IO) {
//            repository.addPost(postdb)
//        }
//    }

    private fun insertDataToDatabase(){

      //  val postsEntered = PostDto(Body, ID, UserID, Title)
    }
}