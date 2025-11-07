package com.example.quotegenerator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.quotegenerator.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var quotesList: List<QuoteGeneratorItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        loadQuotes()

        mainBinding.newQuoteButton.setOnClickListener {
            if (::quotesList.isInitialized && quotesList.isNotEmpty()) {
                showRandomQuote()
            }
        }
    }

    private fun loadQuotes() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://zenquotes.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(QuoteApi::class.java)

        api.getQuotes().enqueue(object : Callback<List<QuoteGeneratorItem>> {
            override fun onResponse(
                call: Call<List<QuoteGeneratorItem>>,
                response: Response<List<QuoteGeneratorItem>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    quotesList = response.body()!!.take(100) // get up to 100 quotes
                    showRandomQuote()
                }
            }

            override fun onFailure(call: Call<List<QuoteGeneratorItem>>, t: Throwable) {
                mainBinding.quoteText.text = "Failed to load quotes"
                mainBinding.authorText.text = ""
            }
        })
    }

    private fun showRandomQuote() {
        val quote = quotesList.random()
        mainBinding.quoteText.text = "\"${quote.q}\""
        mainBinding.authorText.text = "- ${quote.a}"
    }
}
