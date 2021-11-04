package com.wz.jetpackdemo.ui.user

import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import com.wz.jetpackdemo.AIDLService
import com.wz.jetpackdemo.IBookManager
import com.wz.jetpackdemo.MessengerService
import com.wz.jetpackdemo.databinding.MainFragmentBinding
import com.wz.jetpackdemo.model.Book
import com.wz.jetpackdemo.model.INewBookArraivedListener
import com.wz.jetpackdemo.model.User
import com.wz.jetpackdemo.ui.main.BaseViewBindingFragment

class MainFragment : BaseViewBindingFragment<MainFragmentBinding>() {

    val TAG = MainFragment::class.java.simpleName
    var iBookManager: IBookManager? = null
    lateinit var mService: Messenger
    var serviceConnectionFlag = false
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
            val book = Book(2, "App Develop Art")
            iBookManager?.addBook(book)
        }
        binding.tvGetBook.setOnClickListener {
            val bookList = iBookManager?.bookList
            binding.tvBookList.text = bookList.toString()
        }
        binding.tvMessenger.setOnClickListener {
            val intent = Intent(context, MessengerService::class.java)
            context?.bindService(intent, messengerServiceConnection, Context.BIND_AUTO_CREATE)
        }
        binding.tvProvider.setOnClickListener {
            val bookUri = Uri.parse("content://com.wz.jetpackdemo.provider/book")
            val contentValues = ContentValues()
            contentValues.put("_id",6)
            contentValues.put("name", "程序设计的艺术")
            val contentResolver = activity?.contentResolver
            contentResolver?.insert(bookUri, contentValues)
            val bookCursor = contentResolver?.query(bookUri, arrayOf("_id", "name"), null, null, null)
            while (bookCursor!!.moveToNext()) {
                val book = Book(bookCursor.getInt(0), bookCursor.getString(1))
                Log.e(TAG, "query book: $book")
            }
            bookCursor.close()
            val userUri = Uri.parse("content://com.wz.jetpackdemo.provider/user")
            val userCursor = contentResolver.query(userUri, arrayOf("_id", "name", "sex"), null, null, null)
            while (userCursor!!.moveToNext()) {
                val user =
                    User(userCursor.getString(1), userCursor.getInt(2) + 27, userCursor.getInt(0))
                Log.e(TAG, "query user: $user")
            }
            userCursor.close()
        }
    }

    val mOnINewBookArraivedListener = object :INewBookArraivedListener.Stub(){
        override fun onNewBookArrived(newBook: Book?) {
            Log.e(TAG, "receive new book : $newBook")
        }
    }

    private val serviceConnection = object :ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(TAG, "serviceConnected")
            serviceConnectionFlag = true
            iBookManager = IBookManager.Stub.asInterface(service)
            iBookManager?.asBinder()?.linkToDeath({
                Log.e(TAG, "binderDied thread:${Thread.currentThread().name}")

            },0)
            if (iBookManager != null) {
                val bookList = iBookManager?.bookList
                Log.e(TAG, "booklist: $bookList")
                iBookManager?.registerListener(mOnINewBookArraivedListener)
            }

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e(TAG, "service disconnected thread:${Thread.currentThread().name}")
            serviceConnectionFlag = false

        }

    }

    private val messengerServiceConnection = object :ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e("MessengerService", "messenger serviceConnected")
            mService = Messenger(service)
            val msg = Message.obtain(null, 0x01)
            val data = Bundle()
            data.putString("msg", "hello! this is client.")
            msg.data = data
            msg.replyTo = mGetReplyMessenger

            mService.send(msg)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e("MessengerService", "messenger service disconnected")

        }

    }
    val mGetReplyMessenger = Messenger( MessengerHandler())

    private class MessengerHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0x02 -> Log.e(
                    "MessengerService",
                    "receive msg from Service:" + msg.data.getString("reply")
                )
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceConnectionFlag) {
            context?.unbindService(serviceConnection)
        }


        if (iBookManager != null && iBookManager!!.asBinder().isBinderAlive) {
            iBookManager?.unregisterListener(mOnINewBookArraivedListener)
        }
    }
}