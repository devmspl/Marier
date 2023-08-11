package com.app.marier.datamodel.dislike.getalldislikes

data class Data(
    val _id: String,
    val createdAt: String,
    val dislikeBy: DislikeBy,
    val dislikeTo: String,
    val updatedAt: String
)