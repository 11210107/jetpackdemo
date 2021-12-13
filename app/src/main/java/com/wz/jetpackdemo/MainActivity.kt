package com.wz.jetpackdemo

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.permissionx.guolindev.PermissionX
import com.wz.jetpackdemo.databinding.MainActivityBinding
import com.wz.jetpackdemo.ui.BaseViewBindingActivity

class MainActivity : BaseViewBindingActivity<MainActivityBinding>() {

    val TAG = MainActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate")
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        setUpBottom(navHostFragment.navController)
        PermissionX.init(this)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "All permissions are granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                }
            }
        val marsWidgetProvider = MarsWidgetProvider()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.wz.jetpackdemo.action")
        intentFilter.addAction("android.appwidget.action.APPWIDGET_UPDATE")
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

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e(TAG, "onNewIntent,time=${intent?.getLongExtra("time", 0L)}")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy")
    }

}