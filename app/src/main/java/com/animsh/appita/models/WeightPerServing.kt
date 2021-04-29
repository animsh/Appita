package com.animsh.appita.models


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class WeightPerServing(
    @SerializedName("amount")
    val amount: Int?,
    @SerializedName("unit")
    val unit: String?
) : Parcelable