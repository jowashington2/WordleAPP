package com.example.wordlep1

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var wordToGuess: String
    private var guessCount = 0
    private val maxGuesses = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI references
        val input = findViewById<EditText>(R.id.et_guess)
        val submit = findViewById<Button>(R.id.btn_submit)
        val reset = findViewById<Button>(R.id.btn_reset)
        val answer = findViewById<TextView>(R.id.answerText)

        val guessWords = listOf(
            findViewById<TextView>(R.id.guess1Word),
            findViewById<TextView>(R.id.guess2Word),
            findViewById<TextView>(R.id.guess3Word)
        )

        val resultViews = listOf(
            findViewById<TextView>(R.id.result1),
            findViewById<TextView>(R.id.result2),
            findViewById<TextView>(R.id.result3)
        )

        // Start with a random word
        wordToGuess = FourLetterWordList.getRandomFourLetterWord()

        submit.setOnClickListener {
            val userGuess = input.text.toString().uppercase()

            if (userGuess.length != 4 || !userGuess.all { it.isLetter() }) {
                Toast.makeText(this, "Enter a valid 4-letter word!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (guessCount < maxGuesses) {
                // Display guess and result
                guessWords[guessCount].text = userGuess
                resultViews[guessCount].text = checkGuess(userGuess, wordToGuess)
                guessCount++

                // Win
                if (userGuess == wordToGuess) {
                    Toast.makeText(this, "🎉 Correct! You guessed the word!", Toast.LENGTH_LONG).show()
                    answer.text = "The word was: $wordToGuess"
                    answer.visibility = View.VISIBLE
                    submit.isEnabled = false
                    reset.visibility = View.VISIBLE
                }
                // Loss after 3 guesses
                else if (guessCount == maxGuesses) {
                    Toast.makeText(this, "❌ Out of guesses! Try again next time.", Toast.LENGTH_LONG).show()
                    answer.text = "The word was: $wordToGuess"
                    answer.visibility = View.VISIBLE
                    submit.isEnabled = false
                    reset.visibility = View.VISIBLE
                }

                input.text.clear()
                hideKeyboard()
            }
        }

        reset.setOnClickListener {
            // Reset game
            wordToGuess = FourLetterWordList.getRandomFourLetterWord()
            guessCount = 0
            submit.isEnabled = true
            reset.visibility = View.GONE
            answer.visibility = View.GONE
            input.text.clear()

            guessWords.forEach { it.text = "" }
            resultViews.forEach { it.text = "" }

            Toast.makeText(this, "Game Reset. Try the new word!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkGuess(guess: String, target: String): String {
        val result = StringBuilder()
        for (i in guess.indices) {
            result.append(
                when {
                    guess[i] == target[i] -> "O"
                    guess[i] in target -> "+"
                    else -> "X"
                }
            )
        }
        return result.toString()
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        view?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
