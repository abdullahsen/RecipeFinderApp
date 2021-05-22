package com.abdullahsen.recipefinderapp.view.fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.abdullahsen.recipefinderapp.R
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe
import com.abdullahsen.recipefinderapp.data.remote.model.RandomRecipe
import com.abdullahsen.recipefinderapp.databinding.FragmentRandomRecipeBinding
import com.abdullahsen.recipefinderapp.utils.Constants
import com.abdullahsen.recipefinderapp.utils.RecipeApiStatus
import com.abdullahsen.recipefinderapp.viewmodel.RandomRecipeViewModel
import com.abdullahsen.recipefinderapp.viewmodel.RecipeViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomRecipeFragment : Fragment() {

    private lateinit var binding: FragmentRandomRecipeBinding

    private val randomRecipeViewModel: RandomRecipeViewModel by viewModels()

    private lateinit var progressDialog: Dialog

    private val recipeViewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRandomRecipeBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = Dialog(requireActivity())
        progressDialog.setContentView(R.layout.dialog_custom_progress)

        randomRecipeViewModel.getRandomRecipeFromApi()
        randomRecipeViewModel.recipe.observe(viewLifecycleOwner) {
            it?.let {
                Log.i("Random Recipe Response", "${it.recipes[0]}")
                setRandomRecipeResponseInUI(it.recipes[0])
            }
        }

        randomRecipeViewModel.status.observe(viewLifecycleOwner){
            it?.let {
                when(it){
                    RecipeApiStatus.LOADING -> showCustomProgressDialog()
                    RecipeApiStatus.ERROR -> hideCustomProgressDialog()
                    RecipeApiStatus.DONE -> hideCustomProgressDialog()
                }
            }
        }

        binding.swipeRefreshLayoutRandomRecipe.setOnRefreshListener {
            randomRecipeViewModel.getRandomRecipeFromApi()
        }

    }

    private fun showCustomProgressDialog(){
        progressDialog.show()
    }

    private fun hideCustomProgressDialog(){
        progressDialog.dismiss()
        binding.swipeRefreshLayoutRandomRecipe.isRefreshing = false
    }

    private fun setRandomRecipeResponseInUI(recipe: RandomRecipe.Recipe) {

        Glide.with(requireActivity())
            .load(recipe.image)
            .fitCenter()
            .into(binding.imageViewRecipeImage)

        binding.textViewTitle.text = recipe.title

        var recipeType = "other"
        if (recipe.dishTypes.isNotEmpty()) {
            recipeType = recipe.dishTypes[0]
            binding.textViewType.text = recipeType
        }

        binding.textViewCategory.text = "Other"

        var ingredients = ""
        for (value in recipe.extendedIngredients) {
            if (ingredients.isEmpty()) {
                ingredients = value.original
            } else {
                ingredients = ingredients + ", \n" + value.original
            }
        }
        binding.textViewIngredients.text = ingredients

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.textViewCookingDirection.text = Html.fromHtml(
                recipe.instructions,
                Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            @Suppress("DEPRECATION")
            binding.textViewCookingDirection.text = Html.fromHtml(recipe.instructions)
        }

        binding.imageViewFavoriteRecipe.setImageDrawable(
            ContextCompat.getDrawable(requireActivity(),R.drawable.ic_favorite_unselected)
        )

        binding.textViewCookingTime.text = resources.getString(
            R.string.label_estimate_cooking_time,
            recipe.readyInMinutes.toString()
        )

        var addedToFavourite = false

        binding.imageViewFavoriteRecipe.setOnClickListener {

            if(addedToFavourite){
                Toast.makeText(requireActivity(),
                    resources.getString(R.string.message_already_added_to_favourites), Toast.LENGTH_LONG).show()
            }else{
                val randomRecipeDetails = Recipe(
                    image = recipe.image,
                    imageSource = Constants.RECIPE_IMAGE_SOURCE_REMOTE,
                    title = recipe.title,
                    type = recipeType,
                    category = "Other",
                    ingredients = ingredients,
                    cookingTime = recipe.readyInMinutes.toString(),
                    cookingDirection = recipe.instructions,
                    favouriteRecipe = addedToFavourite
                )
                recipeViewModel.insert(randomRecipeDetails)

                addedToFavourite = true

                binding.imageViewFavoriteRecipe.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )
                Toast.makeText(requireActivity(),
                    resources.getString(R.string.message_added_to_favourites),Toast.LENGTH_LONG).show()
            }


        }


    }
}