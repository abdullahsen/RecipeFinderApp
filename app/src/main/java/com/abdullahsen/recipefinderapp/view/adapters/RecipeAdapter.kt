package com.abdullahsen.recipefinderapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe
import com.abdullahsen.recipefinderapp.databinding.ItemRecipeLayoutBinding
import com.bumptech.glide.Glide

class RecipeAdapter(private val fragment:Fragment):RecyclerView.Adapter<RecipeAdapter.ViewHolder> (){

    private var recipes: List<Recipe> = listOf()

    class ViewHolder(view: ItemRecipeLayoutBinding): RecyclerView.ViewHolder(view.root){
        val imageViewRecipeImage = view.imageViewRecipeImage
        val textViewRecipeTitle = view.textViewRecipeTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecipeLayoutBinding = ItemRecipeLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        Glide.with(fragment)
            .load(recipe.image)
            .into(holder.imageViewRecipeImage)
        holder.textViewRecipeTitle.text = recipe.title
    }

    override fun getItemCount() = recipes.size

    fun recipeList(list: List<Recipe>){
        recipes = list
        notifyDataSetChanged()
    }

}