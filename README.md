```markdown
# Android MVVM Posts App

A modern Android application demonstrating MVVM architecture with Jetpack Compose, Kotlin Coroutines, and Navigation Component.

## Features

- MVVM Architecture
- Jetpack Compose UI
- Navigation Component with Arguments
- Kotlin Coroutines and StateFlow
- Sealed Classes for State Management
- Hilt Dependency Injection
- Retrofit API Integration
- Error Handling with Retry
- Material 3 Design

## Tech Stack

- Language: Kotlin
- UI: Jetpack Compose
- Architecture: MVVM
- DI: Hilt
- Networking: Retrofit + OkHttp
- State: StateFlow
- Navigation: Navigation Compose

## Project Structure

- data/model/ - Data classes
- data/network/ - API service
- data/repository/ - Repository
- ui/screens/posts/ - Posts list screen
- ui/screens/detail/ - Post detail screen
- ui/components/ - Reusable components
- ui/navigation/ - Navigation setup
- viewmodel/ - ViewModel

## Getting Started

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle and Run

## API

Base URL: https://jsonplaceholder.typicode.com/

- GET /posts - Fetch all posts
- GET /posts/{id} - Fetch single post

## Screens

Posts List: Displays all posts with click navigation to detail screen.

Post Detail: Shows full post content with back navigation.

## Key Implementations

State Management: Uses sealed interface for Loading, Empty, Success, and Error states.

Navigation: Passes postId as route parameter between screens.

Error Handling: Comprehensive error handling with retry functionality.

## Author

Abdul Qadir

```
