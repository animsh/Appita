package com.animsh.appita.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.animsh.appita.R
import com.animsh.appita.databinding.ActivitySettingBinding


class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            themeLayout.setOnClickListener {
                showAlertDialog()
            }

            val sharedPreference =
                binding.root.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            when (sharedPreference.getString("theme", "system default")) {
                "light" -> theme.text = "Light"
                "dark" -> theme.text = "Dark"
                "system default" -> theme.text = "System Default"
            }

            aboutLayout.setOnClickListener {
                val openBottomSheet: AboutBottomSheetFragment =
                    AboutBottomSheetFragment().newInstance()
                openBottomSheet.show(supportFragmentManager, AboutBottomSheetFragment.TAG)
            }
            val manager = packageManager
            val info = manager?.getPackageInfo(
                packageName, 0
            )
            val versionName = info?.versionName
            val versionNumber = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info?.longVersionCode
            } else {
                info?.versionCode
            }

            val appVersionTxt = "Appita : $versionName ($versionNumber)"
            appVersion.text = appVersionTxt
        }
    }

    private fun showAlertDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(
            this,
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
        ).setTitle("Theme")
        val items = arrayOf("Light", "Dark", "System Default")
        val sharedPreference =
            binding.root.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        var checkedItem = 2
        when (sharedPreference.getString("theme", "system default")) {
            "light" -> checkedItem = 0
            "dark" -> checkedItem = 1
            "system default" -> checkedItem = 2
        }
        alertDialog.setSingleChoiceItems(
            items, checkedItem
        ) { dialog, which ->
            when (which) {
                0 -> {
                    dialog.dismiss()
                    editor.putString("theme", "light")
                    editor.apply()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                1 -> {
                    dialog.dismiss()
                    editor.putString("theme", "dark")
                    editor.apply()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                2 -> {
                    dialog.dismiss()
                    editor.putString("theme", "system default")
                    editor.apply()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
        val alert: AlertDialog = alertDialog.create()
        alert.show()
    }
}