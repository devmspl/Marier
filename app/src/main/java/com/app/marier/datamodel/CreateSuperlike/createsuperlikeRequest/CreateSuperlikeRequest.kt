package com.app.marier.datamodel.CreateSuperlike.createsuperlikeRequest

data class CreateSuperlikeRequest(
    val isSuperLike: Boolean,
    val likedBy: String,
    val likedTo: String
)