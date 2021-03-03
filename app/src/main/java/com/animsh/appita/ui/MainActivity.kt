package com.animsh.appita.ui

import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.animsh.appita.R
import com.animsh.appita.databinding.ActivityMainBinding
import com.animsh.appita.ui.fragments.favorite.FavRecipesFragment
import com.animsh.appita.ui.fragments.foodjoke.FoodJokeFragment
import com.animsh.appita.ui.fragments.recipes.RecipesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val recipesFragment: Fragment = RecipesFragment()
    private val favRecipesFragment: Fragment = FavRecipesFragment()
    private val foodJokeFragment: Fragment = FoodJokeFragment()
    private val fragmentManager: FragmentManager = supportFragmentManager
    private var active = recipesFragment
    private var backStack: MutableList<Fragment> = Stack()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val tf = Typeface.createFromAsset(assets, "fonts/billabong.ttf")
            appName.typeface = tf
            appName.textSize = 34f
            backStack.add(recipesFragment)
            bottomNavigationView.apply {
                setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
            }
            if (fragmentManager.findFragmentByTag("1") == null) {
                fragmentManager.beginTransaction().add(R.id.main_container, foodJokeFragment, "2")
                    .hide(foodJokeFragment).commit()
                fragmentManager.beginTransaction().add(R.id.main_container, favRecipesFragment, "1")
                    .hide(favRecipesFragment).commit()
                fragmentManager.beginTransaction().add(R.id.main_container, recipesFragment, "0")
                    .commit()
            }
        }
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.recipesFragment -> {
                    fragmentManager.beginTransaction().hide(active).show(recipesFragment).commit()
                    active = recipesFragment
                    if (backStack[backStack.lastIndex] != active)
                        backStack.add(active)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.favRecipesFragment -> {
                    fragmentManager.beginTransaction().hide(active).show(favRecipesFragment)
                        .commit()
                    active = favRecipesFragment
                    if (backStack[backStack.lastIndex] != active)
                        backStack.add(active)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.foodJokeFragment -> {
                    fragmentManager.beginTransaction().hide(active).show(foodJokeFragment).commit()
                    active = foodJokeFragment
                    if (backStack[backStack.lastIndex] != active)
                        backStack.add(active)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onBackPressed() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
            fragmentManager.beginTransaction().hide(active).show(backStack[backStack.lastIndex])
                .commit()
            active = backStack[backStack.lastIndex]
            binding.bottomNavigationView.menu.getItem(active.tag!!.toInt()).isChecked = true
        } else {
            super.onBackPressed()
        }
    }
}