package com.animsh.appita.adapters

import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.animsh.appita.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


/**
 * Created by animsh on 3/1/2021.
 */
class ItemBindingAdapter {
    companion object {

        @BindingAdapter("android:loadImageFromURL")
        @JvmStatic
        fun loadImageFromURL(imageView: ImageView, imageUrl: String) {
            try {
//                imageView.alpha = 0f
                Glide.with(imageView.context).load(imageUrl).placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(object : RequestListener<Drawable?> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any,
                            target: Target<Drawable?>,
                            isFirstResource: Boolean
                        ): Boolean {
//                            imageView.animate().setDuration(300).alpha(1f).start()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any,
                            target: Target<Drawable?>,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
//                            imageView.animate().setDuration(300).alpha(1f).start()
                            return false
                        }
                    }).into(imageView)
            } catch (ignored: Exception) {
            }
        }

        @BindingAdapter("android:setHTMLText")
        @JvmStatic
        fun setHTMLText(textView: TextView, overView: String) {
            val htmlAsSpanned = Html.fromHtml(overView)
            textView.text = htmlAsSpanned
        }

        @BindingAdapter("android:setLikes")
        @JvmStatic
        fun setLikes(textView: TextView, likes: Int) {
            textView.text = likes.toString()
        }

        @BindingAdapter("android:setMinutes")
        @JvmStatic
        fun setMinutes(textView: TextView, minutes: Int) {
            textView.text = minutes.toString()
        }

        @BindingAdapter("android:setVegan")
        @JvmStatic
        fun setVegan(textView: TextView, vegan: Boolean) {
            if (vegan) {
                textView.setTextColor(
                    ContextCompat.getColor(
                        textView.context,
                        R.color.green
                    )
                )
                if (textView.compoundDrawables[1] != null)
                    textView.compoundDrawables[1].setTint(
                        ContextCompat.getColor(
                            textView.context,
                            R.color.green
                        )
                    )
            }
        }
    }
}