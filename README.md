

# Wikifut ⚽

Wikifut is a football match tracking app built with **Kotlin**, **Jetpack Compose**, and **MVVM architecture**. It allows users to search and view daily matches using the **API-Football**.

## Features

- 📅 **Daily Match Listings** – Displays football matches for the selected date.
- 🔍 **Search Functionality** – Search matches by league or team name.
- 🔔 **Notifications Icon** – Click to highlight favorite matches.
- 🕒 **Localized Time Display** – Shows match times in 12-hour format (AM/PM).
- 🌙 **Dark Themed UI** – A modern, sleek dark-themed user interface.

## Technologies Used 

- **Kotlin** – Primary language for development.
- **Jetpack Compose** – Declarative UI framework.
- **MVVM Architecture** – Clean architecture pattern.
- **Hilt** – Dependency injection.
- **Firebase Authentication** – User authentication.
- **API-Football** – Fetching real-time match data.
- **Coil** – Image loading library.

## Installation 

1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/wikifut.git
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle dependencies.
4. Run the app on an emulator or physical device.

## Screenshots 


| Initial Screen | Login Screen | Register Screen |
|---------------|-------------|----------------|
| ![Initial Screen](https://i.imgur.com/2Uk4oCM.png) | ![Login Screen](https://i.imgur.com/oZVJkmv.png) | ![Register Screen](https://i.imgur.com/Ic3ycsD.png) |

| Home Screen | Search Match | Calendar | Matches According Day |
|------------|-------------|----------|----------------------|
| ![Home Screen](https://i.imgur.com/6bTRij9.png) | ![Search Match](https://i.imgur.com/z7QwEtv.png) | ![Calendar](https://i.imgur.com/cr8RZ16.png) | ![Matches According Day](https://i.imgur.com/wUCK64V.png) |



## API Setup

To fetch match data, you need an API key from [API-Football](https://www.api-football.com/):

### **1️⃣ Get Your API Key**
1. Sign up on [API-Football](https://www.api-football.com/).
2. Retrieve your **X-RapidAPI-Key** from the dashboard.

### **2️⃣ Store the API Key Securely**
Instead of hardcoding the API key, store it in **`gradle.properties`** to keep it secure:

1. Open the `gradle.properties` file (in the root project directory).
2. Add the following lines:
   ```properties
   X_RAPIDAPI_KEY=your_api_key_here
   CLIENT_ID_FIREBASE=your_firebase_client_id
