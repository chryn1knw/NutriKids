package C242.PS248

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var ageSpinner: Spinner
    private lateinit var heightInput: EditText
    private lateinit var weightInput: EditText
    private lateinit var genderSpinner: Spinner
    private lateinit var submitButton: Button
    private lateinit var toolbar: Toolbar
    private lateinit var progressDialog: ProgressDialog

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ageSpinner = findViewById(R.id.input_age)
        heightInput = findViewById(R.id.input_height)
        weightInput = findViewById(R.id.input_weight)
        genderSpinner = findViewById(R.id.input_gender)
        submitButton = findViewById(R.id.btn_process)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Processing data...")
        progressDialog.setCancelable(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        }

        submitButton.setOnClickListener {
            Log.d(TAG, "Submit button clicked")
            processNutritionData()
        }
    }

    private fun processNutritionData() {
        try {
            val age = ageSpinner.selectedItem.toString().toInt()
            val height = heightInput.text.toString().toInt()
            val weight = weightInput.text.toString().toInt()
            val gender = genderSpinner.selectedItem.toString()

            val requestData = RequestData(
                usia = age,
                tb = height,
                bb = weight,
                jenis_kelamin = gender
            )

            progressDialog.show()

            val call = RetrofitInstance.apiService.processData(requestData)
            call.enqueue(object : Callback<FoodRecommendationResponse> {
                override fun onFailure(call: Call<FoodRecommendationResponse>, t: Throwable) {
                    Log.e(TAG, "API call failed", t)
                    runOnUiThread {
                        progressDialog.dismiss()
                        Toast.makeText(this@MainActivity, "Error: Cannot process API request.", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onResponse(call: Call<FoodRecommendationResponse>, response: Response<FoodRecommendationResponse>) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        Log.d(TAG, "API call successful, response data: $responseData")
                        runOnUiThread {
                            responseData?.let { data ->
                                val intent = Intent(this@MainActivity, RecommendationActivity::class.java)
                                intent.putExtra("RECOMMENDED_DATA", data)
                                startActivity(intent)
                            }
                        }
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Unknown error"
                        val errorMessage = parseErrorMessage(errorBody)
                        runOnUiThread { Toast.makeText(this@MainActivity,
                            "ERROR: $errorMessage", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
        } catch (e: Exception) {
            Toast.makeText(this, "Error processing data. Please check your input.", Toast.LENGTH_LONG).show()
        }
    }
    private fun parseErrorMessage(errorBody: String): String {
        return try {
            val jsonObject = JSONObject(errorBody)
            jsonObject.getString("error")
        } catch (e: JSONException) {
            "Unknown error"
        }
    }
}