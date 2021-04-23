package com.abdullahsen.recipefinderapp.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdullahsen.recipefinderapp.R
import com.abdullahsen.recipefinderapp.RecipeFinderApplication
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe
import com.abdullahsen.recipefinderapp.databinding.ActivityAddUpdateRecipeBinding
import com.abdullahsen.recipefinderapp.databinding.DialogImageSelectionBinding
import com.abdullahsen.recipefinderapp.databinding.DialogListBinding
import com.abdullahsen.recipefinderapp.utils.Constants
import com.abdullahsen.recipefinderapp.view.adapters.ListAdapter
import com.abdullahsen.recipefinderapp.viewmodel.RecipeViewModel
import com.abdullahsen.recipefinderapp.viewmodel.RecipeViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.*
import java.util.*


class AddUpdateRecipeActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "ADD_UPDATE_RECIPE"
        private const val CAMERA_REQUEST_CODE = 1402
        private const val GALLERY_REQUEST_CODE = 3506
        private const val IMAGE_DIRECTORY = "recipefinderapp"
    }

    private val mRecipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory((application as RecipeFinderApplication).repository)
    }

    private lateinit var mBinding: ActivityAddUpdateRecipeBinding
    private var imagePath: String = ""
    private lateinit var mCustomListDialog: Dialog
    private var recipeDetails: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAddUpdateRecipeBinding.inflate(layoutInflater)

        setContentView(mBinding.root)
        
        if(intent.hasExtra(Constants.EXTRA_RECIPE_DETAILS)){
            recipeDetails = intent.getParcelableExtra(Constants.EXTRA_RECIPE_DETAILS)
        }
        setupActionBar()
        recipeDetails?.let {
            if(it.id!=0L){
                imagePath = it.image
                Glide.with(this@AddUpdateRecipeActivity)
                    .load(imagePath)
                    .centerCrop()
                    .into(mBinding.imageViewRecipe)

                mBinding.editTextTitle.setText(it.title)
                mBinding.editTextType.setText(it.type)
                mBinding.editTextCategory.setText(it.category)
                mBinding.editTextIngredients.setText(it.ingredients)
                mBinding.editTextCookingTime.setText(it.cookingTime)
                mBinding.editTextDirectionToCook.setText(it.cookingDirection)
                mBinding.buttonAddRecipe.text =  resources.getText(R.string.label_update_recipe)
            }
        }

        mBinding.imageViewAddRecipe.setOnClickListener(this)
        mBinding.editTextCategory.setOnClickListener(this)
        mBinding.editTextType.setOnClickListener(this)
        mBinding.editTextCookingTime.setOnClickListener(this)
        mBinding.buttonAddRecipe.setOnClickListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(mBinding.toolbarAddRecipeActivity)

        if(recipeDetails != null && recipeDetails!!.id != 0L){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_recipe)
            }
        }else{
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_recipe)
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarAddRecipeActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.image_view_add_recipe -> {
                    customImageDialogHandler()
                    return
                }
                R.id.edit_text_type -> {
                    customListItemsDialogHandler(
                        resources.getString(R.string.title_select_dish_type),
                        Constants.recipeTypes(),
                        Constants.RECIPE_TYPE
                    )
                    return
                }
                R.id.edit_text_category -> {
                    customListItemsDialogHandler(
                        resources.getString(R.string.title_select_dish_category),
                        Constants.recipeCategories(),
                        Constants.RECIPE_CATEGORY
                    )
                    return
                }
                R.id.edit_text_cooking_time -> {
                    customListItemsDialogHandler(
                        resources.getString(R.string.title_select_dish_cooking_time),
                        Constants.recipeCookTime(),
                        Constants.RECIPE_COOKING_TIME
                    )
                    return
                }
                R.id.button_add_recipe -> {
                    val title = mBinding.editTextTitle.text.toString().trim()
                    val type = mBinding.editTextType.text.toString().trim()
                    val category = mBinding.editTextCategory.text.toString().trim()
                    val ingredients = mBinding.editTextIngredients.text.toString().trim()
                    val cookingTimeInMinutes = mBinding.editTextCookingTime.text.toString().trim()
                    val cookingDirection = mBinding.editTextDirectionToCook.text.toString().trim()

                    when {
                        TextUtils.isEmpty(imagePath) -> {
                            Toast.makeText(
                                this@AddUpdateRecipeActivity,
                                resources.getString(R.string.err_msg_select_recipe_image),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        TextUtils.isEmpty(title) -> {
                            Toast.makeText(
                                this@AddUpdateRecipeActivity,
                                resources.getString(R.string.err_msg_enter_recipe_title),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        TextUtils.isEmpty(type) -> {
                            Toast.makeText(
                                this@AddUpdateRecipeActivity,
                                resources.getString(R.string.err_msg_select_recipe_type),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        TextUtils.isEmpty(category) -> {
                            Toast.makeText(
                                this@AddUpdateRecipeActivity,
                                resources.getString(R.string.err_msg_select_recipe_category),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        TextUtils.isEmpty(ingredients) -> {
                            Toast.makeText(
                                this@AddUpdateRecipeActivity,
                                resources.getString(R.string.err_msg_enter_recipe_ingredients),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        TextUtils.isEmpty(cookingTimeInMinutes) -> {
                            Toast.makeText(
                                this@AddUpdateRecipeActivity,
                                resources.getString(R.string.err_msg_select_recipe_cooking_time),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        TextUtils.isEmpty(cookingDirection) -> {
                            Toast.makeText(
                                this@AddUpdateRecipeActivity,
                                resources.getString(R.string.err_msg_enter_recipe_cooking_instructions),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else -> {
                            var recipeId = 0L
                            var imageSource = Constants.RECIPE_IMAGE_SOURCE_LOCAL
                            var favouriteRecipe = false

                            recipeDetails?.let {
                                if(it.id !=0L){
                                    recipeId = it.id
                                    imageSource = it.imageSource
                                    favouriteRecipe = it.favouriteRecipe
                                }
                            }

                            val recipeDetails: Recipe = Recipe(
                                image = imagePath,
                                imageSource = imageSource,
                                title = title,
                                category = category,
                                type = type,
                                ingredients = ingredients,
                                cookingTime = cookingTimeInMinutes,
                                cookingDirection = cookingDirection,
                                favouriteRecipe = favouriteRecipe,
                                id = recipeId
                            )

                            if(recipeId == 0L){
                                mRecipeViewModel.insert(recipeDetails)
                                Toast.makeText(
                                    this@AddUpdateRecipeActivity,
                                    "You have succesfully added the recipe.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }else{
                                mRecipeViewModel.update(recipeDetails)
                                Toast.makeText(
                                    this@AddUpdateRecipeActivity,
                                    "You have succesfully updated the recipe.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            finish()
                        }
                    }
                }
            }
        }
    }


    private fun customImageDialogHandler() {
        val dialog = Dialog(this)
        val binding = DialogImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.show()

        binding.textViewCamera.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread().check()

            dialog.dismiss()
        }

        binding.textViewGallery.setOnClickListener {
            Dexter.withContext(this).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : PermissionListener {

                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@AddUpdateRecipeActivity,
                        "You don't have necessary permission to select image.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }

            }).onSameThread().check()
            dialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    data?.extras?.let {
                        //mBinding.imageViewRecipe.setImageBitmap(thumbnail)
                        val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap
                        Glide.with(this).load(thumbnail).centerCrop().into(mBinding.imageViewRecipe)
                        imagePath = saveImageToInternalStorage(thumbnail)
                        Log.i(TAG, imagePath)
                        mBinding.imageViewAddRecipe.setImageDrawable(
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_edit
                            )
                        )
                    }
                }
                GALLERY_REQUEST_CODE -> {
                    data?.let {
                        val selectedImageUri = data.data
                        //mBinding.imageViewRecipe.setImageURI(selectedImageUri)
                        Glide.with(this)
                            .load(selectedImageUri)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    Log.e(TAG, "Image Loading Error")
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    resource?.let {
                                        val bitmap: Bitmap = resource.toBitmap()
                                        imagePath = saveImageToInternalStorage(bitmap)
                                        Log.i(TAG, imagePath)
                                    }
                                    return false
                                }

                            })
                            .into(mBinding.imageViewRecipe)
                        mBinding.imageViewAddRecipe.setImageDrawable(
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_edit
                            )
                        )
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.d(TAG, "Image selection cancelled!!!")
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpeg")
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this).setMessage(
            "It looks like you have turned off permissions required for this feature. " +
                    "It can be enabled under Application Settings."
        ).setPositiveButton("GO TO SETTINGS") { _, _ ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }

        }.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()

        }.show()
    }

    private fun customListItemsDialogHandler(
        title: String,
        itemsList: List<String>,
        selection: String
    ) {
        mCustomListDialog = Dialog(this)
        val binding: DialogListBinding = DialogListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)

        binding.textViewTitle.text = title
        binding.recyclerViewList.layoutManager = LinearLayoutManager(this)

        val adapter = ListAdapter(this,null, itemsList, selection)
        binding.recyclerViewList.adapter = adapter
        mCustomListDialog.show()
    }

    fun selectedListItem(item: String, selection: String) {
        when (selection) {
            Constants.RECIPE_TYPE -> {
                mCustomListDialog.dismiss()
                mBinding.editTextType.setText(item)
            }
            Constants.RECIPE_CATEGORY -> {
                mCustomListDialog.dismiss()
                mBinding.editTextCategory.setText(item)
            }
            Constants.RECIPE_COOKING_TIME -> {
                mCustomListDialog.dismiss()
                mBinding.editTextCookingTime.setText(item)
            }

        }
    }


}