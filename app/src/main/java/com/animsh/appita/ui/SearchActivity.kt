package com.animsh.appita.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.animsh.appita.bindingadapters.RecipesAdapter
import com.animsh.appita.databinding.ActivitySearchBinding
import com.animsh.appita.util.NetworkResult
import com.animsh.appita.viewmodels.MainViewModel
import com.animsh.appita.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_search.*

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)

        recipeRecyclerview.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        recipeRecyclerview.adapter = mAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                searchApiData(query)
                return true
            }
        })
    }

    private fun searchApiData(searchQuery: String) {
        binding.recipeRecyclerview.showShimmer()
        mainViewModel.searchRecipes(recipesViewModel.applySearch(searchQuery))
        mainViewModel.searchRecipeResponse.observe(this, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.recipeRecyclerview.hideShimmer()
                    response.data.let { mAdapter.setData(it!!) }
                }
                is NetworkResult.Error -> {
                    binding.recipeRecyclerview.hideShimmer()
                    Toast.makeText(this, response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.recipeRecyclerview.showShimmer()
                }
            }
        })
    }
}