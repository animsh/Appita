package com.animsh.appita.bindingadapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.animsh.appita.data.database.RecipeEntity
import com.animsh.appita.models.FoodRecipe
import com.animsh.appita.util.NetworkResult

/**
 * Created by animsh on 3/10/2021.
 */
class RecipeBindingAdapter {

    companion object {

        @BindingAdapter("android:readApiResponse", "android:readDatabase", requireAll = true)
        @JvmStatic
        fun setError(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe?>?,
            database: List<RecipeEntity?>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if (apiResponse is NetworkResult.Loading) {
                textView.visibility = View.INVISIBLE
            } else if (apiResponse is NetworkResult.Success) {
                textView.visibility = View.INVISIBLE
            }

        }
    }
}