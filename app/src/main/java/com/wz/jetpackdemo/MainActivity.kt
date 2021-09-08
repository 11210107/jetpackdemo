package com.wz.jetpackdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, MainFragment.newInstance())
//                    .commitNow()
//        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        setUpBottom(navHostFragment.navController)
    }

    private fun setUpBottom(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)
        bottomNav.setOnNavigationItemSelectedListener {item: MenuItem ->
            val options = NavOptions.Builder()
                .setPopUpTo(navController.currentDestination!!.id,true)
                .setLaunchSingleTop(true)
                .build()
            try {
                navController.navigate(item.itemId,null,options)
                return@setOnNavigationItemSelectedListener true
            } catch (e: Exception) {
                return@setOnNavigationItemSelectedListener  false
            }
        }
    }


}