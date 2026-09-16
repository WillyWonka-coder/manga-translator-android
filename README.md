# Manga Scanlation Studio (Tablet Edition) 📖 🖊️

An open-source AI-assisted manga, manhwa, and manhua translation & inpainting suite designed specifically for **Android tablets with active stylus pens** (Lenovo Xiaoxin Pad / Precision Pen, Samsung Galaxy Tab / S-Pen).

[![Platform: Android](https://img.shields.io/badge/Platform-Android_10+-3DDC84.svg?logo=android&logoColor=white)](https://github.com/WillyWonka-coder/manga-translator-android)
[![Form Factor: Tablet](https://img.shields.io/badge/Optimized_for-11%22--14.6%22_Tablets-FF6F00.svg)](https://github.com/WillyWonka-coder/manga-translator-android)
[![Input: Active Stylus](https://img.shields.io/badge/Stylus-Palm_Rejection_%26_Button_Support-blue.svg)](https://github.com/WillyWonka-coder/manga-translator-android)
[![CI Build](https://img.shields.io/badge/Cloud_Build-GitHub_Actions-2088FF.svg?logo=github-actions&logoColor=white)](https://github.com/WillyWonka-coder/manga-translator-android/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

## Overview 🌟

Unlike traditional handheld readers that overlay solid white boxes over comic pages, **Manga Scanlation Studio (Tablet Edition)** is tailored for scanlators, translators, and editors who work directly on tablets. 

It combines **offline ONNX character recognition**, **AI-driven contextual translation**, **intelligent inpainting**, and a **direct online chapter downloader**, all wrapped into a high-resolution workspace optimized for stylus precision.

---

## Key Highlights ✨

### 🖊️ Active Stylus & Palm Rejection
* **Hardware Stylus Awareness:** Differentiates between finger gestures (pan/zoom) and active stylus input (`TOOL_TYPE_STYLUS`).
* **True Palm Rejection:** Rest your hand comfortably on the 12.7" screen while drawing bubble masks or selecting text.
* **Hardware Pen Button Actions:** Press the stylus side button to instantly delete or erase bubble selections with haptic feedback.

### 🎨 Background Inpainting & Outline Typography
* **Smart Texture Inpainting:** Eliminates Asian characters while reconstructing underlying screentones, paper texture, and art gradients.
* **Outline Text Engine:** High-contrast text stroke rendering ensures that translated Ukrainian, English, or Russian dialogue remains crisp and legible over any complex background artwork.
* **Custom Font Support:** Upload your favorite scanlation TTF/OTF typography (Anime Ace, Manga Temple, Kudryashev, etc.) directly in settings.

### 🌐 Online Chapter Fetcher (MangaDex Integration)
* **Built-in Online Search:** Inspired by [Mihon](https://github.com/mihonapp/mihon), search millions of manga and manhwa titles directly in the app.
* **Batch Chapter Downloader:** Pull complete RAW chapters in Japanese (`ja`), Korean (`ko`), or English (`en`) straight into your library without manual file transfers.

### 🤖 LLM Contextual Translation Engine
* **Scanlation-Tuned Prompts:** Built-in translation rules tailored for authentic comic dialogue (natural phrasing, character vocative case in Ukrainian, honorifics, and comic slang).
* **Multi-Provider Support:** Seamlessly connect to **DeepSeek-V3 / R1**, OpenRouter, OpenAI, Claude, or Google Gemini through OpenAI-compatible endpoints.
* **Accumulating Project Glossary:** Automatically detects and tracks character names, locations, and fantasy abilities in `glossary.json` across chapters.

### 📑 Two-Way Proofreading & Script Export
* Export not just clean pages or translated images, but also the **complete bilingual dialogue script (`script_translations.json`)** containing IDs, original text, and translated strings for team proofreading.

---

## Supported Languages 🗺️

| Source Languages | Target Translation | UI Languages |
| :--- | :--- | :--- |
| **Japanese** (Manga, Furigana, Vertical) | **Ukrainian** (Full scanlation rules) | **Українська** |
| **Korean** (Manhwa, Webtoons) | **English** | **English** |
| **Chinese Simplified & Traditional** (Manhua) | **Russian** | **Русский** |
| English, French, Spanish, German, Italian | Portuguese (Brazil), etc. | *Follow System* |

---

## Quick Start 🚀

1. **Download APK:** Download the latest `manga-translator-tablet-debug.apk` directly from the [GitHub Actions Artifacts](https://github.com/WillyWonka-coder/manga-translator-android/actions).
2. **Configure AI Provider:**
   * Navigate to **Settings -> API**.
   * Set API format to **OpenAI Compatible**.
   * Enter your endpoint (e.g. DeepSeek: `https://api.deepseek.com/v1`) and your API Key.
   * Model name: `deepseek-chat` or your preferred model.
3. **Get Manga:**
   * Tap **MangaDex Online** to search and download chapters, or import local files (**CBZ, ZIP, PDF, or folder**).
4. **Translate & Edit:**
   * Tap **Translate All** for full-chapter processing, or read and edit bubble placements on the fly with your stylus.

---

## Building from Source 🧩

Builds are automated via GitHub Actions — no heavy local Android Studio installation required:

1. Fork this repository.
2. Go to **Actions -> Build Android APK for Tablet -> Run workflow**.
3. Download the compiled ARM64 APK from the artifacts in ~2 minutes.

---

## Special Acknowledgments & Deep Gratitude 🙏

This project stands on the shoulders of giants in the open-source manga and machine learning communities. We express our deepest gratitude to:

* **[jedzqer/manga-translator-android](https://github.com/jedzqer/manga-translator-android)** — For the foundational Android client architecture, bubble segmentation pipelines, and mobile ONNX integration.
* **[kha-white/manga-ocr](https://github.com/kha-white/manga-ocr)** — For pioneering end-to-end Vision-Encoder-Decoder OCR models specialized in Japanese manga text.
* **[mihonapp/mihon](https://github.com/mihonapp/mihon)** (formerly Tachiyomi) — For setting the gold standard in mobile manga reading, extension architecture, and online source integration.
* **[PaddlePaddle/PaddleOCR](https://github.com/PaddlePaddle/PaddleOCR)** — For ultra-lightweight, high-accuracy multilingual OCR models that run locally on mobile CPUs.
* **[advimman/lama](https://github.com/advimman/lama)** — For the breakthrough Large Mask Inpainting (LaMa) architecture that powers clean scanlation background restoration.

---

## License 📄

Distributed under the [MIT License](LICENSE).
