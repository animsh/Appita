package com.animsh.appita.ui.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.animsh.appita.R
import com.animsh.appita.adapters.FavRecipeAdapter
import com.animsh.appita.databinding.ActivityMainBinding
import com.animsh.appita.databinding.FragmentFavRecipesBinding
import com.animsh.appita.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavRecipesFragment(
    private val activityMainBinding: ActivityMainBinding
) : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private val fAdapter: FavRecipeAdapter by lazy {
        FavRecipeAdapter(
            requireActivity(),
            activityMainBinding,
            mainViewModel
        )
    }

    private var _binding: FragmentFavRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
        _binding = FragmentFavRecipesBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@FavRecipesFragment
            viewModel = mainViewModel
            adapter = fAdapter
            favRecipesRecyclerView.adapter = fAdapter
            favRecipesRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


            mainViewModel.readFavRecipe.observe(viewLifecycleOwner, { favEntity ->
                fAdapter.setData(favEntity)
            })

            activityMainBinding.deleteBtn.setOnClickListener {
                mainViewModel.deleteAllFavRecipe()
                Snackbar.make(
                    activityMainBinding.appBar,
                    "All Recipes Deleted!",
                    Snackbar.LENGTH_SHORT
                ).setAction("Okay") {}
                    .show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
            fAdapter.removeContextualActionMode()
            activityMainBinding.deleteBtn.visibility = View.GONE
            activityMainBinding.searchBtn.visibility = View.VISIBLE
            activityMainBinding.subText.text = getString(R.string.ready_to_cook_something_tasty)
        } else {
            activityMainBinding.deleteBtn.visibility = View.VISIBLE
            activityMainBinding.searchBtn.visibility = View.INVISIBLE
            activityMainBinding.subText.text = getString(R.string.your_favorite_recipes_are_here)
        }
        super.onHiddenChanged(hidden)
    }
}