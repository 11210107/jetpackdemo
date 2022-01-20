package com.wz.jetpackdemo.ui.main

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.rjhy.app.pk.bean.PkResultProto
import com.wz.jetpackdemo.R
import com.wz.jetpackdemo.databinding.HomeFragmentBinding
import com.wz.jetpackdemo.lifecycle.observer.HomeLifecycleObserver
import com.wz.jetpackdemo.model.User
import com.wz.jetpackdemo.repository.UserRepository
import java.io.*
import java.nio.charset.Charset

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
        UserRepository.sUserId = 2
//        viewModel.userLiveData.value = User("onCreate", 0)
        Log.e(TAG, "sUserId:${UserRepository.sUserId}")

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvUserinfo.setOnClickListener {
            viewModel.getUserInfo()
//            val intent = Intent(activity, MainActivity::class.java)
//            intent.putExtra("time", System.currentTimeMillis())
//            startActivity(intent)
            val user = User("wangzhen", 28)
            val fileOutputStream = FileOutputStream(activity?.getExternalFilesDir(null)?.absolutePath +File.separator + "cache.txt")
            val objectOutputStream =
                ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(user)
            objectOutputStream.close()
            val objectInputStream =
                ObjectInputStream(FileInputStream(activity?.getExternalFilesDir(null)?.absolutePath + File.separator + "cache.txt"))
            val wangzhen = objectInputStream.readObject() as User
            objectInputStream.close()
            Log.e(TAG, "wangzhen:$wangzhen isSame:${wangzhen === user}")
            val newBuilder = PkResultProto.Pk.newBuilder()
            newBuilder.pkName = "dddd"
            newBuilder.ppName = "eeee"
            newBuilder.build()
            val build = PkResultProto.PkResultBean.newBuilder().addAppIds(newBuilder).build()
            val byteArray = build.toByteArray()
            val string = String(byteArray, Charsets.UTF_8)

        }
        binding.message.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key", "value")
            Navigation.findNavController(it)
                .navigate(R.id.action_fragment_home_to_fragment_mine, bundle)
        }
        binding.editQuery.setText(savedInstanceState?.getString("extra_edittext"))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("extra_edittext",binding.editQuery.text.toString())
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