package com.wz.jetpackdemo.ui.main

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.util.concurrent.locks.Lock

class HomeFragment : BaseViewBindingFragment<HomeFragmentBinding>() {
    val TAG = HomeFragment::class.java.simpleName
    val args: HomeFragmentArgs by navArgs()

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e(TAG, "onAttach")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate")
        lifecycle.addObserver(HomeLifecycleObserver())
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        UserRepository.sUserId = 2
//        viewModel.userLiveData.value = User("onCreate", 0)
        Log.e(TAG, "sUserId:${UserRepository.sUserId}")

    }
    val sumLambda = {a:Int,b:Int-> a + b}
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated")
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
            val sum = "abc".sumBy {
                it.toInt()
            }
            Log.e(TAG,"sum:$sum")
            val result1 = resultByOpt(1,2){
                num1,num2 -> num1 + num2
            }
            Log.e(TAG,"result1:$result1")
            val result2 = resultByOpt(3,4){
                num1,num2 -> num1 - num2
            }
            Log.e(TAG,"result2:$result2")
            val result3 = resultByOpt(5,6){
                num1,num2 -> num1 * num2
            }
            Log.e(TAG,"result3:$result3")
            val result4 = resultByOpt(7,8,{
                    num1,num2 -> num1 / num2
            })
            Log.e(TAG,"result4:$result4")
            val str = "kotlin"
            val run = str.run {
                Log.e(TAG, "length:$length")
                Log.e(TAG, "first:${first()}")
                Log.e(TAG, "last:${last()}")
            }
            Log.e(TAG,"run $run")
            val with = with(str) {
                Log.e(TAG,"with length:$length")
                Log.e(TAG,"with first:${first()}")
                Log.e(TAG,"with last:${last()}")
            }
            Log.e(TAG,"with $with")
            val apply = str.apply {
                Log.e(TAG,"apply length:$length")
                Log.e(TAG,"apply ${this.plus("-java")}")
            }
            Log.e(TAG,"apply $apply")
            str.also {
                Log.e(TAG,"also ${it.plus("-php")}")
            }
            Log.e(TAG,"subLambda:${sumLambda(1, 2)}")

        }
        binding.message.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key", "value")
            Navigation.findNavController(it)
                .navigate(R.id.action_fragment_home_to_fragment_mine, bundle)
        }
        binding.editQuery.setText(savedInstanceState?.getString("extra_edittext"))
    }
    public fun CharSequence.sumBy(selector:(Char)->Int):Int{
        var sum:Int = 0
        for (char in this) {
            sum += selector(char)
        }
        return sum
    }


    fun <T> lock(lock:Lock,body:()->T):T{
        lock.lock()
        try {
            return body()
        }finally {
            lock.unlock()
        }
    }

    fun resultByOpt(num1:Int,num2:Int,result:(Int,Int) -> Int):Int{
        return result(num1,num2)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("extra_edittext",binding.editQuery.text.toString())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG, "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
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