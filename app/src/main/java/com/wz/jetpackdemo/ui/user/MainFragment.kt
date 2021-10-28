package com.wz.jetpackdemo.ui.user

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wz.jetpackdemo.AIDLService
import com.wz.jetpackdemo.IBookManager
import com.wz.jetpackdemo.R
import com.wz.jetpackdemo.databinding.MainFragmentBinding
import com.wz.jetpackdemo.model.Book
import com.wz.jetpackdemo.ui.main.BaseViewBindingFragment

class MainFragment : BaseViewBindingFragment<MainFragmentBinding>() {

    val TAG = MainFragment::class.java.simpleName
    lateinit var iBookManager: IBookManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvMain.setOnClickListener {
//            Navigation.findNavController(it)
//                .navigate(R.id.action_fragment_register_to_fragment_main)
           val intent = Intent()
           intent.setAction("com.wz.jetpackdemo.b")
           intent.addCategory("com.wz.jetpackdemo.c")
           intent.setDataAndType(Uri.parse("http://abc"),"image/png")
           val packageManager = activity?.packageManager
           val queryIntentActivities =
               packageManager?.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

           val resolveActivity =
               packageManager?.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
           if (resolveActivity != null) {
               startActivity(intent)
           }
        }
        binding.tvBindService.setOnClickListener {
            val intent = Intent(context, AIDLService::class.java)
            context?.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
        binding.tvAddBook.setOnClickListener {
            val book = Book(1, "App Develop Art")
            iBookManager.addBook(book)
        }
        binding.tvGetBook.setOnClickListener {
            val bookList = iBookManager.bookList
            binding.tvBookList.text = bookList.toString()
        }
    }

    private val serviceConnection = object :ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(TAG, "serviceConnected")
            iBookManager = IBookManager.Stub.asInterface(service)
            if (iBookManager != null) {
                val bookList = iBookManager.bookList
                Log.e(TAG, "booklist: $bookList")
            }

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e(TAG, "service disconnected")

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unbindService(serviceConnection)
    }
}