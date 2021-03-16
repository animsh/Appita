package com.animsh.appita.ui.fragments.recipes.bottomsheet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.animsh.appita.databinding.RecipeBottomSheetBinding
import com.animsh.appita.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.animsh.appita.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.animsh.appita.viewmodels.RecipesViewModel
import com.animsh.appita.viewmodels.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*

class RecipeBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "RecipeBottomDialog"
    }

    private val sharedPrefFile = "appitaPreference"

    private var _binding: RecipeBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipesViewModel: RecipesViewModel

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0
    private val sharedViewModel: SharedViewModel by viewModels({ requireParentFragment() })


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

            val sharedPreference =
                binding.root.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()


            recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
                mealTypeChip = value.selectedMealType
                dietTypeChip = value.selectedDietType
                updateChip(value.selectedMealTypeId, mealTypeChipGroup)
                updateChip(value.selectedDietTypeId, dietTypeChipGroup)
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
                editor.putString("meal", mealTypeChip)
                editor.putString("diet", dietTypeChip)
                editor.apply()
                sharedViewModel.setBackFrom(true)
                dismiss()
            }
        }
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet", e.message.toString())
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