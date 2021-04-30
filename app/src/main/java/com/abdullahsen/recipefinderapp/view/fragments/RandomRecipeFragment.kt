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
import com.abdullahsen.recipefinderapp.RecipeFinderApplication
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe
import com.abdullahsen.recipefinderapp.data.remote.model.RandomRecipe
import com.abdullahsen.recipefinderapp.databinding.FragmentRandomRecipeBinding
import com.abdullahsen.recipefinderapp.utils.Constants
import com.abdullahsen.recipefinderapp.viewmodel.RandomRecipeViewModel
import com.abdullahsen.recipefinderapp.viewmodel.RecipeViewModel
import com.abdullahsen.recipefinderapp.viewmodel.RecipeViewModelFactory
import com.bumptech.glide.Glide

class RandomRecipeFragment : Fragment() {

    private lateinit var binding: FragmentRandomRecipeBinding

    private val randomRecipeViewModel: RandomRecipeViewModel by viewModels()

    private lateinit var progressDialog: Dialog

    private val recipeViewModel: RecipeViewModel by viewModels{
        RecipeViewModelFactory((requireActivity().application as RecipeFinderApplication).repository)
    }

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

        randomRecipeViewModel.getRandomRecipeFromApi()
        randomRecipeViewModel.randomRecipeResponse.observe(viewLifecycleOwner) {
            it?.let {
                Log.i("Random Recipe Response", "${it.recipes[0]}")

                if(binding.swipeRefreshLayoutRandomRecipe.isRefreshing){
                    binding.swipeRefreshLayoutRandomRecipe.isRefreshing = false
                }

                setRandomRecipeResponseInUI(it.recipes[0])
            }
        }

        randomRecipeViewModel.randomRecipeLoadingError.observe(viewLifecycleOwner) {
            it?.let {
                if (it) Log.e("Random Recipe Error", "$it")

                if(binding.swipeRefreshLayoutRandomRecipe.isRefreshing){
                    binding.swipeRefreshLayoutRandomRecipe.isRefreshing = false
                }
            }
        }

        randomRecipeViewModel.loadRandomRecipe.observe(viewLifecycleOwner) {
            it?.let {
                Log.i("Random Recipe Loading", "$it")
                if(it && !binding.swipeRefreshLayoutRandomRecipe.isRefreshing){
                    showCustomProgressDialog()
                }else{
                    hideCustomProgressDialog()
                }
            }
        }

        binding.swipeRefreshLayoutRandomRecipe.setOnRefreshListener {
            randomRecipeViewModel.getRandomRecipeFromApi()
        }

    }

    private fun showCustomProgressDialog(){
        progressDialog = Dialog(requireActivity())
        progressDialog.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }

    private fun hideCustomProgressDialog(){
        progressDialog.let {
            it.dismiss()
        }
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