package com.game.stonescissorspaper

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), CallbackMsg {

    private lateinit var tvResult: TextView
    private lateinit var llResult: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvResult = findViewById(R.id.tvResult)
        llResult = findViewById(R.id.llResult)
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