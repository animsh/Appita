package com.animsh.appita.bindingadapters

import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.text.Html
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch
import com.animsh.appita.R
import com.animsh.appita.models.ExtendedIngredient
import com.animsh.appita.models.Nutrition
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.util.*


/**
 * Created by animsh on 3/1/2021.
 */
class ItemBindingAdapter {
    companion object {

        @BindingAdapter("android:loadImageFromURL")
        @JvmStatic
        fun loadImageFromURL(imageView: ImageView, imageUrl: Int) {
            Glide.with(imageView.context)
                .load("https://spoonacular.com/recipeImages/$imageUrl-636x393.jpg")
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }

        @BindingAdapter("android:loadIngredientImage")
        @JvmStatic
        fun loadIngredientImage(imageView: ImageView, imageUrl: String) {
            Glide.with(imageView.context)
                .load("https://spoonacular.com/cdn/ingredients_100x100/$imageUrl")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }

        @BindingAdapter("android:loadIngredientImageBackground")
        @JvmStatic
        fun loadBackGround(imageView: ImageView, imageUrl: String) {
            Glide.with(imageView).asBitmap()
                .load("https://spoonacular.com/cdn/ingredients_100x100/$imageUrl")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Bitmap?> {
                    override fun onLoadFailed(
                        @Nullable e: GlideException?,
                        model: Any,
                        target: Target<Bitmap?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any,
                        target: Target<Bitmap?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        val p: Palette = Palette.from(resource!!).generate()
                        val palette: Swatch = getDominantSwatch(p)
                        imageView.background = ContextCompat.getDrawable(
                            imageView.context,
                            R.drawable.background_ingredients
                        )
                        val drawable: GradientDrawable = imageView.background as GradientDrawable
                        drawable.setColor(palette.rgb)
                        drawable.cornerRadius = 20f
                        return true
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }

        private fun getDominantSwatch(palette: Palette): Swatch {
            // find most-represented swatch based on population
            return Collections.max(
                palette.swatches
            ) { sw1, sw2 -> sw1.population.compareTo(sw2.population) }
        }

        @BindingAdapter("android:loadStars")
        @JvmStatic
        fun loadStars(ratingBar: RatingBar, score: Double) {
            ratingBar.rating = ((score * 5) / 100).toFloat()
        }

        @BindingAdapter("android:setHTMLText")
        @JvmStatic
        fun setHTMLText(textView: TextView, overView: String?) {
            if (!overView.isNullOrEmpty()) {
                val htmlAsSpanned = Html.fromHtml(overView)
                textView.text = htmlAsSpanned
            }
        }

        @BindingAdapter("android:setLikes")
        @JvmStatic
        fun setLikes(textView: TextView, likes: Int) {
            val likesTxt = "$likes likes"
            textView.text = likesTxt
        }

        @BindingAdapter("android:setMinutes")
        @JvmStatic
        fun setMinutes(textView: TextView, minutes: Int) {
            val minutesTxt = "$minutes min"
            textView.text = minutesTxt
        }

        @BindingAdapter("android:setServings")
        @JvmStatic
        fun setServing(textView: TextView, serving: Int) {
            val servings = "$serving serve"
            textView.text = servings
        }

        @BindingAdapter("android:loadStepCount")
        @JvmStatic
        fun loadStepCount(textView: TextView, number: Int) {
            val txt = "Step $number"
            textView.text = txt
        }

        @BindingAdapter("android:setAmount", "android:setUnit", requireAll = true)
        @JvmStatic
        fun loadMeasure(textView: TextView, amount: Double, unit: String) {
            var txt = ""
            if (unit.isEmpty()) {
                txt = "$amount items"
            } else {
                txt = "$amount $unit"
            }
            textView.isSelected = true
            textView.text = txt
        }

        @BindingAdapter("android:loadNoOfIngredients")
        @JvmStatic
        fun loadNoOfIngredients(textView: TextView, ingredients: List<ExtendedIngredient?>?) {
            val txt = ingredients?.size.toString() + " ingredients"
            textView.text = txt
        }

        @BindingAdapter("android:loadCal")
        @JvmStatic
        fun loadCal(textView: TextView, nutrition: Nutrition) {
            val txt =
                nutrition.nutrients?.get(0)?.amount.toString() + " " + nutrition.nutrients?.get(0)?.unit

            textView.text = txt
        }
    }


}