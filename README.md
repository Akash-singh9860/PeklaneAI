# Peklane AI Summarizer 📄✨

Peklane is an intelligent, privacy-first Android application that instantly summarizes long PDF documents completely offline. Powered by Google's **Gemini Nano** (via ML Kit GenAI), Peklane processes your private documents directly on your device—no internet connection or cloud API keys required.

## 🚀 Features

* **100% Offline AI Processing:** Utilizes Gemini Nano on-device for secure, private, and fast summarization.
* **Smart PDF Chunking:** Capable of reading long PDFs by intelligently chunking the text to fit within the NPU's context window.
* **Share Sheet Integration:** Send a PDF directly to Peklane from your File Manager, Google Drive, or Email via the Android Share menu.
* **Offline History & Smart Titles:** Automatically saves your generated summaries into a local database (Room). It even uses AI to generate a catchy 3-5 word title for your document history!
* **Markdown Support:** Renders the AI's bulleted and formatted responses beautifully using Markwon.
* **Modern UI/UX:** Built entirely with Jetpack Compose, featuring smooth Lottie loading animations and a clean, responsive layout.

## 🛠 Tech Stack

* **Language:** [Kotlin](https://kotlinlang.org/)
* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
* **On-Device AI:** [Google ML Kit GenAI (Summarization API)](https://developers.google.com/ml-kit/genai)
* **Architecture:** Clean Architecture + MVVM
* **Dependency Injection:** [Dagger Hilt](https://dagger.dev/hilt/)
* **Local Database:** [Room](https://developer.android.com/training/data-storage/room) (SQLite)
* **Asynchronous Programming:** Coroutines & Flows
* **PDF Extraction:** PDF Box
* **OCR Extraction:** ML Kit Text Recognition
* **Markdown Rendering:** Markwon

## 📱 Screenshots

| Home Screen | Summarizing | Result / History |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/d10dfa98-bf02-4822-a73e-7fd8dfa9907b" width="250" alt="Home Screen" /> | <img src="https://github.com/user-attachments/assets/ef8433fe-677b-4087-abcc-4856af4d64e3" width="250" alt="Summarizing" /> | <img src="https://github.com/user-attachments/assets/65dc593b-936f-475c-90fd-c793ac82576d" width="250" alt="Result / History" /> |

## ⚙️ Prerequisites & Compatibility

Because this app uses **Gemini Nano**, it relies on the device's NPU (Neural Processing Unit). 
* The app works best on modern flagship devices that support AICore (e.g., Samsung Galaxy S24/S25 series, Google Pixel 8 Pro / 9 series).
* Upon first launch, the ML Kit SDK may need to download the AI model (~1-2GB) via Wi-Fi if it is not already present on the device.

## 💻 Getting Started

1. **Clone the repository:**
   bash
   git clone [https://github.com/Akash-singh9860/PeklaneAI.git](https://github.com/Akash-singh9860/PeklaneAI.git)
2. Open the project in Android Studio (Iguana or newer recommended).
3. Sync Gradle to download all dependencies.
4.Build and Run on a physical device (Emulators may not support the NPU hardware acceleration required for Gemini Nano).

## 🧑‍💻 Author

Akash Singh
GitHub: @Akash-singh9860

## 📜 License
This project is licensed under the MIT License - see the LICENSE file for details.
