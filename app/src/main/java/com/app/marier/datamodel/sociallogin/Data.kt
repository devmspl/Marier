package com.app.marier.datamodel.sociallogin

data class Data(
    val avatar: String,
    val bio: String,
    val createdAt: String,
    val dob: String,
    val email: String,
    val fcmToken: String,
    val gallery: List<Any>,
    val id: String,
    val interests: List<Any>,
    val ipAddress: String,
    val loc: String,
    val name: String,
    val phoneNumber: String,
    val platform: String,
    val profession: String,
    val setting: Setting,
    val sex: String,
    val sexualOrientations: List<Any>,
    val socialLogin: SocialLogin,
    val status: String,
    val updatedAt: String,
    val uuid: String
)