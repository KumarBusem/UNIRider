package com.uni.data.models


data class Runsheet(
        val status: String,
        val id: String,
        val date: String,
        val ofd: Int,
        val delivered: Int,
        val remarks: String,
        val rider_name: String,
        val rider: String
){

    companion object {
        const val RUNSHEET_STATUS_INPROGRESS: String = "INPROGRESS"
        const val RUNSHEET_STATUS_VERIFIED: String = "VERIFIED"
    }
}