package org.android.androidsubeen

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : GalleryRepository {
    override fun galleryPagingSource() = GalleryPagingDataSource(context)
}