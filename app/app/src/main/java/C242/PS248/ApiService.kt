package C242.PS248

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @POST("/predict")
    @Headers("Content-Type: application/json")
    fun processData(@Body data: RequestData): Call<FoodRecommendationResponse>
}