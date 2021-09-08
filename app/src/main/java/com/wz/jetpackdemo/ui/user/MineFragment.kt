package com.wz.jetpackdemo.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.wz.jetpackdemo.R

class MineFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.mine_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tv_login_register).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_fragment_mine_to_fragment_main)
        }

        view.findViewById<TextView>(R.id.tv_login_main).setOnClickListener {
            val action =
                MineFragmentDirections.actionFragmentMineToFragmentHome("token", 10001)
            Navigation.findNavController(it).navigate(action)
        }
    }
}