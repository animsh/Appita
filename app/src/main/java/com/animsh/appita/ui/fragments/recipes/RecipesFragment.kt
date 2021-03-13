package com.animsh.appita.ui.fragments.recipes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.animsh.appita.R
import com.animsh.appita.bindingadapters.RecipesAdapter
import com.animsh.appita.databinding.FragmentRecipesBinding
import com.animsh.appita.ui.fragments.recipes.bottomsheet.RecipeBottomSheetFragment
import com.animsh.appita.util.NetworkResult
import com.animsh.appita.util.observeOnce
import com.animsh.appita.viewmodels.MainViewModel
import com.animsh.appita.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private lateinit var binding: FragmentRecipesBinding
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipesBinding.bind(view)
        binding.lifecycleOwner = this
        binding.apply {
            mainViewModel =
                ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
            recipesViewModel =
                ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)

            floatingActionButton.setOnClickListener {
                val openBottomSheet: RecipeBottomSheetFragment =
                    RecipeBottomSheetFragment().newInstance()
                openBottomSheet.show(childFragmentManager, RecipeBottomSheetFragment.TAG)
            }

            binding.viewModel = mainViewModel
            recipeRecyclerview.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            recipeRecyclerview.adapter = mAdapter
            recipeRecyclerview.showShimmer()
            readDatabase()
        }
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipe.observeOnce(viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    mAdapter.setData(it[0].foodRecipe)
                    binding.recipeRecyclerview.hideShimmer()
                } else {
                    requestData()
                }
            })
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipe.observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    mAdapter.setData(it[0].foodRecipe)
                }
            })
        }
    }

    private fun requestData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.foodRecipeResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.recipeRecyclerview.hideShimmer()
                    response.data.let { mAdapter.setData(it!!) }
                }
                is NetworkResult.Error -> {
                    binding.recipeRecyclerview.hideShimmer()
                    loadDataFromCache()
                    Toast.makeText(context, response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.recipeRecyclerview.showShimmer()
                }
            }
        })
    }
}