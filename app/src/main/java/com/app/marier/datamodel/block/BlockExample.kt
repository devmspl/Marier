package com.app.marier.datamodel.block

data class BlockExample(
    val `data`: Data,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int
)