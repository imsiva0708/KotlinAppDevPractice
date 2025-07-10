package com.example.imageclassifier

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class MainActivity : AppCompatActivity() {
    private lateinit var btnPickImg: Button
    private lateinit var btnPredict: Button
    private lateinit var imageView: ImageView
    private lateinit var tvPrediction: TextView
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private var selectedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImageURI: Uri? = result.data?.data
                if (selectedImageURI != null) {
                    val bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, selectedImageURI)
                    imageView.setImageBitmap(bitmap)

                    selectedBitmap = bitmap
                }
            }
        }
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Permission granted – launch gallery
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageLauncher.launch(intent)
            } else {
                Toast.makeText(this, "Permission denied to access images", Toast.LENGTH_SHORT)
                    .show()
            }
        }



        btnPickImg = findViewById(R.id.btnPickImg)
        btnPredict = findViewById(R.id.btnPredict)
        imageView = findViewById(R.id.imageView)
        tvPrediction = findViewById(R.id.tvPrediction)

        btnPickImg.setOnClickListener {
            val permission =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }

            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageLauncher.launch(intent)
            } else {
                requestPermissionLauncher.launch(permission)
            }
        }

        btnPredict.setOnClickListener(View.OnClickListener {

            var resized: Bitmap = Bitmap.createScaledBitmap(selectedBitmap!!, 224, 224, false)

            val model = AutoModel1.newInstance(this)

            var tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(resized)

            val imageProcessor = ImageProcessor.Builder()
                .build()
            tensorImage = imageProcessor.process(tensorImage)

            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(tensorImage.buffer)
            val outputs = model.process(inputFeature0)


            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

// TODO: Process outputFeature0 to get meaningful results (e.g., class labels and scores)

            model.close()
        }
        )
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}