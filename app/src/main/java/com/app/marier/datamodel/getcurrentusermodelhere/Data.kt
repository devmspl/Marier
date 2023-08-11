package com.app.marier.datamodel.getcurrentusermodelhere

data class Data(
    val avatar: String,
    val bio: String,
    val createdAt: String,
    val dob: String,
    val email: String,
    val gallery: List<Gallery>,
    val id: String,
    val interests: List<Interest>,
    val ipAddress: String,
    val loc: String,
    val name: String,
    val phoneNumber: Long,
    val platform: String,
    val setting: Setting,
    val sex: String,
    val sexualOrientations: List<Any>,
    val socialLogin: SocialLogin,
    val status: String,
    val updatedAt: String,
    val uuid: String
)