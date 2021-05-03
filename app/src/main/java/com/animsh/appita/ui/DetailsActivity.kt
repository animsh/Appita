package com.animsh.appita.ui

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.animsh.appita.R
import com.animsh.appita.adapters.IngredientsAdapter
import com.animsh.appita.adapters.StepsAdapter
import com.animsh.appita.data.database.entity.FavoriteEntity
import com.animsh.appita.databinding.ActivityDetailsBinding
import com.animsh.appita.models.Result
import com.animsh.appita.models.Step
import com.animsh.appita.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.RawValue


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private val iAdapter by lazy { IngredientsAdapter() }
    private val sAdapter by lazy { StepsAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipe: Result
    private var recipeSaved: Boolean = false
    private var savedRecipeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recipe = intent.extras!!.getParcelable("result")!!
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.result = recipe
        binding.apply {
            window.apply {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                statusBarColor = Color.TRANSPARENT
            }

            ingredientsRecyclerview.layoutManager =
                LinearLayoutManager(this@DetailsActivity, LinearLayoutManager.HORIZONTAL, false)
            ingredientsRecyclerview.adapter = iAdapter
            iAdapter.setData(recipe.extendedIngredients)

            stepsRecyclerView.layoutManager =
                LinearLayoutManager(this@DetailsActivity, LinearLayoutManager.VERTICAL, false)
            stepsRecyclerView.adapter = sAdapter
            sAdapter.setData(recipe.analyzedInstructions?.get(0)?.steps as @RawValue List<Step>)

            addToFavBtn.setOnClickListener {
                if (recipeSaved) {
                    removeFromFav(favIcon)
                } else {
                    saveToFav(favIcon)
                }
            }
            checkRecipeFavStatus()
        }
    }

    private fun saveToFav(icon: ImageView) {
        val favoriteEntity = FavoriteEntity(0, recipe)
        mainViewModel.insertFavRecipe(favoriteEntity)
        updateIcon(icon, R.color.red)
        showMessage("Recipe Saved!!")
        recipeSaved = true
    }

    private fun removeFromFav(icon: ImageView) {
        val favoriteEntity = FavoriteEntity(savedRecipeId, recipe)
        mainViewModel.deleteFavRecipe(favoriteEntity)
        when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                updateIcon(icon, R.color.blurIconTintColorDark)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                updateIcon(icon, R.color.blurIconTintColorLight)
            }
        }
        showMessage("Recipe Removed!!")
        recipeSaved = false
    }

    private fun checkRecipeFavStatus() {
        mainViewModel.readFavRecipe.observe(this, { favoriteEntity ->
            try {
                for (savedRecipe in favoriteEntity) {
                    if (savedRecipe.result.id == recipe.id) {
                        Log.d("TAGTAGTAG", "checkRecipeFavStatus: inside if")
                        updateIcon(binding.favIcon, R.color.red)
                        recipeSaved = true
                        savedRecipeId = savedRecipe.id
                    }
                }
            } catch (e: Exception) {
                Log.d("TAGTAGTAG", "checkRecipeFavStatus: " + e.message)
            }
        })
    }

    private fun showMessage(message: String) {
        Snackbar.make(
            binding.detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    private fun updateIcon(icon: ImageView, color: Int) {
        icon.setColorFilter(ContextCompat.getColor(this, color))
    }
}