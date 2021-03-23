package com.abdullahsen.recipefinderapp.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.abdullahsen.recipefinderapp.R
import com.abdullahsen.recipefinderapp.RecipeFinderApplication
import com.abdullahsen.recipefinderapp.databinding.FragmentAllRecipesBinding
import com.abdullahsen.recipefinderapp.view.activities.AddUpdateRecipeActivity
import com.abdullahsen.recipefinderapp.view.adapters.RecipeAdapter
import com.abdullahsen.recipefinderapp.viewmodel.HomeViewModel
import com.abdullahsen.recipefinderapp.viewmodel.RecipeViewModel
import com.abdullahsen.recipefinderapp.viewmodel.RecipeViewModelFactory

class AllRecipesFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentAllRecipesBinding

    private val recipeViewModel: RecipeViewModel by viewModels{
        RecipeViewModelFactory((requireActivity().application as RecipeFinderApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewRecipesList.layoutManager = GridLayoutManager(requireActivity(),2)
        val recipeAdapter: RecipeAdapter = RecipeAdapter(this@AllRecipesFragment)
        binding.recyclerViewRecipesList.adapter = recipeAdapter

        recipeViewModel.allRecipesList.observe(viewLifecycleOwner){
            it.let {
                if(it.isNotEmpty()){
                    binding.recyclerViewRecipesList.visibility = View.VISIBLE
                    binding.textViewNoAddedRecipes.visibility = View.GONE
                    recipeAdapter.recipeList(it)
                }else{
                    binding.recyclerViewRecipesList.visibility = View.INVISIBLE
                    binding.textViewNoAddedRecipes.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_recipes,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_recipe -> {
                startActivity(Intent(requireActivity(),AddUpdateRecipeActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}