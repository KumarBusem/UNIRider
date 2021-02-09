package com.uni.data.models


data class RunsheetResponse(
    val status: String,
    val message: String,
    val runsheets: List<Runsheet>
)