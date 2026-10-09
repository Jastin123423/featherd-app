# Feathered Native Android Application

This repository features the **100% native Android application** written in Java/Android SDK (alongside the responsive web client) with unified design systems, real-time API integrations, and native device performance.

---

## 🎨 Visual Identity & Unified Design System
The native Android app reproduces the exact aesthetic of Feathered:
- **Background**: Deep Dark Navy (`#050B18`)
- **Cards & Surfaces**: Midnight Slate (`#0F172A`, `#141E33`) with subtle borders (`#1E293B`)
- **Brand Accent**: Vibrant Carrot Orange (`#F97316`) and Feathered Blue (`#1877F2`)
- **Typography & Icons**: Crisp white headers (`#F8FAFC`) with muted slate secondary labels (`#94A3B8`)
- **Edge-to-Edge System Bars**: Immersive dark navy status and navigation bars matching Android Material Design guidelines.

---

## 🏗️ Native Architecture (`android/app/src/main/java/com/unera/social/`)

### 1. **MainActivity** (`MainActivity.java`)
- Native `AppCompatActivity` controlling fragment routing and bottom navigation.
- Header bar with brand identity, live search, notifications, and direct chat actions.
- Bottom navigation with 5 primary destinations: Home Feed, Reels, Marketplace, Events, and Profile.
- Intelligent hardware back-button handling.

### 2. **Live API Integration** (`api/ApiClient.java`)
- Directly communicates with the live backend at `https://featheredsocial.site/api/`:
  - `GET /api/feeds` & `GET /api/posts`: Live community social feed.
  - `POST /api/posts`: Creates new posts with text and optional media attachments.
  - `POST /api/posts/{id}/react`: Instant optimistic likes and real-time reaction syncing.
  - `GET /api/stories`: Live 24-hour creator stories carousel.
  - `GET /api/reels`: Full-screen vertical video reels.
  - `GET /api/products`: Live marketplace items and search filtering.
  - `GET /api/events`: Upcoming events and RSVP attendance.
- Resilient background thread pool (`ExecutorService`) and main thread UI dispatching.
- Offline-ready fallback cache ensuring zero blank states.

### 3. **Native Fragments & Screens**
- **FeedFragment**: Horizontal Stories carousel with animated gradient borders, "What's on your mind?" create post dialog, pull-to-refresh, post cards with likes, comments bottom sheet, and native Android sharing.
- **ReelsFragment**: Full-height vertical video reel player with PagerSnapHelper, creator badges, heart/comment actions, and sound tags.
- **MarketplaceFragment**: Search bar, category filter pills, and 2-column product grid with price badges.
- **EventsFragment**: Upcoming events with calendar badges, venue details, and "Interested" RSVP toggles.
- **ProfileFragment**: User cover banner, avatar, follower counts, bio, and personal post history.

### 4. **High-Performance Image Caching** (`utils/ImageLoader.java`)
- Memory-efficient `LruCache<String, Bitmap>` cache.
- Async HTTP image downloads with recycling tag protection to prevent list flickering.

---

## 🛠️ Building & Running

### ⚡ Automated GitHub Actions (Build APK & AAB in the Cloud):
The repository includes a production-grade GitHub Actions workflow at `.github/workflows/android-build.yml`.

1. **Push your code to GitHub** on `main` or push a version tag like `v1.0.0`.
2. Go to your GitHub repository and open the **Actions** tab.
3. Select **Android Build & Release (APK & AAB)**.
4. Click **Run workflow** (or let it run automatically on commit/push).
5. Once completed, scroll to the **Artifacts** section to download:
   - **`Feathered-Debug-APK`**: Ready to install directly on any Android device.
   - **`Feathered-Release-APK`**: Production release APK.
   - **`Feathered-GooglePlay-AAB`**: Google Play Store submission package.

### 🔑 Release Signing Secrets (Optional in GitHub Settings → Secrets):
- `ANDROID_KEYSTORE_BASE64`: Base64 string of your `.keystore` / `.jks` file.
- `ANDROID_KEYSTORE_PASSWORD`: Keystore password.
- `ANDROID_KEY_ALIAS`: Key alias.
- `ANDROID_KEY_PASSWORD`: Key password.

*(If no secrets are supplied, the workflow automatically generates functional debug-signed APKs.)*

### Android Studio / Local Gradle:
Open the `/android` directory in **Android Studio** or build via command line:
```bash
cd android
./gradlew assembleDebug
```
The output APK will be generated at:
`android/app/build/outputs/apk/debug/UNERA-debug-1.0.0.apk`
