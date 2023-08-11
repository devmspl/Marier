package com.app.marier.datamodel.createlike.createlikerequest

data class CreateLikeReq(
    val isSuperLike: Boolean,
    val likeBy: String,
    val likeTo: String
)