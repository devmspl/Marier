package com.app.marier.datamodel.getusermodel

data class GetAlluserExample(
    val `data`: List<Data>,
    val isSuccess: Boolean,
    val pageNo: String,
    val pageSize: String,
    val statusCode: Int,
    val total: Int
)