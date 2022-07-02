package com.example.currencyconverter.presentation.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging

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

        if (checkGooglePlayServices()) {
            logFirebaseIdentifiers()
        } else {
            Log.w(TAG, "Device doesn't have google play services")
        }
    }

    private fun display(msg: String): String {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        return msg
    }

    private fun initialNetworkCheck() {
        if (!networkStatusHelper.isInternetAvailable()) display("Network Unavailable")
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