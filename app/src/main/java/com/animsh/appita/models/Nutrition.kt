package com.animsh.appita.models


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Nutrition(
    @SerializedName("caloricBreakdown")
    val caloricBreakdown: CaloricBreakdown?,
    @SerializedName("flavanoids")
    val flavanoids: List<Flavanoid?>?,
    @SerializedName("ingredients")
    val ingredients: List<Ingredient?>?,
    @SerializedName("nutrients")
    val nutrients: List<Nutrient?>?,
    @SerializedName("properties")
    val properties: List<Property?>?,
    @SerializedName("weightPerServing")
    val weightPerServing: WeightPerServing?
) : Parcelable