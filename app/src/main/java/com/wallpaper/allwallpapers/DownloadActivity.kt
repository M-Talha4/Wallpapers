package com.wallpaper.allwallpapers
import android.app.WallpaperManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.wallpaper.allwallpapers.databinding.ActivityDownloadBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import java.io.File
import java.util.*


class DownloadActivity : AppCompatActivity() {
    lateinit var binding: ActivityDownloadBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        val link = intent.getStringExtra("link1")
        Glide.with(this).load(link).into(binding.imageview1)
        binding.setwallpaper.setOnClickListener {
            val wallPaperManager = WallpaperManager.getInstance(applicationContext)
            val uri =  File(link.toString()).toUri()
            val bitmap = BitmapFactory.decodeStream(
                contentResolver.openInputStream(uri)
            )
            wallPaperManager.setBitmap(bitmap)
            Toast.makeText(this,"Set successfully",Toast.LENGTH_SHORT).show()

        }
    }
}

