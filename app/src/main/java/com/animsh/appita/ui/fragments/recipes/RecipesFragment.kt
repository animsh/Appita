package com.animsh.appita.ui.fragments.recipes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.animsh.appita.R
import com.animsh.appita.adapters.RecipesAdapter
import com.animsh.appita.databinding.FragmentRecipesBinding
import com.animsh.appita.util.Constants.Companion.API_KEY
import com.animsh.appita.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.animsh.appita.util.Constants.Companion.QUERY_API_KEY
import com.animsh.appita.util.Constants.Companion.QUERY_DIET
import com.animsh.appita.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.animsh.appita.util.Constants.Companion.QUERY_INSTRUCTION_REQUIRED
import com.animsh.appita.util.Constants.Companion.QUERY_NUMBER
import com.animsh.appita.util.Constants.Companion.QUERY_SORT_DIRECTION
import com.animsh.appita.util.Constants.Companion.QUERY_TYPE
import com.animsh.appita.util.NetworkResult
import com.animsh.appita.util.observeOnce
import com.animsh.appita.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private lateinit var binding: FragmentRecipesBinding
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mainViewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipesBinding.bind(view)
        binding.lifecycleOwner = this
        binding.apply {
            mainViewModel =
                ViewModelProvider(this@RecipesFragment).get(MainViewModel::class.java)
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
        mainViewModel.getRecipes(applyQueries())
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

    private fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = "snack"
        queries[QUERY_DIET] = "vegan"
        queries[QUERY_INSTRUCTION_REQUIRED] = "true"
        queries[QUERY_SORT_DIRECTION] = "asc"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
}