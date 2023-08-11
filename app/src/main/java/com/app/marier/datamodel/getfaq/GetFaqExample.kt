package com.app.marier.datamodel.getfaq

data class GetFaqExample(
    val `data`: List<Data>,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int
)