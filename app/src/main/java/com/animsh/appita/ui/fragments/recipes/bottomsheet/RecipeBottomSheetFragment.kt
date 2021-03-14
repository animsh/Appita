package com.animsh.appita.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.animsh.appita.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RecipeBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "RecipeBottomDialog"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.recipe_bottom_sheet, container, false)
    }

    fun newInstance(): RecipeBottomSheetFragment {
        return RecipeBottomSheetFragment()
    }

}