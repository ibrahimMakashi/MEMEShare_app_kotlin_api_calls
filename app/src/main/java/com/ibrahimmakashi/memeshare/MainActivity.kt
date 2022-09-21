package com.ibrahimmakashi.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Calling loadmeme() to display the meme.
        loadmeme()

    }

    lateinit var presentImageUrl :String

    private fun loadmeme() {
        // Initializing a progress bar which
        // gets disabled whenever image is loaded.
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val meme: ImageView = findViewById(R.id.memeImageView)

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a JSON object response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                // Storing the url of meme from the API.
                 presentImageUrl = response.getString("url")

                // Displaying it with the use of Glide.
                Glide.with(this).load(presentImageUrl).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false

                    }
                }).into(meme)
            }
        ) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
        }
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    fun nextmeme(view: View) {
        // Calling loadmeme() whenever
        // Next button is clicked.
        loadmeme()
    }

    fun sharememe(view: View) {
        // Creating an Intent to share
        // the meme with other apps.
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout this cool meme $presentImageUrl")
        val chooser = Intent.createChooser(intent, "Share this meme")
        startActivity(chooser)
    }
}
