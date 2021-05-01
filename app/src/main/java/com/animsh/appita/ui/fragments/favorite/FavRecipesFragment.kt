package com.animsh.appita.ui.fragments.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.animsh.appita.adapters.FavRecipeAdapter
import com.animsh.appita.databinding.FragmentFavRecipesBinding
import com.animsh.appita.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavRecipesFragment : Fragment() {

    private val fAdapter: FavRecipeAdapter by lazy { FavRecipeAdapter(requireActivity()) }
    private val mainViewModel: MainViewModel by viewModels()

    private var _binding: FragmentFavRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavRecipesBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@FavRecipesFragment
            viewModel = mainViewModel
            adapter = fAdapter
            favRecipesRecyclerView.adapter = fAdapter
            favRecipesRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


            mainViewModel.readFavRecipe.observe(viewLifecycleOwner, { favEntity ->
                Log.d("TAGTAGTAG", "onViewCreated: " + favEntity.size)
                fAdapter.setData(favEntity)
            })
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}