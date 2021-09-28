package com.wz.jetpackdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wz.jetpackdemo.databinding.MainActivityBinding
import com.wz.jetpackdemo.ui.BaseViewBindingActivity

class MainActivity : BaseViewBindingActivity<MainActivityBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        setUpBottom(navHostFragment.navController)
    }
    private fun setUpBottom(navController: NavController) {
        val bottomNav = binding.bottomNavigation
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

    override fun getViewBinding()= MainActivityBinding.inflate(layoutInflater)


}