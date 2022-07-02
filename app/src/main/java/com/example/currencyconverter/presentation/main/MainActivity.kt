package com.example.currencyconverter.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.example.currencyconverter.presentation.converter.CurrencyViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var networkStatusHelper: NetworkStatusHelper
    private val currencyViewModel: CurrencyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setupNetworkStatusHelper()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupNavigation()
        setupObservers()
        initialNetworkCheck()

        if (checkGooglePlayServices()) {
            logFirebaseIdentifiers()
        } else {
            Log.w(TAG, "Device doesn't have google play services")
        }
    }

    private fun setupNetworkStatusHelper() {
        networkStatusHelper = NetworkStatusHelper(this@MainActivity)
        networkStatusHelper.observe(this) {
            when (it) {
                NetworkStatus.Available -> displayToast("Network Available")
                NetworkStatus.Unavailable -> displayToast("Network Unavailable")
            }
        }
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)
    }

    private fun setupObservers() {
        currencyViewModel.isItemSelected.observe(this) { itemSelected ->
            if (itemSelected) {
                changeLayout(R.color.hint, R.string.currencies_list_item_selected, View.GONE)
            } else {
                changeLayout(R.color.primaryColor, R.string.currency, View.VISIBLE)
            }
        }
    }

    private fun changeLayout(colorId: Int, titleId: Int, bottomNavVisibility: Int) {
        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, colorId))
        binding.toolbar.title = getString(titleId)
        binding.bottomNav.visibility = bottomNavVisibility
    }

    private fun displayToast(msg: String): String {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        return msg
    }

    private fun initialNetworkCheck() {
        if (!networkStatusHelper.isInternetAvailable()) displayToast("Network Unavailable")
    }

    private fun checkGooglePlayServices(): Boolean {
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        return if (status != ConnectionResult.SUCCESS) {
            Log.e(TAG, "Error: Google Play Services is not Available")
            false
        } else {
            Log.e(TAG, "Google Play Services is updated")
            true
        }
    }

    private fun logFirebaseIdentifiers() {
        logTokenFirebase()
        logInstallationIdFirebase()
    }

    private fun logTokenFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { regTokenTask ->
            if (regTokenTask.isSuccessful) {
                Log.d(TAG, "FCM registration token: ${regTokenTask.result}")
            } else {
                Log.d(TAG, "Unable to retrieve registration token", regTokenTask.exception)
            }
        }
    }

    private fun logInstallationIdFirebase() {
        FirebaseInstallations.getInstance().id.addOnCompleteListener { installationIdTask ->
            if (installationIdTask.isSuccessful) {
                Log.d(TAG, "Firebase Installation Id: ${installationIdTask.result}")
            } else {
                Log.d(TAG, "Unable to retrieve installation ID", installationIdTask.exception)
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}