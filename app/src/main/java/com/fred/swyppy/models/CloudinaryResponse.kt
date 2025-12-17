package com.fred.swyppy.models

import com.google.gson.annotations.SerializedName

data class CloudinaryResponse(
    val url: String,
    val secure_url:String,
    val public_id: String
)
