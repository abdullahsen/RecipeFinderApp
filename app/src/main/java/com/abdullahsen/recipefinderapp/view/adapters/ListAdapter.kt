package com.abdullahsen.recipefinderapp.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdullahsen.recipefinderapp.databinding.ItemListBinding
import com.abdullahsen.recipefinderapp.view.activities.AddUpdateRecipeActivity

class ListAdapter(private val activity: Activity,
                  private val list: List<String>,
                  private val selection:String):RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    class ViewHolder(view:ItemListBinding):RecyclerView.ViewHolder(view.root){
        val textViewText = view.textViewText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemListBinding =
            ItemListBinding.inflate(LayoutInflater.from(activity),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.textViewText.text = item
        holder.itemView.setOnClickListener {
            if(activity is AddUpdateRecipeActivity){
                activity.selectedListItem(item, selection)
            }
        }
    }

    override fun getItemCount() = list.size
}