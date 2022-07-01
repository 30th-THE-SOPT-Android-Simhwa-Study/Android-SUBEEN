package org.android.androidsubeen

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import org.android.androidsubeen.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    // 기존 코드에서 적절한 위치에 변수와 함수를 복붙해주시고, 함수를 호출해주세요!
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private fun initLayout() {
        binding.btnAddImage.setOnClickListener {
            requestStorage.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        setSignUpResult()
    }

    private fun setSignUpResult() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
                val data = result.data ?: return@registerForActivityResult
                binding.ivGalleryImage.load(data.getParcelableExtra(ARG_IMAGE_URI))
            }
    }

    private val requestStorage =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) resultLauncher.launch(Intent(this, GalleryActivity::class.java))
        }

    companion object {
        private const val ARG_IMAGE_URI = "imageUri"
    }
}
