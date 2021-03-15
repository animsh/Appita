package com.animsh.appita.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.animsh.appita.databinding.RecipeBottomSheetBinding
import com.animsh.appita.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.animsh.appita.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.animsh.appita.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*

class RecipeBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "RecipeBottomDialog"
    }

    private lateinit var recipesViewModel: RecipesViewModel
    private var _binding: RecipeBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var mealTypeChip: String = DEFAULT_MEAL_TYPE
    private var mealTypeChipId: Int = 0
    private var dietTypeChip: String = DEFAULT_DIET_TYPE
    private var dietTypeChipId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RecipeBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, {
                mealTypeChip = it.selectedMealType
                dietTypeChip = it.selectedDietType
                updateChip(it.selectedMealTypeId, mealTypeChipGroup)
                updateChip(it.selectedDietTypeId, dietTypeChipGroup)
            })

            mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
                val chip = group.findViewById<Chip>(selectedChipId)
                val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)
                mealTypeChip = selectedMealType
                mealTypeChipId = selectedChipId
            }

            dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
                val chip = group.findViewById<Chip>(selectedChipId)
                val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
                dietTypeChip = selectedDietType
                dietTypeChipId = selectedChipId
            }

            applyBtn.setOnClickListener {
                recipesViewModel.saveMealAndDietType(
                    mealTypeChip,
                    mealTypeChipId,
                    dietTypeChip,
                    dietTypeChipId
                )
                dismiss()
            }
        }
    }

    private fun updateChip(selectedId: Int, chipGroup: ChipGroup) {
        if (selectedId != 0) {
            try {
                chipGroup.findViewById<Chip>(selectedId).isChecked = true
            } catch (e: Exception) {
                Log.d(TAG, "updateChip: " + e.message.toString())
            }
        }
    }

    fun newInstance(): RecipeBottomSheetFragment {
        return RecipeBottomSheetFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}