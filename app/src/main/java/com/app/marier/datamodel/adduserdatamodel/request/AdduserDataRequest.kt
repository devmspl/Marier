package com.app.marier.datamodel.adduserdatamodel.request

data class AdduserDataRequest(
    val dob: String,
    val name: String,
    val setting: Setting,
    val sex: String,
    val bio: String
)