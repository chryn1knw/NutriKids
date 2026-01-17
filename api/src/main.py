from datetime import datetime, timezone
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import joblib
import numpy as np
from typing import Optional

app = FastAPI(
    title="Nutrition Status Prediction API",
    description="API untuk prediksi status gizi menggunakan SVM dengan kalkulasi otomatis",
    version="1.0"
)

MODEL_PATH = "./artifacts/models/best_model_SVM.pkl"
ENCODER_PATH = "./artifacts/models/label_encoder.pkl"

model = joblib.load(MODEL_PATH)
label_encoder = joblib.load(ENCODER_PATH)

# ===============================
# Schema Input
# ===============================
class NutritionInput(BaseModel):
    age: float
    gender: int  # 1 untuk male, 0 untuk female
    height: float  # dalam cm
    weight: float  # dalam kg

# ===============================
# Schema Input Complete
# ===============================
class NutritionInputComplete(BaseModel):
    age: float
    gender: int
    height: float
    weight: float
    bfp: Optional[float] = None
    bmr: Optional[float] = None
    bmi: Optional[float] = None

# ===============================
# Schema Output
# ===============================
class NutritionOutput(BaseModel):
    status_gizi: str
    bmi: float
    persentase_lemak_tubuh: float
    tingkat_metabolisme_basal: float
    input_data: dict
    prediction_encoded: int

# ===============================
# Health Check
# ===============================
@app.get("/")
def root():
    return {"status": "API is running 🚀", "version": "1.0"}

@app.get("/health")
def health_check():
    return {
        "status": "ok",
        "timestamp": datetime.now(timezone.utc).isoformat(),
        "version": "1.0.0"
    }

# ===============================
# Helper Functions
# ===============================
def calculate_bmi(height_cm: float, weight_kg: float) -> float:
    """
    Calculate Body Mass Index (BMI)
    """
    height_m = height_cm / 100
    bmi = weight_kg / (height_m ** 2)
    return round(bmi, 2)

def estimate_body_fat_percentage(gender: int, bmi: float, age: float) -> float:
    """
    Estimate Body Fat Percentage based on gender, BMI, and age
    """
    if gender == 1:
        body_fat_percentage = 1.20 * bmi + 0.23 * age - 16.2
    else:
        body_fat_percentage = 1.20 * bmi + 0.23 * age - 5.4
    return round(body_fat_percentage, 2)

def calculate_bmr(weight_kg: float, height_cm: float, age: float, gender: int) -> float:
    """
    Calculate Basal Metabolic Rate (BMR) using Mifflin-St Jeor Equation
    """
    if gender == 1:
        bmr = 88.362 + (13.397 * weight_kg) + (4.799 * height_cm) - (5.677 * age)
    else:
        bmr = 447.593 + (9.247 * weight_kg) + (3.098 * height_cm) - (4.330 * age)
    return round(bmr, 2)

def validate_input(age: float, height_cm: float, weight_kg: float) -> bool:
    """
    Validate input ranges
    """
    return (65 < height_cm < 300 and 
            6 < weight_kg < 200 and 
            0 < age < 19)

# ===============================
# Main Prediction Endpoint
# ===============================
@app.post("/predict", response_model=NutritionOutput)
def predict_nutrition(data: NutritionInput):
    """
    Predict nutrition status dengan kalkulasi otomatis BFP, BMR, dan BMI
    """
    try:
        # Validasi input
        if not validate_input(data.age, data.height, data.weight):
            raise HTTPException(
                status_code=400,
                detail="Input values for height, weight, or age are out of valid range."
            )
        
        bmi = calculate_bmi(data.height, data.weight)
        bfp = estimate_body_fat_percentage(data.gender, bmi, data.age)
        bmr = calculate_bmr(data.weight, data.height, data.age, data.gender)
        
        input_data = np.array([[
            data.age,
            data.gender,
            data.height,
            data.weight,
            bfp,
            bmr,
            bmi
        ]])
        
        prediction_encoded = model.predict(input_data)[0]
        
        prediction_label = label_encoder.inverse_transform([prediction_encoded])[0]
        
        return {
            "status_gizi": prediction_label,
            "bmi": bmi,
            "persentase_lemak_tubuh": bfp,
            "tingkat_metabolisme_basal": bmr,
            "input_data": {
                "age": data.age,
                "gender": data.gender,
                "height": data.height,
                "weight": data.weight
            },
            "prediction_encoded": int(prediction_encoded)
        }
        
    except Exception as e:
        print("ERROR:", repr(e))
        raise HTTPException(status_code=500, detail=str(e))

