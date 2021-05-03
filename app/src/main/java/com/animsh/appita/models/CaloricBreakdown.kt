package com.animsh.appita.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CaloricBreakdown(
    @SerializedName("percentCarbs")
    val percentCarbs: Double?,
    @SerializedName("percentFat")
    val percentFat: Double?,
    @SerializedName("percentProtein")
    val percentProtein: Double?
) : Parcelable