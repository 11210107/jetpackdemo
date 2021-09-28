package com.wz.jetpackdemo.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.wz.jetpackdemo.R
import com.wz.jetpackdemo.databinding.HomeFragmentBinding
import com.wz.jetpackdemo.lifecycle.observer.HomeLifecycleObserver

class HomeFragment : BaseViewBindingFragment<HomeFragmentBinding>() {
    val TAG = HomeFragment::class.java.simpleName
    val args: HomeFragmentArgs by navArgs()

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate")
        lifecycle.addObserver(HomeLifecycleObserver())
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//        viewModel.userLiveData.value = User("onCreate", 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvUserinfo.setOnClickListener {
            viewModel.getUserInfo()
        }
        binding.message.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key", "value")
            Navigation.findNavController(it)
                .navigate(R.id.action_fragment_home_to_fragment_mine, bundle)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart")
//        viewModel.userLiveData.value = User("onStart", 1)
    }
    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume token:${args.token} id:${args.id}")
//        viewModel.userLiveData.value = User("onResume", 2)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            Log.e(TAG,"userLiveData:$it")
            binding.tvUserinfo.text = it.toString()
        })
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause")
//        viewModel.userLiveData.value = User("onPause", 3)
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop")
//        viewModel.userLiveData.value = User("onStop", 4)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy")
//        viewModel.userLiveData.value = User("onDestroy", 5)
    }
}