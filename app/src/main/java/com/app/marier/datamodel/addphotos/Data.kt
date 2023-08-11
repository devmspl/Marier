package com.app.marier.datamodel.addphotos

data class Data(
    val _id: String,
    val address: String,
    val avatar: String,
    val bio: String,
    val createdAt: String,
    val dob: String,
    val email: Any,
    val fcmToken: String,
    val gallery: List<Gallery>,
    val interests: List<Any>,
    val ipAddress: String,
    val is_deleted: Boolean,
    val lastActive: String,
    val name: String,
    val platform: String,
    val profession: String,
    val setting: Setting,
    val sexualOrientations: List<Any>,
    val socialLogin: SocialLogin,
    val status: String,
    val token: String,
    val updatedAt: String,
    val uuid: String
)