package com.wallpaper.allwallpapers

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration
import com.wallpaper.allwallpapers.Fragment.DownloadFragment
import com.wallpaper.allwallpapers.Fragment.HomeFragment
import com.wallpaper.allwallpapers.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MaxAdViewAdListener {

    private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE = 101
    lateinit var binding: ActivityMainBinding

    private var adView: MaxAdView? = null
    private lateinit var interstitialAd: MaxInterstitialAd
    private var retryAttempt = 0.0

    @SuppressLint("AppCompatMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        interstitialAd = MaxInterstitialAd( "00d118750c21cde3", this )
        interstitialAd.setListener(this)
        AppLovinSdk.getInstance( this ).setMediationProvider( "max" )
        AppLovinSdk.getInstance( this ).initializeSdk({ configuration: AppLovinSdkConfiguration ->
            // AppLovin SDK is initialized, start loading ads
            // call function

            //createBannerAd1()
            createInterstitialAd()

        })

        setupPermissions()

        if (isOnline(this)) {

            Toast.makeText(this, "online", Toast.LENGTH_SHORT).show()


        } else {
            Toast.makeText(this, "off line", Toast.LENGTH_SHORT).show()


        }






        loadfragment(HomeFragment())


        binding.bottomNav.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.home -> {

                    loadfragment(HomeFragment())
                    true
                }
                R.id.download -> {
                    loadfragment(DownloadFragment())


                    true


                }
                else -> {
                    true
                }
            }


        }


    }

    private fun loadfragment(frag: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, frag).commit()

    }


    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }


    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }
    }


    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            RECORD_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i("@@@@@", "Permission has been denied by user")
                } else {
                    Log.i("@@@@@", "Permission has been granted by user")
                }
            }
        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option,menu)
        return super.onCreateOptionsMenu(menu)

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.rate->{

                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$packageName")
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                        )
                    )
                }



            }
            R.id.more->{
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/dev?id=6625738419290814495")
                    )
                )




            }

            R.id.share->{
                ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setChooserTitle("Chooser title")
                    .setText("http://play.google.com/store/apps/details?id=" + this.getPackageName())
                    .startChooser();



            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createInterstitialAd() {
        interstitialAd.loadAd()
    }

    //    private fun createBannerAd1() {
//        adView = MaxAdView("2d04fc65d6bc2382", this)
//        adView?.setListener(this@MainActivity)
//
//        // Stretch to the width of the screen for banners to be fully functional
//        val width = ViewGroup.LayoutParams.MATCH_PARENT
//
//        // Banner height on phones and tablets is 50 and 90, respectively
//        val heightPx = resources.getDimensionPixelSize(R.dimen.banner_height)
//
//        adView?.layoutParams = FrameLayout.LayoutParams(width, heightPx, Gravity.BOTTOM)
//
//        // Set background or background color for banners to be fully functional
//          adView?.setBackgroundColor(Color.WHITE)
//
//        val rootView = findViewById<ViewGroup>(android.R.id.content)
//        rootView.addView(adView)
//
//        // Load the ad
//        adView?.loadAd()
//
//    }
    override fun onAdLoaded(ad: MaxAd?) {
        retryAttempt = 0.0
    }

    override fun onAdDisplayed(ad: MaxAd?) {
        TODO("Not yet implemented")
    }

    override fun onAdHidden(ad: MaxAd?) {
        interstitialAd.loadAd()
    }

    override fun onAdClicked(ad: MaxAd?) {
        TODO("Not yet implemented")
    }

    override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
        retryAttempt++
        val delayMillis = TimeUnit.SECONDS.toMillis( Math.pow( 2.0, Math.min( 6.0, retryAttempt ) ).toLong() )

        Handler().postDelayed( { interstitialAd.loadAd() }, delayMillis )

    }

    override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
        interstitialAd.loadAd()
    }

    override fun onAdExpanded(ad: MaxAd?) {
        TODO("Not yet implemented")
    }

    override fun onAdCollapsed(ad: MaxAd?) {
        TODO("Not yet implemented")
    }



}
