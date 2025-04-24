package com.defined.mobile.entities

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id") val userId: String
)
