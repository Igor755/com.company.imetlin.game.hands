package com.company.imetlin.game.hands

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity(), CallbackMsg {

    private lateinit var tvResult: TextView
    private lateinit var llResult: LinearLayout
    private var mInterstitialAd: InterstitialAd? = null
    private  val TAG = "MainActivity"
    private lateinit var adView: AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this,"ca-app-pub-5248500607614632/3976268752", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.toString())
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
                show()
            }
        })
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }

        }
        tvResult = findViewById(R.id.tvResult)
        llResult = findViewById(R.id.llResult)
        adView = findViewById(R.id.adView)
        mutableListOf(
            R.id.btnPaperPlayerOne,
            R.id.btnRockPlayerOne,
            R.id.btnScissorsPlayerOne,
            R.id.btnRefresh
        )
            .forEachIndexed { index, i ->
                findViewById<ImageButton>(i).setOnClickListener {
                    val btn = it as ImageButton
                    if (index != 3) {
                        refresh(false)
                        val player = Player(btn.tag.toString())
                        val controller = Controller(this, this)
                        controller.setPlayer(player)
                        controller.setPlayerTwo()
                        val computerBet = controller.getPlayerTwo()?.bet
                        selectComputer(computerBet)
                        controller.playRound()
                        btn.background = ContextCompat.getDrawable(this, R.drawable.bg_item)
                    } else {
                        refresh(true)
                        tvResult.text = getString(R.string.versus)
                        tvResult.setTextColor(
                            ContextCompat.getColor(
                                this,
                                android.R.color.holo_red_light
                            )
                        )
                        tvResult.textSize = 90F
                        tvResult.setLines(1)
                        llResult.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }

        val adRequest2 = AdRequest.Builder().build()
        adView.loadAd(adRequest2)
        adView.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }
    }

    private fun show(){
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

    private fun refresh(enabled: Boolean) {
        mutableListOf(
            R.id.btnPaperPlayerOne,
            R.id.btnRockPlayerOne,
            R.id.btnScissorsPlayerOne,
            R.id.btnPaperPlayerTwo,
            R.id.btnRockPlayerTwo,
            R.id.btnScissorsPlayerTwo
        ).forEachIndexed { _, i ->
            findViewById<ImageButton>(i).isEnabled = enabled
            findViewById<ImageButton>(i).background = null
        }
    }

    private fun selectComputer(s: String?) {
        mutableListOf(
            R.id.btnPaperPlayerTwo,
            R.id.btnRockPlayerTwo,
            R.id.btnScissorsPlayerTwo
        ).forEachIndexed { _, i ->
            val btn = findViewById<ImageButton>(i)
            btn.background = if (btn.tag == s) {
                ContextCompat.getDrawable(this, R.drawable.bg_item)
            } else {
                null
            }
        }
    }

    override fun result(s: String) {
        tvResult.text = s
        tvResult.textSize = 20F
        tvResult.setTextColor(ContextCompat.getColor(this, R.color.white))
        when (s) {
            getString(R.string.player_one_win), getString(R.string.player_two_win) -> {
                llResult.setBackgroundColor(
                    ContextCompat.getColor(
                        this, R.color.win_green
                    )
                )
                tvResult.setLines(3)
            }
            else -> {
                llResult.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.draw_blue
                    )
                )
                tvResult.setLines(2)
            }
        }
    }
}