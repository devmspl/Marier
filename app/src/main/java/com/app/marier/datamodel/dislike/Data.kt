package com.app.marier.datamodel.dislike

data class Data(
    val _id: String,
    val createdAt: String,
    val dislikeBy: String,
    val dislikeTo: String,
    val isdislike: Boolean,
    val updatedAt: String
)