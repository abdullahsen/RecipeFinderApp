package com.abdullahsen.recipefinderapp.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.abdullahsen.recipefinderapp.R
import com.abdullahsen.recipefinderapp.databinding.ActivityAddUpdateRecipeBinding
import com.abdullahsen.recipefinderapp.databinding.DialogImageSelectionBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener


class AddUpdateRecipeActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val CAMERA_REQUEST_CODE = 1402
        private const val GALLERY_REQUEST_CODE = 3506
    }

    private lateinit var mBinding: ActivityAddUpdateRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAddUpdateRecipeBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        setupActionBar()

        mBinding.imageViewAddRecipe.setOnClickListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(mBinding.toolbarAddRecipeActivity)
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
                        val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap
                        mBinding.imageViewRecipe.setImageBitmap(thumbnail)
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
                        mBinding.imageViewRecipe.setImageURI(selectedImageUri)
                        mBinding.imageViewAddRecipe.setImageDrawable(
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_edit
                            )
                        )
                    }
                }
            }
        }else if (resultCode == Activity.RESULT_CANCELED){
            Log.d("ImageSelection", "Image selection cancelled!!!")
        }
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
}