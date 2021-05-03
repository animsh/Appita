package com.animsh.appita.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Step(
    @SerializedName("equipment")
    val equipment: @RawValue List<Equipment?>?,
    @SerializedName("ingredients")
    val ingredients: @RawValue List<Any>?,
    @SerializedName("number")
    val number: Int?,
    @SerializedName("step")
    val step: String?
) : Parcelable