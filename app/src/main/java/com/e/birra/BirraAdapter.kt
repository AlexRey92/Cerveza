package com.e.birra

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BirraAdapter: ListAdapter<BirraOb,BirraAdapter.ViewHolder>(DiffCallBack) {
    lateinit var onItemClicListener:(BirraOb)->Unit
    inner class ViewHolder(val view:View):RecyclerView.ViewHolder(view) {
        val id:TextView=view.findViewById(R.id.textViewID)
        val name:TextView=view.findViewById(R.id.textViewName)
        val phone:TextView=view.findViewById(R.id.textViewNumber)
        val beerType:TextView=view.findViewById(R.id.textViewBeerType)

        fun onBind( cerveza:BirraOb){
            id.text=cerveza.id.toString()
            name.text=cerveza.name
            beerType.text=beerType.text
            view.setOnClickListener{
                onItemClicListener(cerveza)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val holderCerveza=getItem(position)
        holder.onBind(holderCerveza)
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<BirraOb>() {
        override fun areItemsTheSame(oldItem: BirraOb, newItem: BirraOb): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BirraOb, newItem: BirraOb): Boolean {
            return oldItem == newItem
        }
    }
}