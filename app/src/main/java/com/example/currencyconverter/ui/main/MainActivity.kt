package com.example.currencyconverter.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var networkStatusHelper: NetworkStatusHelper
    private lateinit var binding: ActivityMainBinding
    val toolbar by lazy { binding.toolbar }
    val bottomNav by lazy { binding.bottomNav }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkStatusHelper = NetworkStatusHelper(this@MainActivity)
        binding = ActivityMainBinding.inflate(layoutInflater)
        initialNetworkCheck()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)

        networkStatusHelper.observe(this) {
            when (it) {
                NetworkStatus.Available -> display("Network Available")
                NetworkStatus.Unavailable -> display("Network Unavailable")
            }
        }
    }

    private fun display(msg: String): String {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        return msg
    }

    private fun initialNetworkCheck() {
        if (!networkStatusHelper.isInternetAvailable()) display("Network Unavailable")
    }
}