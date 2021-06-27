package com.example.samplestickerapp

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object Ads {
    /* TEST ADS */
    private const val APPLICATION_ID = "ca-app-pub-3940256099942544~3347511713"
    private const val INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"

    private var interstitialAd: InterstitialAd? = null
    private lateinit var interAdCallback: FullScreenContentCallback
    fun initInterstitial() {
        interAdCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
            }
        }
    }

    fun loadInterstitial(context: Context) {
        InterstitialAd.load(
            context,
            INTERSTITIAL_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad;
                    interstitialAd?.fullScreenContentCallback = interAdCallback
                }

                override fun onAdFailedToLoad(ad: LoadAdError) {

                }
            })
    }

    fun showInterstitial(activity: Activity) {
        interstitialAd?.show(activity)
    }
}