package com.abdullahsen.recipefinderapp.view.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.abdullahsen.recipefinderapp.R
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe
import com.abdullahsen.recipefinderapp.databinding.ItemRecipeLayoutBinding
import com.abdullahsen.recipefinderapp.utils.Constants
import com.abdullahsen.recipefinderapp.view.activities.AddUpdateRecipeActivity
import com.abdullahsen.recipefinderapp.view.fragments.AllRecipesFragment
import com.abdullahsen.recipefinderapp.view.fragments.FavouriteRecipesFragment
import com.bumptech.glide.Glide

class RecipeAdapter(private val fragment:Fragment):RecyclerView.Adapter<RecipeAdapter.ViewHolder> (){

    private var recipes: List<Recipe> = listOf()

    class ViewHolder(view: ItemRecipeLayoutBinding): RecyclerView.ViewHolder(view.root){
        val imageViewRecipeImage = view.imageViewRecipeImage
        val textViewRecipeTitle = view.textViewRecipeTitle
        val imageButtonMore = view.imageButtonMore
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
        holder.itemView.setOnClickListener {
            if(fragment is AllRecipesFragment){
                fragment.navigateToRecipeDetails(recipe)
            }else if(fragment is FavouriteRecipesFragment){
                fragment.navigateToRecipeDetails(recipe)
            }
        }

        holder.imageButtonMore.setOnClickListener {
            val popupMenu = PopupMenu(fragment.context, holder.imageButtonMore)
            popupMenu.menuInflater.inflate(R.menu.menu_adapter, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.action_edit_recipe -> {
                        val intent = Intent(fragment.requireActivity(), AddUpdateRecipeActivity::class.java)
                        intent.putExtra(Constants.EXTRA_RECIPE_DETAILS, recipe)
                        fragment.requireActivity().startActivity(intent)
                    }
                    R.id.action_delete_recipe -> {
                        if(fragment is AllRecipesFragment){
                            fragment.deleteRecipe(recipe)
                        }
                    }
                }
                true
            }
            popupMenu.show()
        }

        if(fragment is AllRecipesFragment){
            holder.imageButtonMore.visibility = View.VISIBLE
        }else if(fragment is FavouriteRecipesFragment){
            holder.imageButtonMore.visibility = View.GONE
        }
    }

    override fun getItemCount() = recipes.size

    fun recipeList(list: List<Recipe>){
        recipes = list
        notifyDataSetChanged()
    }

}