package com.animsh.appita.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.animsh.appita.R
import com.animsh.appita.util.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_recipes.view.*

class RecipesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipes, container, false)
        view.recipeRecyclerview.addItemDecoration(GridSpacingItemDecoration(2, 10, true))
        view.recipeRecyclerview.showShimmer()
        return view
    }
}