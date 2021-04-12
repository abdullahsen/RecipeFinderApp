package com.abdullahsen.recipefinderapp.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.abdullahsen.recipefinderapp.R
import com.abdullahsen.recipefinderapp.databinding.FragmentRecipeDetailsBinding
import com.bumptech.glide.Glide
import java.io.IOException
import java.util.*

class RecipeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentRecipeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRecipeDetailsBinding.inflate(inflater,container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: RecipeDetailsFragmentArgs by navArgs()

        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.recipeDetails.image)
                    .centerCrop()
                    .into(binding.imageViewRecipeImage)
            }catch (exception: IOException){
                exception.printStackTrace()
            }

            binding.textViewTitle.text = it.recipeDetails.title
            binding.textViewType.text = it.recipeDetails.type.capitalize(Locale.ROOT) // Used to make first letter capital
            binding.textViewCategory.text = it.recipeDetails.category
            binding.textViewIngredients.text = it.recipeDetails.ingredients
            binding.textViewCookingDirection.text = it.recipeDetails.cookingDirection
            binding.textViewCookingTime.text = resources.getString(R.string.label_estimate_cooking_time, it.recipeDetails.cookingTime)
        }

    }


}