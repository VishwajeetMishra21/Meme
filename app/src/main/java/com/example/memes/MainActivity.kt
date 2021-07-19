package com.example.memes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButtonToggleGroup
import com.squareup.picasso.Picasso
import org.json.JSONException
import android.view.View

class MainActivity : AppCompatActivity() {

    lateinit var Image: ImageView
    lateinit var next: Button
    lateinit var share: Button
    var currentImage: String? = null
    lateinit var big_theme: MaterialButtonToggleGroup
    lateinit var dark: Button
    lateinit var light: Button
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Image = findViewById(R.id.Image)
        next = findViewById(R.id.next)
        share = findViewById(R.id.share)
        big_theme = findViewById(R.id.big_theme)
        dark = findViewById(R.id.dark)
        light = findViewById(R.id.light)
        progressBar = findViewById(R.id.progressBar)

        //It provide the meme
        search()


        //When click on next button for next meme
        next.setOnClickListener {
            search()
        }

        //dark theme and light theme
        big_theme.addOnButtonCheckedListener { group, checkedId, isChecked ->
            val theme = when (checkedId) {
                R.id.dark -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(theme)
        }


        //Sharing the Meme
        share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Share this please")
            val chooser = Intent.createChooser(intent, "Share $currentImage")
            startActivity(chooser)
        }

    }

    fun search() {
        progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this@MainActivity)
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest =
            object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                try {

                    println("Response are $it")
                    progressBar.visibility = View.GONE
                    currentImage = it.getString("url")
                    Picasso.get().load(currentImage).into(Image)
                } catch (e: JSONException) {
                    Toast.makeText(this, "Error Josn", Toast.LENGTH_SHORT).show()
                }

            },
                Response.ErrorListener {
                    println("Error are occ $it")
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"] = "Mozilla/5.0"
                    return headers
                }
            }
        queue.add(jsonObjectRequest)

    }

}