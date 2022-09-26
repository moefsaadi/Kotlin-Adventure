package com.example.kotlinmigration

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.example.kotlinmigration.API.PostsJsonItem

class MyAdapter() : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private val myList = emptyList<PostsJsonItem>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textViewUserId : TextView = itemView.findViewById(R.id.userId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       //return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_post,parent,false))
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_post,parent,false)
        return MyViewHolder(view)
    }


    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textViewUserId.text = "whatever user Id should be"
    }

}

