package oso.example.com.fsfirebase

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import kotlinx.android.synthetic.main.item_joker.view.*


class PeopleAdapter(val list:ArrayList<People>, val context: Context, val item:Int)
    : RecyclerView.Adapter<PeopleAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = list[position].name.toUpperCase()
        holder.content.text = list[position].content
        holder.content.setOnClickListener {
            Toast.makeText(context,"delete",Toast.LENGTH_SHORT).show()
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size)
        }

        holder.name.setOnClickListener { Toast.makeText(context,"update",Toast.LENGTH_SHORT).show() }

    }


    class ViewHolder(val view:View) : RecyclerView.ViewHolder(view){
        val name = view.name_user
        val content = view.user_content_joker

    }
}