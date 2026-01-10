package C242.PS248

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_splash_screen)

        val logo = findViewById<ImageView>(R.id.logo)
        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.scale)
        logo.startAnimation(animation)
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        Handler().postDelayed({
            if (currentUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            finish()
        }, 3000)
    }
}