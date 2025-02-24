package kz.olzhass.kolesa

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class MainActivity : AppCompatActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_activity)

        Log.d("Main", "Created")

        enableEdgeToEdge()
    }
    fun onClickListener(view: View) {
        setContentView(R.layout.activity_2)
    }
    fun onClickToLogin(view: View) {
        setContentView(R.layout.login_page)
    }
    fun onClickToReturnWelcome(view: View) {
        setContentView(R.layout.welcome_activity)
    }
    fun listener2 (view: View) {
        setContentView(R.layout.activity_main)
    }


    override fun onStart() {
        Log.d("Main", "Started")
        super.onStart()
    }

    override fun onPause() {
        Log.d("Main", "Paused")
        super.onPause()

    }

    override fun onResume() {
        Log.d("Main", "Resumed")
        super.onResume()
    }

}