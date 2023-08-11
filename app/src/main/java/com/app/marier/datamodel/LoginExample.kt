package com.app.marier.datamodel

data class LoginExample(
    val `data`: Data,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int
)