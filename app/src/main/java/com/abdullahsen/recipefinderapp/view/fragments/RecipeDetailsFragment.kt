package com.abdullahsen.recipefinderapp.view.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.abdullahsen.recipefinderapp.R
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe
import com.abdullahsen.recipefinderapp.databinding.FragmentRecipeDetailsBinding
import com.abdullahsen.recipefinderapp.utils.Constants
import com.abdullahsen.recipefinderapp.viewmodel.RecipeViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class RecipeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentRecipeDetailsBinding
    private lateinit var recipeDetails: Recipe

    private val recipeViewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_share_recipe -> {
                val type = "text/plain"
                val subject = "Checkout the Recipe"
                var extraText: String
                val shareWith = "Share With"

                recipeDetails.let {
                    var image = ""
                    if (it.imageSource == Constants.RECIPE_IMAGE_SOURCE_REMOTE) {
                        image = it.image
                    }

                    var cookingInstructions: String
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cookingInstructions = Html.fromHtml(
                            it.cookingDirection,
                            Html.FROM_HTML_MODE_COMPACT
                        ).toString()
                    } else {
                        cookingInstructions = Html.fromHtml(it.cookingDirection).toString()
                    }
                    extraText =
                        "$image \n" +
                                "\n Title:  ${it.title} \n\n Type: ${it.type} \n\n Category: ${it.category}" +
                                "\n\n Ingredients: \n ${it.ingredients} \n\n Instructions To Cook: \n $cookingInstructions" +
                                "\n\n Time required to cook the dish approx ${it.cookingTime} minutes."

                }

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = type
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.putExtra(Intent.EXTRA_TEXT, extraText)
                startActivity(Intent.createChooser(intent, shareWith))
                return true

            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: RecipeDetailsFragmentArgs by navArgs()
        recipeDetails = args.recipeDetails
        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.recipeDetails.image)
                    .centerCrop()
                    .into(binding.imageViewRecipeImage)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

            binding.textViewTitle.text = it.recipeDetails.title
            binding.textViewType.text =
                it.recipeDetails.type.capitalize(Locale.ROOT) // Used to make first letter capital
            binding.textViewCategory.text = it.recipeDetails.category
            binding.textViewIngredients.text = it.recipeDetails.ingredients
            //binding.textViewCookingDirection.text = it.recipeDetails.cookingDirection

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.textViewCookingDirection.text = Html.fromHtml(
                    it.recipeDetails.cookingDirection,
                    Html.FROM_HTML_MODE_COMPACT
                )
            } else {
                @Suppress("DEPRECATION")
                binding.textViewCookingDirection.text =
                    Html.fromHtml(it.recipeDetails.cookingDirection)
            }


            binding.textViewCookingTime.text = resources.getString(
                R.string.label_estimate_cooking_time,
                it.recipeDetails.cookingTime
            )

            if (it.recipeDetails.favouriteRecipe) {
                binding.imageViewFavoriteRecipe.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )
            } else {
                binding.imageViewFavoriteRecipe.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_unselected
                    )
                )
            }
        }

        binding.imageViewFavoriteRecipe.setOnClickListener {
            args.recipeDetails.favouriteRecipe = !args.recipeDetails.favouriteRecipe
            recipeViewModel.update(args.recipeDetails)

            if (args.recipeDetails.favouriteRecipe) {
                binding.imageViewFavoriteRecipe.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )
            } else {
                binding.imageViewFavoriteRecipe.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_unselected
                    )
                )
            }
        }

    }
}