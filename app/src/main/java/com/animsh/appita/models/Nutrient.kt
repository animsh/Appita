package com.animsh.appita.models


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Nutrient(
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("percentOfDailyNeeds")
    val percentOfDailyNeeds: Double?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("unit")
    val unit: String?
) : Parcelable