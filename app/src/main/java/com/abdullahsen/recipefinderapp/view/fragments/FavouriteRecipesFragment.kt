package com.abdullahsen.recipefinderapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.abdullahsen.recipefinderapp.RecipeFinderApplication
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe
import com.abdullahsen.recipefinderapp.databinding.FragmentFavouriteRecipesBinding
import com.abdullahsen.recipefinderapp.view.activities.MainActivity
import com.abdullahsen.recipefinderapp.view.adapters.RecipeAdapter
import com.abdullahsen.recipefinderapp.viewmodel.RecipeViewModel
import com.abdullahsen.recipefinderapp.viewmodel.RecipeViewModelFactory

class FavouriteRecipesFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteRecipesBinding

    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory((requireActivity().application as RecipeFinderApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteRecipesBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewFavouriteRecipesList.layoutManager = GridLayoutManager(requireActivity(),2)
        val recipeAdapter: RecipeAdapter = RecipeAdapter(this@FavouriteRecipesFragment)
        binding.recyclerViewFavouriteRecipesList.adapter = recipeAdapter

        recipeViewModel.allFavouriteRecipesList.observe(viewLifecycleOwner){
            it.let {
                if(it.isNotEmpty()){
                    binding.recyclerViewFavouriteRecipesList.visibility = View.VISIBLE
                    binding.textViewNoFavouriteRecipes.visibility = View.GONE
                    recipeAdapter.recipeList(it)
                }else{
                    binding.recyclerViewFavouriteRecipesList.visibility = View.INVISIBLE
                    binding.textViewNoFavouriteRecipes.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }

    fun navigateToRecipeDetails(recipe:Recipe){
        findNavController().navigate(FavouriteRecipesFragmentDirections.actionNavigationFavouriteRecipesToNavigationRecipeDetails(recipe))
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
    }
}