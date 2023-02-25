package com.wallpaper.allwallpapers

import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wallpaper.allwallpapers.databinding.ActivityFinalBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.random.Random


class FinalActivity : AppCompatActivity() {
    lateinit var binding: ActivityFinalBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val link = intent.getStringExtra("link")

        val url1 = URL(link)



        Glide.with(this).load(link).into(binding.imageview1)

        binding.setwallpaper.setOnClickListener {

            if(isOnline(this)){
                val result: kotlinx.coroutines.Deferred<Bitmap?> = GlobalScope.async {

                    getBitmapFromURL(url1.toString())

                }

                GlobalScope.launch(Dispatchers.Main) {

                    val wallpaperManager=WallpaperManager.getInstance(applicationContext)
                    wallpaperManager.setBitmap(result.await())

                    saveimage(result.await())

                }
                Toast.makeText(this,"set wallpaper successfully",Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(this,"You are offline",Toast.LENGTH_SHORT).show()


            }





        }

        binding.downloadwallpaper.setOnClickListener {

            if(isOnline(this)){
                val result: kotlinx.coroutines.Deferred<Bitmap?> = GlobalScope.async {

                    getBitmapFromURL(url1.toString())

                }
                GlobalScope.launch(Dispatchers.Main) {
                    saveimage(result.await())

                }
                Toast.makeText(this,"Download  successfully",Toast.LENGTH_SHORT).show()


            }else{

                Toast.makeText(this,"You are offline",Toast.LENGTH_SHORT).show()


            }



        }

    }

    private fun saveimage(image: Bitmap?) {
        val random1 = Random.nextInt(520985)
        val random2 = Random.nextInt(520985)
        val name ="All Wallpapers-${random1+random2}"

        val data:OutputStream
        try {
            val resolver=contentResolver
            val contentValues=ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,"$name.jpg")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpg")

            contentValues.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES+ File.separator+"All Wallpapers"

            )
            val imageurl =resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)
            data=resolver.openOutputStream(Objects.requireNonNull(imageurl)!!)!!
            image?.compress(Bitmap.CompressFormat.JPEG,100,data)
            Objects.requireNonNull(data)

        }catch (e:Exception){
            Toast.makeText(this,"not saved",Toast.LENGTH_SHORT).show()


        }

    }

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun isOnline(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }



}

