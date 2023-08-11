package com.app.marier.datamodel.sociallogin

data class SocialLoginExample(
    val `data`: Data,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int,
    val token: String
)