package com.wz.jetpackdemo.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.wz.jetpackdemo.R
import com.wz.jetpackdemo.lifecycle.observer.HomeLifecycleObserver

class HomeFragment : Fragment() {
    val TAG = HomeFragment::class.java.simpleName
    val args: HomeFragmentArgs by navArgs()
    lateinit var userinfo: TextView

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate")
        lifecycle.addObserver(HomeLifecycleObserver())
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userinfo = view.findViewById<TextView>(R.id.tv_userinfo)
        userinfo.setOnClickListener {
            viewModel.getUserInfo()
        }
        view.findViewById<TextView>(R.id.message).setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key", "value")
            Navigation.findNavController(it)
                .navigate(R.id.action_fragment_home_to_fragment_mine, bundle)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume token:${args.token} id:${args.id}")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause")
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            userinfo.text = it.toString()
        })
    }

}