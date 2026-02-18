```markdown
# 📱 Android MVVM Posts App

A modern Android application demonstrating **MVVM architecture** with **Jetpack Compose**, **Kotlin Coroutines**, and **Navigation Component**.
---

## ✨ Features

- ✅ MVVM Architecture
- ✅ Jetpack Compose UI
- ✅ Navigation Component with Arguments
- ✅ Kotlin Coroutines & StateFlow
- ✅ Sealed Classes for State Management
- ✅ Hilt Dependency Injection
- ✅ Retrofit API Integration
- ✅ Error Handling with Retry
- ✅ Material 3 Design

---

## 🏗️ Architecture

```
┌─────────────────────────────┐
│   Presentation Layer        │
│   Screen → ViewModel → UI   │
└──────────────┬──────────────┘
               ↓
┌─────────────────────────────┐
│   Data Layer                │
│   Repository → API Service  │
└─────────────────────────────┘
```

---

## 📂 Project Structure

```
app/
├── data/
│   ├── model/Post.kt
│   ├── network/ApiService.kt
│   └── repository/PostRepository.kt
├── ui/
│   ├── screens/
│   │   ├── posts/PostsScreen.kt
│   │   └── detail/DetailScreen.kt
│   ├── components/PostCard.kt
│   └── navigation/AppNavigation.kt
├── viewmodel/PostViewModel.kt
└── MainActivity.kt
```

---

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM |
| DI | Hilt |
| Networking | Retrofit + OkHttp |
| State | StateFlow |
| Navigation | Navigation Compose |

---

## 🚀 Getting Started

1. Clone the repository
```bash
https://github.com/abdulqadirtr/android-mvvm-api-fetch-data.git
```

2. Open in Android Studio

3. Sync Gradle and Run

---

## 🔑 API

**Base URL:** `https://jsonplaceholder.typicode.com/`

| Endpoint | Description |
|----------|-------------|
| GET /posts | Fetch all posts |
| GET /posts/{id} | Fetch single post |

---

## 📱 Screens

### Posts List
- Displays all posts
- Click to navigate to detail
- Error handling with retry

### Post Detail
- Shows full post content
- Back navigation
- Receives postId via navigation argument

---

## 🧩 Key Implementations

### State Management
```kotlin
sealed interface PostsUiState {
    data object Loading : PostsUiState
    data object Empty : PostsUiState
    data class Success(val posts: List<Post>) : PostsUiState
    data class Error(val message: String) : PostsUiState
}
```

### Navigation
```kotlin
composable("detail/{postId}") { backStackEntry ->
    val postId = backStackEntry.arguments?.getInt("postId") ?: 0
    DetailScreen(postId = postId)
}
```

---

## 👤 Author

**Abdul Qadir**


⭐ Star this repo if you found it helpful!
```
