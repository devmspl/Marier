package com.app.marier.datamodel.otpmodel

data class Setting(
    val ageRange: AgeRange,
    val distance: Int,
    val language: String,
    val location: Location,
    val sexType: String
)