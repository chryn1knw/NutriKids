package C242.PS248

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class FoodRecommendationResponse(
    @SerializedName("bmi") val bmi: Double,
    @SerializedName("description") val description: String,
    @SerializedName("prediction") val prediction: String,
    @SerializedName("recommendations") val recommendations: List<FoodItem>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(FoodItem)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(bmi)
        parcel.writeString(description)
        parcel.writeString(prediction)
        parcel.writeTypedList(recommendations)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FoodRecommendationResponse> {
        override fun createFromParcel(parcel: Parcel): FoodRecommendationResponse {
            return FoodRecommendationResponse(parcel)
        }

        override fun newArray(size: Int): Array<FoodRecommendationResponse?> {
            return arrayOfNulls(size)
        }
    }
}

data class FoodItem(
    @SerializedName("Caloric Value") val caloricValue: Double,
    @SerializedName("Protein") val protein: Double,
    @SerializedName("food") val food: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(caloricValue)
        parcel.writeDouble(protein)
        parcel.writeString(food)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FoodItem> {
        override fun createFromParcel(parcel: Parcel): FoodItem {
            return FoodItem(parcel)
        }

        override fun newArray(size: Int): Array<FoodItem?> {
            return arrayOfNulls(size)
        }
    }
}