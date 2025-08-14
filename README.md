# KNR Connect: A Local Business Connection Platform

**Tagline:** *A modern Android application designed to bridge the gap between the community and local businesses in Karimnagar, Connecting the community of Karimnagar with local businesses built with a professional-grade tech stack.*

This project is a comprehensive portfolio piece, engineered to solve a real-world problem while demonstrating a mastery of modern Android development principles. The core mission is to provide a clean, reliable, and fast way for the local community to find and directly connect with essential businesses.

---

## 📸 App Screenshots

| Home Screen | Details Screen | Favorites Screen | Settings Screen |
| :---: | :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/e82125ea-b479-4370-954f-b75664cc5a0f" alt="Home Screen" width="200"/> | <img src="https://github.com/user-attachments/assets/86fd3ab0-d3a2-4f55-8bac-0fb309155498" alt="Details Screen" width="200"/> | <img src="https://github.com/user-attachments/assets/7b66d815-6513-40ff-b5af-bb1cef7f6e4e" alt="Favorites Screen" width="200"/> | <img src="https://github.com/user-attachments/assets/84775261-ed02-42e8-b23e-e32761c6f76e" alt="Settings Screen" width="200"/> |

---

## ✨ Core Features

* **Powerful Search:** A floating search bar that filters businesses not just by name, but by category and hidden keywords (e.g., "biryani," "doctor") for a superior search experience.
* **Direct Connect Actions:** The details screen allows users to instantly:
    * 📞 **Call** the business.
    * 📱 Open a **WhatsApp** chat.
    * 🗺️ Get **Directions** via Google Maps.
* **Persistent Favorites:** Users can mark businesses as favorites, with choices saved permanently on the device's local database.
* **Modern UI & UX:**
    * A professional bottom navigation bar for seamless screen transitions.
    * A functional Settings screen with a working **Dark Mode / Light Mode** toggle that themes the entire application.
    * Loading indicators and clear error messages to ensure a smooth user experience.

---

## 🛠️ Tech Stack & Professional Architecture

This project was engineered using professional best practices to demonstrate readiness for a developer role.

* **Architecture:**
    * **MVVM (Model-View-ViewModel):** A clean separation between the UI (View), app logic (ViewModel), and data (Model), making the code scalable, testable, and maintainable.
    * **Repository Pattern:** ViewModels communicate with a central `BusinessRepository`, which acts as the **"Single Source of Truth"** for all app data—a critical professional pattern that decouples data sources from the UI logic.

* **Modern Tech Stack:**
    * **UI:** Built entirely with **Jetpack Compose**, the modern, declarative UI toolkit for Android.
    * **Networking:** Uses **Retrofit** to communicate with a live API (a GitHub Gist) to fetch and parse JSON data.
    * **Database:** Integrated a **Room Database** for local data persistence, enabling offline access and powering the favorites system.
    * **Asynchronous Programming:** All network and database operations are handled efficiently in the background using **Kotlin Coroutines and Flow**, ensuring the app's UI never freezes.
    * **Navigation:** Implemented a robust, multi-screen architecture using **Compose Navigation**.

---

## 🚀 The Development Journey

This project's development was a realistic engineering journey that required overcoming numerous real-world obstacles, including:
* Resolving stubborn IDE caching bugs that required invalidating caches and performing clean builds.
* Debugging silent data loading failures caused by database schema mismatches and race conditions.
* Diagnosing and fixing broken API links and JSON syntax errors using `Logcat` and detailed error analysis.

Successfully navigating these challenges was a critical part of the process, proving my ability to debug, problem-solve, and build a stable, resilient application..
