package com.abdullahsen.recipefinderapp.view.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.abdullahsen.recipefinderapp.R
import com.abdullahsen.recipefinderapp.databinding.ActivityAddUpdateRecipeBinding
import com.abdullahsen.recipefinderapp.databinding.DialogImageSelectionBinding

class AddUpdateRecipeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivityAddUpdateRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAddUpdateRecipeBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        setupActionBar()

        mBinding.imageViewAddRecipe.setOnClickListener(this)
    }

    private fun setupActionBar(){
        setSupportActionBar(mBinding.toolbarAddRecipeActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarAddRecipeActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(view: View?) {
        if(view != null){
            when(view.id){
                R.id.image_view_add_recipe -> {
                    customImageDialogHandler()
                    return
                }
            }
        }
    }


    private fun customImageDialogHandler(){
        val dialog = Dialog(this)
        val binding = DialogImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.show()

        binding.textViewCamera.setOnClickListener {
            Toast.makeText(this, "Camera clicked", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }

        binding.textViewGallery.setOnClickListener {
            Toast.makeText(this, "Gallery clicked", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
    }
}