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

class HomeFragment : Fragment() {
    val args: HomeFragmentArgs by navArgs()
    lateinit var userinfo: TextView

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

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
        Log.e("MainFragment:", "token:${args.token} id:${args.id}")
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            userinfo.text = it.toString()
        })
    }

}