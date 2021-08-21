package com.wz.jetpackdemo.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.wz.jetpackdemo.R

class MainFragment : Fragment() {
    val args: MainFragmentArgs by navArgs()

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.message).setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key", "value")
            Navigation.findNavController(it)
                .navigate(R.id.action_fragment_main_to_fragment_login2, bundle)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("MainFragment:", "token:${args.token} id:${args.id}")
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}