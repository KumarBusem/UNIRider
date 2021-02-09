package com.uni.data.models

data class User(
        val status: Int,
        val phone_number: String?,
        val rc_no: String?,
        val name: String?,
        val driving_licencse_no: String?,
        val rider_id: Int,
        var alternate_no: String?,
        var pan_card: String?,
        val sf_id: String?,
        var aadhar_number: String?,
        val rider_name: String?,
        val token: String?,
        var ifsc_no: String?,
        val insurance_no: String?,
        var email: String?,
        var branch_name: String?,
        var bank_account_no: String?,
        var self_declaration: Boolean,
        var is_profile_pic_uploaded: Boolean,
        var profile_pic_url: String? = null,
        val city: String?,
        val state: String?,
        val hub: String?,
        val rider_type: String?
){
    companion object {

        const val STATUS: String = "status"
        const val PHONE_NUMBER: String = "phone_number"
        const val NAME: String = "name"
        const val RIDER_ID: String = "rider_id"
        const val ALTERNATE_NO: String = "alternate_no"
        const val SF_ID: String = "sf_id"
        const val RIDER_NAME: String = "rider_name"

        const val TOKEN: String = "token"
        const val IS_PROFILE_PIC_UPLOADED: String = "is_profile_pic_uploaded"
        const val PROFILE_PIC_URL: String = "profile_pic_url"

        const val RIDER_TYPE: String = "rider_type"

        const val RIDER_TYPE_LMA: String = "LMA"
        const val RIDER_TYPE_LIQUOR: String = "LIQUOR"
    }
}