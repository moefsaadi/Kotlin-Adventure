package com.example.kotlinmigration.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinmigration.models.API.PostsJsonItem
import com.example.kotlinmigration.models.API.ServiceAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://jsonplaceholder.typicode.com/"

class MainViewModel: ViewModel() {

    enum class RetrofitStates {
        IDLE, RUNNING, SUCCESSFUL, FAILED
    }


    var retrofitState = MutableLiveData(RetrofitStates.IDLE)
    var retrofitResponse : List<PostsJsonItem>? = null


    fun initRetrofit() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val serviceApi = retrofit.create(ServiceAPI::class.java)

        val call = serviceApi.getPosts()

        retrofitState.postValue(RetrofitStates.RUNNING)

        call.enqueue(object : Callback<List<PostsJsonItem>> {
            override fun onResponse(
                call: Call<List<PostsJsonItem>>,
                response: Response<List<PostsJsonItem>>
            ) {
                //still need to set the state to `successful` here, and `failed` somewhere else..
                if (!response.isSuccessful) {
                    return
                }

                retrofitResponse = response.body()

            }

            override fun onFailure(call: Call<List<PostsJsonItem>>, t: Throwable) {

            }
        })

    }
}