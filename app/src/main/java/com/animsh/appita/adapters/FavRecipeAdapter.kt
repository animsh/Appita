package com.animsh.appita.adapters

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.animsh.appita.R
import com.animsh.appita.data.database.entity.FavoriteEntity
import com.animsh.appita.databinding.LayoutFavRecipeContainerBinding
import com.animsh.appita.ui.DetailsActivity
import com.animsh.appita.util.RecipesDiffUtil
import com.animsh.appita.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_fav_recipe_container.view.*


/**
 * Created by animsh on 5/1/2021.
 */
class FavRecipeAdapter(
    private var activity: Activity,
    private var mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavRecipeAdapter.FavRecipeViewHolder>(), ActionMode.Callback {

    private var multiSelection = false
    private lateinit var dActionMode: ActionMode
    private var selectedRecipes = arrayListOf<FavoriteEntity>()
    private var myHolders = arrayListOf<FavRecipeViewHolder>()
    private lateinit var rootView: View

    private var favRecipeList = emptyList<FavoriteEntity>()

    class FavRecipeViewHolder(private val binding: LayoutFavRecipeContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteEntity: FavoriteEntity) {
            binding.result = favoriteEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FavRecipeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutFavRecipeContainerBinding.inflate(layoutInflater, parent, false)
                return FavRecipeViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavRecipeViewHolder {
        return FavRecipeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavRecipeViewHolder, position: Int) {
        rootView = holder.itemView.rootView
        myHolders.add(holder)
        val current = favRecipeList[position]
        holder.bind(current)
        holder.itemView.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, current)
            } else {
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra("result", current.result)
                activity.startActivity(intent)
            }
        }
        holder.itemView.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                activity.startActionMode(this)
                setupAppBar(0, 0)
                applySelection(holder, current)
                true
            } else {
                false
            }

        }
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.fav_contextual_menu, menu)
        dActionMode = actionMode!!
        when (activity.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                applyStatusColor(R.color.backgroundColorDark)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                applyStatusColor(R.color.backgroundColorLight)
            }
        }
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_fav_recipe) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavRecipe(it)
            }
            showSnackBarMessage("${selectedRecipes.size} Recipe/s deleted!!")
            selectedRecipes.clear()
            multiSelection = false
            dActionMode.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myHolders.forEach { holder ->
            setColorAsPerMode(holder)
        }
        multiSelection = false
        selectedRecipes.clear()
        setupAppBar(dpToPx(60), dpToPx(28))
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun applySelection(holder: FavRecipeViewHolder, currentRecipe: FavoriteEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            setColorAsPerMode(holder)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.lightThemePrimary, R.color.themePrimary)
            applyActionModeTitle()
        }
    }

    private fun setupAppBar(height: Int, marginTop: Int) {
        activity.appBar.layoutParams.height = height
        val margin = activity.appBar.layoutParams as ViewGroup.MarginLayoutParams
        margin.topMargin = marginTop
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> dActionMode.finish()
            1 -> dActionMode.title = "${selectedRecipes.size} item selected"
            else -> dActionMode.title = "${selectedRecipes.size} items selected"
        }
    }

    private fun changeRecipeStyle(
        holder: FavRecipeViewHolder,
        backGroundColor: Int,
        strokeColor: Int
    ) {
        holder.itemView.layoutContainerChild.setBackgroundColor(
            ContextCompat.getColor(
                activity,
                backGroundColor
            )
        )

        holder.itemView.layoutContainer.strokeWidth = 1
        holder.itemView.layoutContainer.strokeColor = ContextCompat.getColor(
            activity,
            strokeColor
        )
    }

    private fun setColorAsPerMode(holder: FavRecipeViewHolder) {
        when (activity.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                changeRecipeStyle(
                    holder,
                    R.color.backgroundLayoutColorDark,
                    R.color.backgroundLayoutColorDark
                )
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                changeRecipeStyle(
                    holder,
                    R.color.backgroundLayoutColorLight,
                    R.color.backgroundLayoutColorLight
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return favRecipeList.size
    }

    fun setData(newFavRecipeList: List<FavoriteEntity>) {
        val favRecipesDiffUtils = RecipesDiffUtil(favRecipeList, newFavRecipeList)
        val diffUtilResult = DiffUtil.calculateDiff(favRecipesDiffUtils)
        favRecipeList = newFavRecipeList
        diffUtilResult.dispatchUpdatesTo(this)

    }

    private fun applyStatusColor(color: Int) {
        activity.window.statusBarColor = ContextCompat.getColor(activity, color)
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    fun removeContextualActionMode() {
        if (this::dActionMode.isInitialized) {
            dActionMode.finish()
        }
    }
}