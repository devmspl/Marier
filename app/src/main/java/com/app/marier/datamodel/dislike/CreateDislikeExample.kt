package com.app.marier.datamodel.dislike

data class CreateDislikeExample(
    val `data`: Data,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int
)