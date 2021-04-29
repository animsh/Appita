package com.animsh.appita.ui

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.animsh.appita.adapters.IngredientsAdapter
import com.animsh.appita.adapters.StepsAdapter
import com.animsh.appita.databinding.ActivityDetailsBinding
import com.animsh.appita.models.Result
import com.animsh.appita.models.Step
import kotlinx.android.parcel.RawValue
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private val iAdapter by lazy { IngredientsAdapter() }
    private val sAdapter by lazy { StepsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val result: Result? = intent.extras!!.getParcelable("result")
        binding.result = result
        binding.apply {
            window.apply {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                statusBarColor = Color.TRANSPARENT
            }

            ingredientsRecyclerview.layoutManager =
                LinearLayoutManager(this@DetailsActivity, LinearLayoutManager.HORIZONTAL, false)
            ingredientsRecyclerview.adapter = iAdapter
            iAdapter.setData(result?.extendedIngredients)

            stepsRecyclerView.layoutManager =
                LinearLayoutManager(this@DetailsActivity, LinearLayoutManager.VERTICAL, false)
            stepsRecyclerView.adapter = sAdapter
            sAdapter.setData(result?.analyzedInstructions?.get(0)?.steps!! as @RawValue List<Step>)
        }
    }
}