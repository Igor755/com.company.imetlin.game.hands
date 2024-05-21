package com.company.imetlin.game.hands

import android.content.Context

class Controller(private val listener: CallbackMsg, private val context: Context) {

    private var player: Player? = null
    private var playerTwo: Player? = null
    private var weakness = mapOf("scissors" to "rock", "paper" to "scissors", "rock" to "paper")

    fun setPlayer(player: Player) {
        this.player = player
    }

    fun setPlayerTwo() {
        this.playerTwo = Player(weakness.entries.random().key)
    }

    fun getPlayerTwo(): Player? {
        return this.playerTwo
    }

    fun playRound() {
        println(player!!.bet)
        listener.result(
            when (player!!.bet) {
                playerTwo!!.bet -> context.resources?.getString(R.string.draw)
                weakness[playerTwo!!.bet] -> context.resources?.getString(R.string.player_one_win)
                else -> context.resources?.getString(R.string.player_two_win)
            }.toString()
        )
    }
}
