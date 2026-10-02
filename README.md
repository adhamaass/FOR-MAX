# FOR MAX

> **"Train smarter. Track everything. Become stronger."**

FOR MAX is a production-quality fitness and progressive overload tracking platform engineered for lifters pursuing fat loss, muscle hypertrophy, and measurable athletic strength.

The core training system uses **deterministic mathematical rules**, local **Room database persistence**, and zero reliance on external AI/API services.

---

## 📱 Framework & Platform Note

* **Environment:** Native Android (Kotlin + Jetpack Compose + Material Design 3 + Room).
* **Cross-Platform Compatibility:** The codebase is built following strict **Clean Architecture** patterns separating domain entities, progression engines, repositories, and presentation layers. If porting to Flutter/Dart in another environment, all domain rules, deterministic progression mathematics, and database schemas map 1:1.

---

## ⚡ Key Features

### 1. First Launch & Dynamic Split Recommendation
* Collects biometrics (Age, Sex, Height, Weight), training experience, equipment access, and primary fitness goal (**Fat Loss + Muscle Gain** as recommended default).
* Recommends optimal training systems based on recovery kinetics and frequency:
  * **Full Body:** Optimal frequency for 3 training days.
  * **Upper / Lower:** Balanced stimulus & systemic recovery for 4 training days.
  * **Push / Pull / Legs:** Dedicated movement chain specialization for 5–6 days.
  * **5-Day Hybrid:** Power foundation coupled with hypertrophy micro-cycles.
  * **Custom / Advanced:** Athlete-tailored volume distributions.
* Explains the physiological rationale behind recommendations without claiming universal superiority.

### 2. Comprehensive Exercise Database (50+ Movements)
Pre-seeded with 50+ movements across all muscle groups:
* **Chest:** Barbell Bench Press, Incline Barbell Press, Dumbbell Bench Press, Incline DB Press, Machine Chest Press, Cable Fly, Pec Deck, Push-Up.
* **Back:** Lat Pulldown, Pull-Up, Assisted Pull-Up, Seated Cable Row, Chest Supported Row, Barbell Row, Dumbbell Row, Machine Row.
* **Shoulders:** Overhead Press, Seated DB Press, Machine Press, Lateral Raise, Cable Lateral Raise, Rear Delt Fly, Face Pull.
* **Biceps:** Barbell Curl, Standing DB Curl, Hammer Curl, Cable Curl, Preacher Curl.
* **Triceps:** Cable Pushdown, Overhead Cable Extension, Skull Crusher, Parallel Bar Dips, Machine Dip.
* **Legs:** Back Squat, Hack Squat, Leg Press, Bulgarian Split Squat, Walking Lunges, Leg Extension, Romanian Deadlift (RDL), Lying Leg Curl, Seated Leg Curl, Barbell Hip Thrust, Standing Calf Raise, Seated Calf Raise.
* **Core:** Kneeling Cable Crunch, Hanging Knee Raise, Lying Leg Raise, Forearm Plank.

Each exercise contains:
* Primary & secondary muscles, equipment category, difficulty, movement pattern.
* Step-by-step setup, execution, and breathing instructions.
* Common mistakes to avoid (with ❌ indicators).
* Biomechanical cues, safety tips, and progression notes.

### 3. Resilient Media Engine (Video / GIF / Image / Fallback)
* Supports decoupled `ExerciseMedia` models referencing local assets, web URLs, and cloud endpoints.
* **Fallback Priority:** `Video` ➔ `GIF / Movement Loop` ➔ `Image Gallery` ➔ `Biomechanical Vector Placeholder`.
* Integrated interactive video player controls (Play, Pause, Replay, Fullscreen, Mute).
* Never crashes the application when network or media files are absent.

### 4. High-Precision Workout Logger
* **Mandatory Rule:** **NEVER INVENT STARTING WEIGHTS.** If an exercise is performed for the first time, it guides: *"Choose a comfortable weight that allows controlled technique."*
* Set-by-set logging (Weight, Reps, RIR) with previous session benchmarks.
* Automated **Rest Interval Timer** (Compound: 2–3 min, Isolation: 60–120s) with `+30s`, `Skip`, and `Pause` controls.
* Instant **Exercise Alternative Replacement** maintaining historical tracking links.

### 5. Deterministic Progression Engine
* Computes Set Volume ($Weight \times Reps$), Exercise Volume, and Estimated 1RM using the scientific Epley formula.
* Evaluates completed rep targets against RIR:
  * **Suggested Progression:** *"Consider a small increase (+1.25 to 2.5 kg) next session."*
  * **Maintain Load:** *"Keep current load and work towards upper rep target."*
  * **Recovery Warning:** *"Performance is trending down. Review recovery, sleep, or nutrition."*

### 6. Weekly Report & Physique Analytics
* Weekly adherence consistency ($Workouts / Target \times 100\%$).
* Total tonnage and per-muscle group volume distribution (~10 hard sets reference).
* Categorized movement trends: **Improved**, **Stable**, **Declining**.
* Circumference tracking (Waist, Chest, Arms, Thighs) and private local progress photos (Front, Side, Back) with side-by-side comparison.
* Aerobic & Zone 2 cardio session logger.

### 7. Administrative Content Management System (CMS)
* Accessible via Profile ➔ **Admin / Owner Controls**.
* **Dashboard:** Metrics on published exercises, missing media, and missing alternatives.
* **Exercise CRUD:** Create, Edit, Preview, and Delete movements with full parameter tuning.
* **Media Manager:** Attach videos, GIFs, and images, configure primary display, and validate URLs.
* **Content Quality Checker:** Automated audit verifying 8 quality standards before publication.

---

## 🛠️ Tech Stack & Architecture

* **UI Layer:** Jetpack Compose + Material Design 3 (Electric Green `#00E676` on Dark `#0B0D0F`).
* **Architecture:** Clean Architecture + MVVM (Domain, Data, Presentation).
* **Database:** Android Room Database (`ForMaxDatabase`) with asynchronous Kotlin coroutines and reactive `Flow`.
* **Testing:** Local JVM unit tests (`ExampleUnitTest`) and Robolectric integration tests (`ExampleRobolectricTest`).
