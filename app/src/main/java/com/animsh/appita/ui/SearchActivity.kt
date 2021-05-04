package com.animsh.appita.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.R
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.animsh.appita.adapters.RecipesAdapter
import com.animsh.appita.databinding.ActivitySearchBinding
import com.animsh.appita.util.NetworkResult
import com.animsh.appita.viewmodels.MainViewModel
import com.animsh.appita.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)
        binding.apply {
            recipeRecyclerview.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            recipeRecyclerview.adapter = mAdapter

            searchBtn.setOnClickListener {
                searchView.visibility = View.VISIBLE
                searchView.isIconified = false
                searchBtn.visibility = View.INVISIBLE
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    searchApiData(query)
                    return true
                }
            })

            val clearButton: ImageView = searchView.findViewById(R.id.search_close_btn)
            clearButton.setOnClickListener { v ->
                if (searchView.query.isEmpty()) {
                    searchView.isIconified = true
                    searchView.visibility = View.INVISIBLE
                    searchBtn.visibility = View.VISIBLE
                } else {
                    // Do your task here
                    searchView.setQuery("", false)
                }
            }
        }
    }

    private fun searchApiData(searchQuery: String) {
        binding.recipeRecyclerview.showShimmer()
        mainViewModel.searchRecipes(recipesViewModel.applySearch(searchQuery))
        mainViewModel.searchRecipeResponse.observe(this, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.recipeRecyclerview.hideShimmer()
                    binding.animationView.visibility = View.INVISIBLE
                    binding.subText.visibility = View.INVISIBLE
                    response.data.let { mAdapter.setData(it!!) }
                }
                is NetworkResult.Error -> {
                    binding.recipeRecyclerview.hideShimmer()
                    binding.animationView.visibility = View.VISIBLE
                    binding.subText.visibility = View.VISIBLE
                    Toast.makeText(this, response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.animationView.visibility = View.INVISIBLE
                    binding.subText.visibility = View.INVISIBLE
                    binding.recipeRecyclerview.showShimmer()
                }
            }
        })
    }
}