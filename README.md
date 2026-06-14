# NutriKids 

**Aplikasi Prediksi Status Gizi Anak berbasis Machine Learning**

NutriKids adalah aplikasi Android yang membantu memantau dan memprediksi status gizi anak usia 0-18 tahun menggunakan model **Support Vector Machine (SVM)**. Aplikasi ini dilengkapi dengan backend FastAPI yang melakukan perhitungan otomatis BMI, Body Fat Percentage (BFP), dan Basal Metabolic Rate (BMR).

---

## ✨ Fitur Utama

- **Prediksi Status Gizi** menggunakan model SVM
- **Perhitungan Otomatis**:
  - Body Mass Index (BMI)
  - Persentase Lemak Tubuh (BFP)
  - Basal Metabolic Rate (BMR)
- **Rekomendasi Makanan** (via RecommendationActivity)
- **Autentikasi** (Login & Register dengan Google)
- **Antarmuka Android Modern** (Kotlin + Jetpack Compose?)

---

## 🏗️ Struktur Proyek
```
NutriKids/
├── app/                  # Aplikasi Android (Kotlin)
│   └── app/
│       └── src/main/
│
├── api/                  # Backend FastAPI
│   ├── src/
│   │   └── main.py       # Endpoint utama
│   ├── artifacts/models/ # Model ML (SVM + Label Encoder)
│   ├── requirements.txt
│   └── README.md
│
└── model/                # Notebook pelatihan model
    └── Classification.ipynb
```
---

## 🚀 Cara Menjalankan

### 1. Backend (API)

```bash
# Clone repository
git clone https://github.com/chryn1knw/NutriKids.git
cd NutriKids/api

# Buat virtual environment
python -m venv .venv

# Aktifkan environment
# Linux / macOS
source .venv/bin/activate
# Windows
.venv\Scripts\activate

# Install dependencies
pip install -r requirements.txt

# Jalankan API
python src/main.py
```

📄 Lisensi
Proyek ini dibuat untuk keperluan kuliah.
