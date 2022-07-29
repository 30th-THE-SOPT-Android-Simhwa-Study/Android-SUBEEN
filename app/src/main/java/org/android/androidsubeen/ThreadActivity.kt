package org.android.androidsubeen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import org.android.androidsubeen.databinding.ActivityThreadBinding
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ThreadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThreadBinding
    val myHandler = MyHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThreadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickEvent()
    }

    private fun clickEvent() {
        binding.btnFirst.setOnClickListener {
            binding.ivProfile.setImageResource(R.drawable.ic_back)
            BackgroundThread().start()
        }
        binding.btnSecond.setOnClickListener {
            binding.ivProfile.setImageResource(R.drawable.ic_back)
            BackgroundThread2().start()
        }
    }

    private fun getBitmapFromURL(src: String): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true  //읽기모드임
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    inner class MyHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            // 다른 Thread에서 전달받은 Message 처리
            val bundle = msg.data
            val image = bundle.getString("bitmap")
            image?.let {
                binding.ivProfile.post {
                    binding.ivProfile.setImageBitmap(convertBitMap().StringToBitmap(it))
                }
            }
        }
    }

    inner class BackgroundThread : Thread() {
        override fun run() {
            val bitmap = getBitmapFromURL("https://avatars.githubusercontent.com/u/15981307?v=4")
            val bundle = Bundle()
            bundle.putString("bitmap",
                bitmap?.let { bitmapImage ->
                    convertBitMap().BitmapToString(bitmapImage)
                })
            val message = myHandler.obtainMessage()
            message.data = bundle
            SystemClock.sleep(1000)
            myHandler.sendMessage(message)
        }
    }

    inner class BackgroundThread2 : Thread() {
        override fun run() {
            val bitmap = getBitmapFromURL("https://avatars.githubusercontent.com/u/90037701?v=4")
            val bundle = Bundle()
            bundle.putString("bitmap",
                bitmap?.let { bitmapImage ->
                    convertBitMap().BitmapToString(bitmapImage)
                })
            val message = myHandler.obtainMessage()
            message.data = bundle
            SystemClock.sleep(1000)
            myHandler.sendMessage(message)
        }
    }

    inner class BackgroundThread3 : Thread() {
        override fun run() {
            //implement Count
            val bundle = Bundle()
            val message = myHandler.obtainMessage()
            myHandler.sendMessage(message)

        }
    }
}
