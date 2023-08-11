package com.app.marier.datamodel.dislike.getalldislikes

data class DislikeBy(
    val _id: String,
    val avatar: String,
    val email: String,
    val gallery: List<Any>,
    val name: String,
    val sex: String
)