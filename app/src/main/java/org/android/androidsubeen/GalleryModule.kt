package org.android.androidsubeen

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class GalleryModule {
    @Binds
    abstract fun bindGalleryRepositoryImpl(galleryRepositoryImpl: GalleryRepositoryImpl): GalleryRepository
}