# ===============================
# Complete Prediction Endpoint
# ===============================
@app.post("/predict/complete")
def predict_nutrition_complete(data: NutritionInputComplete):
    """
    Predict nutrition status dengan atau tanpa kalkulasi otomatis
    Jika BFP, BMR, atau BMI tidak disediakan, akan dihitung otomatis
    """
    try:
        if not validate_input(data.age, data.height, data.weight):
            raise HTTPException(
                status_code=400,
                detail="Input values for height, weight, or age are out of valid range."
            )
        
        bmi = data.bmi if data.bmi is not None else calculate_bmi(data.height, data.weight)
        bfp = data.bfp if data.bfp is not None else estimate_body_fat_percentage(data.gender, bmi, data.age)
        bmr = data.bmr if data.bmr is not None else calculate_bmr(data.weight, data.height, data.age, data.gender)
        
        input_data = np.array([[
            data.age,
            data.gender,
            data.height,
            data.weight,
            bfp,
            bmr,
            bmi
        ]])
        
        prediction_encoded = model.predict(input_data)[0]
        prediction_label = label_encoder.inverse_transform([prediction_encoded])[0]
        
        return {
            "prediction_encoded": int(prediction_encoded),
            "prediction_label": prediction_label,
            "calculated_values": {
                "bmi": bmi,
                "bfp": bfp,
                "bmr": bmr
            },
            "input_data": {
                "age": data.age,
                "gender": data.gender,
                "height": data.height,
                "weight": data.weight
            }
        }
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

# ===============================
# Test Endpoint
# ===============================
@app.get("/test")
def test_model():
    """
    Test endpoint dengan sample data dan kalkulasi otomatis
    """
    sample_data = {
        "age": 10,
        "gender": 1,
        "height": 140.5,
        "weight": 35.2
    }
    
    bmi = calculate_bmi(sample_data["height"], sample_data["weight"])
    bfp = estimate_body_fat_percentage(sample_data["gender"], bmi, sample_data["age"])
    bmr = calculate_bmr(sample_data["weight"], sample_data["height"], sample_data["age"], sample_data["gender"])
    
    input_data = np.array([[
        sample_data["age"],
        sample_data["gender"],
        sample_data["height"],
        sample_data["weight"],
        bfp,
        bmr,
        bmi
    ]])
    
    prediction_encoded = model.predict(input_data)[0]
    prediction_label = label_encoder.inverse_transform([prediction_encoded])[0]
    
    return {
        "test_sample": sample_data,
        "calculated_values": {
            "bmi": bmi,
            "bfp": bfp,
            "bmr": bmr
        },
        "prediction_encoded": int(prediction_encoded),
        "prediction_label": prediction_label
    }

# ===============================
# Utility Endpoints
# ===============================
@app.post("/calculate/bmi")
def calculate_bmi_endpoint(height: float, weight: float):
    """Endpoint khusus untuk kalkulasi BMI"""
    try:
        bmi = calculate_bmi(height, weight)
        return {"bmi": bmi}
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

@app.post("/calculate/bfp")
def calculate_bfp_endpoint(gender: int, bmi: float, age: float):
    """Endpoint khusus untuk kalkulasi Body Fat Percentage"""
    try:
        bfp = estimate_body_fat_percentage(gender, bmi, age)
        return {"body_fat_percentage": bfp}
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

@app.post("/calculate/bmr")
def calculate_bmr_endpoint(weight: float, height: float, age: float, gender: int):
    """Endpoint khusus untuk kalkulasi BMR"""
    try:
        bmr = calculate_bmr(weight, height, age, gender)
        return {"bmr": bmr}
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)