package com.app.marier.datamodel.otpmodel

data class OtpExample(
    val `data`: Data,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int,
    val token: String
)