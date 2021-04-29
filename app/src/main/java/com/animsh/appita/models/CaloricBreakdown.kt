package com.animsh.appita.models


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class CaloricBreakdown(
    @SerializedName("percentCarbs")
    val percentCarbs: Double?,
    @SerializedName("percentFat")
    val percentFat: Double?,
    @SerializedName("percentProtein")
    val percentProtein: Double?
) : Parcelable