package com.abdullahsen.recipefinderapp.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.abdullahsen.recipefinderapp.databinding.ItemListBinding
import com.abdullahsen.recipefinderapp.view.activities.AddUpdateRecipeActivity
import com.abdullahsen.recipefinderapp.view.fragments.AllRecipesFragment

class ListAdapter(private val activity: Activity,
                  private val fragment: Fragment?,
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
            if(fragment is AllRecipesFragment){
                fragment.filterSelectionHandler(item)
            }
        }
    }

    override fun getItemCount() = list.size
}