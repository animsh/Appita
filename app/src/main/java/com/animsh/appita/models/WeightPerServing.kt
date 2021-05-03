package com.animsh.appita.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeightPerServing(
    @SerializedName("amount")
    val amount: Int?,
    @SerializedName("unit")
    val unit: String?
) : Parcelable