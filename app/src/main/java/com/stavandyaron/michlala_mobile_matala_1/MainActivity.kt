package com.stavandyaron.michlala_mobile_matala_1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var board = Array(3) { Array(3) { "" } }
    private var isGameOver = false
    private var turnCount = 0

    private fun initCells() {
        for (index in 1..9) {
            val cellId = resources.getIdentifier("main_activity_cell_${index}_text_view", "id", packageName)
            findViewById<TextView>(cellId).apply {
                setOnClickListener { cellOnClick(it as TextView, index) }
            }
        }
    }

    val amogus = 3

    private fun clearCells() {
        for (index in 1..9) {
            val cellId = resources.getIdentifier("main_activity_cell_${index}_text_view", "id", packageName)
            findViewById<TextView>(cellId).apply {
                text = ""
            }
        }
    }

    private fun initRestartButton() {
        findViewById<Button>(R.id.main_activity_play_again_button).apply {
            setOnClickListener {
                playAgain()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initCells()
        initRestartButton()
    }

    private fun cellOnClick(cell: TextView, index: Int) {
        val row = (index - 1) / 3
        val column = (index - 1) % 3

        if (isGameOver || board[row][column] != "") {
            return
        }

        val currentPlayer = if (turnCount % 2 == 0) "X" else "O"
        val currentColor = if (turnCount % 2 == 0) getColor(R.color.black) else getColor(R.color.white)

        board[row][column] = currentPlayer
        cell.text = currentPlayer
        cell.setTextColor(currentColor)

        if (checkWin(currentPlayer, row, column)) {
            findViewById<TextView>(R.id.main_activity_title).text = "The Winner: ${currentPlayer}"
            isGameOver = true
        } else if (checkFullBoard()) {
            findViewById<TextView>(R.id.main_activity_title).text = "It's a Draw"
            isGameOver = true
        }

        if (isGameOver) {
            findViewById<Button>(R.id.main_activity_play_again_button).visibility = Button.VISIBLE
            return
        }

        turnCount++
        val nextPlayer = if (turnCount % 2 == 0) "X" else "O"
        findViewById<TextView>(R.id.main_activity_title).text = "It's ${nextPlayer}'s Turn"
    }

    private fun checkFullBoard(): Boolean {
        for (row in board) {
            for (cell in row) {
                if (cell == "") {
                    return false
                }
            }
        }

        return true
    }

    private fun checkWin(player: String, row: Int, col: Int): Boolean {
        if (board[row].all { it == player }) {
            return true
        }

        if ((0..2).all { board[it][col] == player }) {
            return true
        }

        if (row + col == 2 && (0..2).all { board[it][2 - it] == player }) {
            return true
        }

        if (row == col && (0..2).all { board[it][it] == player }) {
            return true
        }

        return false
    }

    private fun playAgain() {
        board = Array(3) { Array(3) { "" } }
        clearCells()
        isGameOver = false
        turnCount = 0
        findViewById<TextView>(R.id.main_activity_title).text = "It's X's Turn"
        findViewById<Button>(R.id.main_activity_play_again_button).visibility = Button.INVISIBLE
    }
}