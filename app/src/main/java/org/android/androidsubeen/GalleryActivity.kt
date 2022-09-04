package org.android.androidsubeen

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.android.androidsubeen.databinding.ActivityGalleryBinding

@AndroidEntryPoint
class GalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalleryBinding
    private lateinit var adapter: GalleryPagingAdapter
    private val viewModel: GalleryViewModel by viewModels()
    val pagingAdapter = GalleryPagingAdapter(::onItemClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)

        initializeView()
        addCollectors()
    }

    private fun initializeView() {
        adapter = GalleryPagingAdapter(::onItemClick)
        binding.imageList.adapter = adapter
        binding.back.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun onItemClick(imageUri: Uri) {
        moveToPrevious(imageUri)
    }

    private fun addCollectors() {
        binding.imageList.adapter = pagingAdapter

        lifecycleScope.launch {
            viewModel.imageList.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }

    private fun moveToPrevious(galleryImageUri: Uri) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(ARG_IMAGE_URI, galleryImageUri)
        setResult(RESULT_OK, intent)
        finish()
    }

    companion object {
        private const val TAG = "GalleryImageFragment"
        private const val ARG_IMAGE_URI = "imageUri"
    }
}