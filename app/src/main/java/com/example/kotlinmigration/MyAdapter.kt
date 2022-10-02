package com.example.kotlinmigration

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat.getDrawable
import com.example.kotlinmigration.API.PostsJsonItem

class MyAdapter(
    private var myList : List<PostsJsonItem>
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //Get the single_post textviews
        val textViewUserId: TextView = itemView.findViewById(R.id.userId)
        val textViewId : TextView = itemView.findViewById(R.id.id)
        val textViewTitle : TextView = itemView.findViewById(R.id.title)
        val textViewBody : TextView = itemView.findViewById(R.id.body)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_post,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int { return myList.size }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //Update RecyclerView
        holder.textViewUserId.text ="userID: "+  myList[position].userId.toString() + "\n"
        holder.textViewId.text = "ID: " + myList[position].id.toString() + "\n"
        holder.textViewTitle.text = "Title: " + myList[position].title + "\n"
        holder.textViewBody.text = "Body: " + myList[position].body

        //Alternate colors of each block
        if(position %2 == 1)holder.itemView.setBackgroundResource(R.drawable.background1)
        else{holder.itemView.setBackgroundResource(R.drawable.background2)}

    }

}

