package robi.saputra.nutapos.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import org.koin.androidx.viewmodel.ext.android.viewModel
import robi.saputra.nutapos.BaseFragment
import robi.saputra.nutapos.R
import robi.saputra.nutapos.databinding.FragmentInputUangMasukBinding
import robi.saputra.nutapos.models.FinanceIn
import robi.saputra.nutapos.utils.arePermissionsGranted
import robi.saputra.nutapos.utils.convertBitmapToBase64
import robi.saputra.nutapos.utils.dateTimeToMilliseconds
import robi.saputra.nutapos.utils.decodeBase64ToBitmap
import robi.saputra.nutapos.utils.getBitmapFromUri
import robi.saputra.nutapos.utils.getLocalDateTime
import robi.saputra.nutapos.utils.isCameraPermissionGranted
import robi.saputra.nutapos.utils.isStoragePermissionGranted
import robi.saputra.nutapos.utils.requestCameraPermission
import robi.saputra.nutapos.utils.requestStoragePermission

class FragmentInsertUangMasuk: BaseFragment<FragmentInputUangMasukBinding>() {
    private val viewModel by viewModel<FinanceViewModel>()
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2
    private var base64Image: String = ""

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentInputUangMasukBinding {
        return FragmentInputUangMasukBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.llOption.visibility = View.GONE
            binding.buttonSimpan.visibility = View.VISIBLE
        } else {
            binding.llOption.visibility = View.VISIBLE
            binding.buttonSimpan.visibility = View.GONE
        }

        viewModel.insertTransactions.observe(viewLifecycleOwner) { result ->
            if (result) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Success")
                    .setMessage("Berhasil menyimpan data transaksi")
                    .setPositiveButton("OK") { dialog, _ -> navigatinSimpan() }
                    .setIcon(R.drawable.ic_circle_check)
                    .show()
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Error")
                    .setMessage("Gagal menyimpan data transaksi")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .setIcon(R.drawable.ic_circle_error)
                    .show()
            }
        }

        binding.apply {
            val items = arrayOf("Pendapatan Lain", "Non Pendapatan")
            (etType as? MaterialAutoCompleteTextView)?.setSimpleItems(items)

            cardFoto.setOnClickListener {
                showMediaDialog(requireContext())
            }
            ivFoto.setOnClickListener {
                showPreviewDialog(requireContext(), base64Image)
            }
            buttonUbah.setOnClickListener {
                showMediaDialog(requireContext())
            }

            buttonHapus.setOnClickListener {
                llFotoOption.visibility = View.GONE
                ivFoto.visibility = View.GONE
                base64Image = ""
                Glide.with(requireContext()).load(base64Image).into(ivFoto)
            }

            btnAppBarSimpan.setOnClickListener {
                if (isValidate()) {
                    val financeIn = FinanceIn(
                        id = 0,
                        date = getLocalDateTime().dateTimeToMilliseconds("dd-MM-yyyy HH:mm:ss"),
                        amount = try {
                            binding.etAmount.text.toString().toInt()
                        } catch (e: NumberFormatException) {
                            0
                        },
                        insertTo = binding.etInsertTo.text.toString(),
                        insertFrom = binding.etInsertFrom.text.toString(),
                        desc = binding.etDesc.text.toString(),
                        type = binding.etType.text.toString(),
                        image = base64Image
                    )
                    viewModel.insertTransaction(financeIn)
                } else {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Warning")
                        .setMessage("Form belum lengkap")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .setIcon(R.drawable.ic_warning)
                        .show()
                }
            }
        }
    }

    private fun isValidate(): Boolean {
        return binding.etAmount.text.toString().toIntOrNull()!=null &&
                !binding.etInsertTo.text.isNullOrEmpty() &&
                !binding.etInsertFrom.text.isNullOrEmpty() &&
                !binding.etDesc.text.isNullOrEmpty() &&
                !binding.etType.text.isNullOrEmpty() &&
                base64Image.isNotEmpty()
    }

    private fun setupToolbar() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navigatinSimpan() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            findNavController().navigate(R.id.fragmentLandscapeDaftarUangMasuk)
        } else {
            findNavController().navigate(R.id.fragmentDaftarUangMasuk)
        }
    }

    override fun onOrientationChanged(orientation: Int) {
        super.onOrientationChanged(orientation)
        val fragment = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            R.id.fragmentInsertUangMasuk
        } else {
            R.id.fragmentInsertUangMasuk
        }
        findNavController().navigate(fragment)
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            base64Image = imageBitmap.convertBitmapToBase64()
            binding.ivFoto.visibility = View.VISIBLE
            Glide.with(requireContext())
                .load(imageBitmap)
                .into(binding.ivFoto)
            binding.llFotoOption.visibility = View.VISIBLE
        }
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                val imageBitmap = imageUri.getBitmapFromUri(requireContext())
                imageBitmap?.let {
                    binding.ivFoto.visibility = View.VISIBLE
                    base64Image = it.convertBitmapToBase64()
                    Glide.with(requireContext())
                        .load(imageBitmap)
                        .into(binding.ivFoto)
                    binding.llFotoOption.visibility = View.VISIBLE
                }
            } else {
                Log.e("Image", "Failed to get image URI")
            }
        }
    }

    private fun showMediaDialog(context: Context) {
        val customView = LayoutInflater.from(context).inflate(R.layout.dialog_media_button, null)
        val btnCamera = customView.findViewById<ImageView>(R.id.iv_camera)
        val btnGallery = customView.findViewById<ImageView>(R.id.iv_gallery)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(customView)
            .setCancelable(false)
            .create()
        btnCamera.setOnClickListener {
            dialog.dismiss()
            openCamera()
        }
        btnGallery.setOnClickListener {
            dialog.dismiss()
            openGallery()
        }
        dialog.show()
    }

    private fun showPreviewDialog(context: Context, base64String: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_preview_image, null)
        val btnClose = dialogView.findViewById<Button>(R.id.btn_close)
        val imageView = dialogView.findViewById<ImageView>(R.id.view_image)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .setCancelable(true)  // Allow dialog to be dismissed by tapping outside
            .create()
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        val bitmap = base64String.decodeBase64ToBitmap()
        if (bitmap != null) {
            Glide.with(context)
                .load(bitmap)
                .into(imageView)
        } else {
            Log.e("ImagePreview", "Failed to decode base64 string into Bitmap.")
        }
        dialog.show()
    }
}