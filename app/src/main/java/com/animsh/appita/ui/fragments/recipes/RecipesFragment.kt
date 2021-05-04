package com.animsh.appita.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.animsh.appita.R
import com.animsh.appita.adapters.RandomRecipesAdapter
import com.animsh.appita.adapters.RecipesAdapter
import com.animsh.appita.databinding.FragmentRecipesBinding
import com.animsh.appita.ui.fragments.recipes.bottomsheet.RecipeBottomSheetFragment
import com.animsh.appita.util.NetworkListener
import com.animsh.appita.util.NetworkResult
import com.animsh.appita.util.observeOnce
import com.animsh.appita.viewmodels.MainViewModel
import com.animsh.appita.viewmodels.RecipesViewModel
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private lateinit var binding: FragmentRecipesBinding
    private val mAdapter by lazy { RecipesAdapter(requireActivity()) }
    private val rAdapter by lazy { RandomRecipesAdapter(requireActivity()) }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var networkListener: NetworkListener
    private var notFromBottomSheet: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        binding = FragmentRecipesBinding.bind(view)
        binding.lifecycleOwner = this
        binding.apply {
            mainViewModel =
                ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
            recipesViewModel =
                ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)

            floatingActionButton.setOnClickListener {
                if (recipesViewModel.networkStatus) {
                    val openBottomSheet: RecipeBottomSheetFragment =
                        RecipeBottomSheetFragment().newInstance()
                    openBottomSheet.show(childFragmentManager, RecipeBottomSheetFragment.TAG)
                } else {
                    Toast.makeText(requireContext(), "No Internet Connection!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            binding.viewModel = mainViewModel
            recipeRecyclerview.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            recipeRecyclerview.adapter = mAdapter
            recipeRecyclerview.showShimmer()

            recipesViewModel.readBackOnline.observe(viewLifecycleOwner, {
                recipesViewModel.backOnline = it
                readDatabase(notFromBottomSheet)
            })

            lifecycleScope.launchWhenStarted {
                networkListener = NetworkListener()
                networkListener.checkNetworkAvailability(requireContext())
                    .collect { status ->
                        Log.d("TAGTAGTAG", "onViewCreated: $status")
                        recipesViewModel.networkStatus = status
                        recipesViewModel.showNetworkStatus()
                        recipesViewModel.setOnlineStatus(status)
                    }
            }

            recipesViewModel.onlineStatus.observe(viewLifecycleOwner, {
                recipesViewModel.backFrom.observe(viewLifecycleOwner, { result ->
                    notFromBottomSheet = result
                })
            })

            randomRecipes.adapter = rAdapter
            randomRecipes.clipToPadding = false
            randomRecipes.clipChildren = false
            randomRecipes.offscreenPageLimit = 3
            randomRecipes.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS

            /*val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(40))
            compositePageTransformer.addTransformer { page, position ->
                val r: Float = 1 - abs(position)
                page.scaleY = (0.85 + r * 0.15f).toFloat()
            }
            randomRecipes.setPageTransformer(compositePageTransformer)*/
        }
    }

    private fun readDatabase(backFromBottomSheet: Boolean) {
        lifecycleScope.launch {
            mainViewModel.readRecipe.observeOnce(viewLifecycleOwner, {
                if (it.isNotEmpty() && !backFromBottomSheet) {
                    mAdapter.setData(it[0].foodRecipe)
                    binding.recipeRecyclerview.hideShimmer()
                } else {
                    requestData()
                }
                requestRandomData()
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
        mainViewModel.getRecipes(recipesViewModel.applyQueries(requireContext(), "20"))
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

    private fun requestRandomData() {
        mainViewModel.getRandomRecipes(recipesViewModel.applyQueries(requireContext(), "5"))
        mainViewModel.randomRecipeResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data.let {
                        rAdapter.setData(it!!)
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(context, response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                }
            }
        })
    }
}