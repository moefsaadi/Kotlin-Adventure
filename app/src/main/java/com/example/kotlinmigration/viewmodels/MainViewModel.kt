package com.example.kotlinmigration.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinmigration.app.App
import com.example.kotlinmigration.app.App.Companion.retrofit
import com.example.kotlinmigration.database.dto.PostDto
import com.example.kotlinmigration.models.API.PostsJsonItem
import com.example.kotlinmigration.models.API.ServiceAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val BASE_URL = "https://jsonplaceholder.typicode.com/"

class MainViewModel: ViewModel() {

    sealed class RetrofitEvent {
        object Idle : RetrofitEvent()
        object Running : RetrofitEvent()
        data class Successful(val response: List<PostsJsonItem>?) : RetrofitEvent()
        data class Failed(val msg: String?) : RetrofitEvent()
    }

    sealed class DatabaseEvent {
        data class SuccessfulRead(val data: List<PostDto>) : DatabaseEvent()
    }


    private val _databaseFlow = MutableSharedFlow<DatabaseEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val databaseFlow: SharedFlow<DatabaseEvent> = _databaseFlow
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

                }

                override fun onFailure(call: Call<List<PostsJsonItem>>, t: Throwable) {
                    _retrofitState.tryEmit(RetrofitEvent.Failed(t.message))
                }
            })
        }
    }

    fun insertDataToDatabase(data: List<PostsJsonItem>) {

        viewModelScope.launch(Dispatchers.IO) {

                            for (item in data) {
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

    fun convertRoomData(data: List<PostDto>){

        viewModelScope.launch(Dispatchers.IO){

            for(item in data){
                    PostsJsonItem(
                    body = item.Body,
                    id = item.ID,
                    userId = item.UserID,
                    title = item.Title
                )
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

            val postDtoList = App.room.postDao().readAllData()
            _databaseFlow.emit(DatabaseEvent.SuccessfulRead(postDtoList))

            convertRoomData(postDtoList)


        }
    }

}