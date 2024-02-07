package uz.salikhdev.workphoto

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import uz.salikhdev.workphoto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 101)
        }


        binding.buttonCamera.setOnClickListener {
            launcherCamera.launch(null)
        }

        binding.buttonGalery.setOnClickListener {
            launcherGalery.launch("image/*")
        }

    }


    private val launcherCamera = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { result ->

            result?.let {
                binding.image.setImageBitmap(it)
            }

        }

    private val launcherGalery = registerForActivityResult(ActivityResultContracts.GetContent()) {uri->
            uri?.let {
                binding.image.setImageURI(uri)
            }
        }


    // Kerak bo'lsa Uri orqali Bitmap yaratish.
    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        var parcelFileDescriptor: ParcelFileDescriptor? = null
        try {
            parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")

            val fileDescriptor = parcelFileDescriptor?.fileDescriptor
            return BitmapFactory.decodeFileDescriptor(fileDescriptor)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                parcelFileDescriptor?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

}
