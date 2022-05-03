package com.example.currencyconverter

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

const val REQUEST_IMAGE_CAPTURE = 1

class MainScreen : AppCompatActivity() {

    val userIcon: ImageView by lazy { findViewById(R.id.user_icon) }
    val userName: TextView by lazy { findViewById(R.id.user_name) }
    val userEmail: TextView by lazy { findViewById(R.id.user_email) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        supportActionBar?.hide()

        val shareButton: Button = findViewById(R.id.share_button)
        val shareEmailButton: Button = findViewById(R.id.share_email_button)
        val callButton: Button = findViewById(R.id.call_button)
        val startCameraButton: Button = findViewById(R.id.start_camera_button)

        shareButton.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Message of share button")
                type = "text/plain"
            }
            val intent = Intent.createChooser(shareIntent, "Share with")
            startActivity(intent)
        }

        shareEmailButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:example@mail.com")
            startActivity(Intent.createChooser(intent, "Send with"))
        }

        callButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel: +7747627929"))
            startActivity(intent)
        }

        startCameraButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            userIcon.setImageBitmap(imageBitmap)
        }
    }
}