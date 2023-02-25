package com.wallpaper.allwallpapers.Adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration
import com.wallpaper.allwallpapers.FinalActivity
import com.wallpaper.allwallpapers.ModalClass.wallpapermodal
import com.bumptech.glide.Glide
import com.wallpaper.allwallpapers.R
import java.util.concurrent.TimeUnit

class WallpaperAdapter(val cc:Context, val list1:ArrayList<wallpapermodal>,val activity: Fragment): MaxAdViewAdListener, RecyclerView.Adapter<WallpaperAdapter.ViewHolder>() {

    private var adView: MaxAdView? = null
    private lateinit var interstitialAd: MaxInterstitialAd
    private var retryAttempt = 0.0
    var intent = Intent()


    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.imageview)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.simple_wallpaper,parent,false)

        interstitialAd = MaxInterstitialAd( "00d118750c21cde3", activity.requireActivity())
        interstitialAd.setListener(this)

        AppLovinSdk.getInstance( cc ).setMediationProvider( "max" )
        AppLovinSdk.getInstance( cc ).initializeSdk({ configuration: AppLovinSdkConfiguration ->
            // AppLovin SDK is initialized, start loading ads
            // call function

            createInterstitialAd()

        })

        return ViewHolder(view)

    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(cc).load(list1[position].link).into(holder.image);
        holder.itemView.setOnClickListener {

            intent = Intent(cc, FinalActivity::class.java)
            intent.putExtra("link",list1[position].link)

            if  ( interstitialAd.isReady )
            {

                cc.startActivity(intent)
                interstitialAd.showAd()



            }else{
                cc.startActivity(intent)

            }

        }


    }

    override fun getItemCount()=list1.size


    private fun createInterstitialAd() {
        interstitialAd.loadAd()
    }




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