package com.example.hishab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.work.*
import com.example.hishab.utils.BottomNavigationViewWithViewPagerManager
import com.example.hishab.workManager.ApiCallerWorker
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.activity_main_bottom_navigation_view)
        BottomNavigationViewWithViewPagerManager.create(navHostFragment, bottomNavigationView)
        testWorkManager();
    }

    fun cancelJob() {
        WorkManager.getInstance(this).cancelAllWork()
    }


    private fun testWorkManager() {
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        var request =
            OneTimeWorkRequest.Builder(ApiCallerWorker::class.java).setConstraints(constraints)
                .build()
        WorkManager.getInstance(this)
            .beginUniqueWork("RemoteUpdateWork", ExistingWorkPolicy.REPLACE, request).enqueue()
    }


}