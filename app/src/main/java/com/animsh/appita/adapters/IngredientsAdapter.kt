package com.animsh.appita.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.animsh.appita.databinding.LayoutIngredientsBinding
import com.animsh.appita.models.ExtendedIngredient
import com.animsh.appita.util.RecipesDiffUtil
import kotlinx.android.parcel.RawValue
import kotlinx.android.synthetic.main.layout_ingredients.view.*

/**
 * Created by animsh on 2/27/2021.
 */
class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    private var ingredients = emptyList<ExtendedIngredient>()

    class IngredientsViewHolder(private val binding: LayoutIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ExtendedIngredient) {
            binding.ingredient = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): IngredientsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutIngredientsBinding.inflate(layoutInflater, parent, false)
                return IngredientsViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientsViewHolder {
        return IngredientsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        val current = ingredients[position]
        holder.bind(current)
        holder.itemView.name.isSelected = true
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun setData(newData: @RawValue List<ExtendedIngredient?>?) {
        val recipesDiffUtil = RecipesDiffUtil(ingredients, newData!!)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        ingredients = (newData as List<ExtendedIngredient>?)!!
        diffUtilResult.dispatchUpdatesTo(this)
    }

}