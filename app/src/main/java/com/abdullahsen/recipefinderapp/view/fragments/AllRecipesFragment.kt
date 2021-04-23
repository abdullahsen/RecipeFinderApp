package com.abdullahsen.recipefinderapp.view.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdullahsen.recipefinderapp.R
import com.abdullahsen.recipefinderapp.RecipeFinderApplication
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe
import com.abdullahsen.recipefinderapp.databinding.DialogListBinding
import com.abdullahsen.recipefinderapp.databinding.FragmentAllRecipesBinding
import com.abdullahsen.recipefinderapp.utils.Constants
import com.abdullahsen.recipefinderapp.view.activities.AddUpdateRecipeActivity
import com.abdullahsen.recipefinderapp.view.activities.MainActivity
import com.abdullahsen.recipefinderapp.view.adapters.ListAdapter
import com.abdullahsen.recipefinderapp.view.adapters.RecipeAdapter
import com.abdullahsen.recipefinderapp.viewmodel.RecipeViewModel
import com.abdullahsen.recipefinderapp.viewmodel.RecipeViewModelFactory

class AllRecipesFragment : Fragment() {

    private lateinit var binding: FragmentAllRecipesBinding
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var customListDialog: Dialog

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
        recipeAdapter = RecipeAdapter(this@AllRecipesFragment)
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

    private fun filterRecipesListDialogHandler(){
        customListDialog = Dialog(requireActivity())
        val binding: DialogListBinding = DialogListBinding.inflate(layoutInflater)
        customListDialog.setContentView(binding.root)
        binding.textViewTitle.text = resources.getString(R.string.select_item_to_filter)
        val recipeTypes = Constants.recipeTypes()
        recipeTypes.add(0, Constants.ALL_ITEMS)
        binding.recyclerViewList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = ListAdapter(requireActivity(), this@AllRecipesFragment, recipeTypes, Constants.FILTER_SELECTION)
        binding.recyclerViewList.adapter = adapter
        customListDialog.show()
    }

    fun navigateToRecipeDetails(recipe: Recipe){
        findNavController().navigate(AllRecipesFragmentDirections.actionNavigationAllRecipesToNavigationRecipeDetails(recipe))
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
    }

    fun deleteRecipe(recipe: Recipe){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_recipe))
        builder.setMessage(resources.getString(R.string.message_delete_recipe_dialog, recipe.title))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.label_yes)){ dialogInterface, _ ->
            recipeViewModel.delete(recipe)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.label_no)){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun filterSelectionHandler(filterItemSelection:String){
        customListDialog.dismiss()

        if(filterItemSelection == Constants.ALL_ITEMS){
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
        }else{
            recipeViewModel.getFilteredRecipesList(filterItemSelection).observe(viewLifecycleOwner){
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
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.showBottomNavigationView()
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

            R.id.action_filter_recipes -> {
                filterRecipesListDialogHandler()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}