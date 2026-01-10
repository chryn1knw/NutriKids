package C242.PS248

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

class RecommendationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation)

        val statusRecyclerView: RecyclerView = findViewById(R.id.status_recycler_view)
        val foodRecyclerView: RecyclerView = findViewById(R.id.food_recycler_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        }

        statusRecyclerView.layoutManager = LinearLayoutManager(this)
        foodRecyclerView.layoutManager = LinearLayoutManager(this)

        val recommendedData = intent.getParcelableExtra<FoodRecommendationResponse>("RECOMMENDED_DATA")

        recommendedData?.let {
            statusRecyclerView.adapter = StatusAdapter(listOf(it))
            foodRecyclerView.adapter = FoodAdapter(it.recommendations)
        }
    }
}