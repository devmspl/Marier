package com.app.marier.datamodel.getusermodel

data class Data(
    val _id: String,
    val address: String,
    val avatar: String,
    val createdAt: String,
    val dob: String,
    val email: String,
    val bio: String,
    val fcmToken: String,
    val gallery: List<Gallery>,
    val interests: List<String>,
    val ipAddress: String,
    val is_deleted: Boolean,
    val name: String,
    val password: String,
    val phoneNumber: Long,
    val platform: String,
    val role: String,
    val setting: Setting,
    val sex: String,
    val sexualOrientations: List<String>,
    val socialLogin: SocialLogin,
    val status: String,
    val token: String,
    val updatedAt: String,
    val uuid: String
)