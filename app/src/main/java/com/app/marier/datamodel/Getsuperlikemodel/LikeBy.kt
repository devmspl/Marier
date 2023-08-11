package com.app.marier.datamodel.Getsuperlikemodel

data class LikeBy(
    val _id: String,
    val avatar: String,
    val gallery: List<Gallery>,
    val name: String,
    val sex: String
)