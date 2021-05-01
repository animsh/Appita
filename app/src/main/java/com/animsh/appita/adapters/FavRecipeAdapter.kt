package com.animsh.appita.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.animsh.appita.data.database.entity.FavoriteEntity
import com.animsh.appita.databinding.LayoutFavRecipeContainerBinding
import com.animsh.appita.ui.DetailsActivity
import com.animsh.appita.util.RecipesDiffUtil

/**
 * Created by animsh on 5/1/2021.
 */
class FavRecipeAdapter(
    private var activity: Activity
) : RecyclerView.Adapter<FavRecipeAdapter.FavRecipeViewHolder>() {

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
        val current = favRecipeList[position]
        holder.bind(current)
        holder.itemView.setOnClickListener {
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra("result", current.result)
            activity.startActivity(intent)
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
}