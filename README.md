# GalactiList
# Requirement
create a native mobile app that consumes the
Star Wars API (SWAPI) to display a list of planets.

# Features 
01. Scrollable List of Planets
2. Planet Details Page

# Technologies used
Android SDK
REST API (Consumed through Retrofit2)
JSON Parsing (GSON)
Dependency Injection (Hilt)
Concurrency (Kotlin Coroutines)
Image display - Glide
Navigation - Jetpack - navigation
Unit test - Junit / Mockito

# IDE 
Android Studio Koala | 2024.1.1 Patch 2

# Summery 
## Approach Used for Star Wars App (GalactiList) Development

Followed the MVVM architecture to ensure a well-structured codebase and allow for future scalability. The project is organized as follows:

#### - Dependency Injection: Used Hilt for managing dependencies efficiently.

Project Structure:
#### -UI Components (Activities, Fragments, and ViewModels) are grouped together.
#### -Network Layer includes API endpoints and response data classes.
#### -Dependency Injection related files are in a separate folder.
#### -Repositories and Data Interfaces are structured separately for better maintainability.
#### -Image Handling: Integrated Picksome for fetching random images and used Glide for optimized memory management.
#### -Splash Screen: Implemented the Android system splash screen, which is the recommended approach for a smoother user experience.

# API & Data Handling
The GalactiList app interacts with external services using Retrofit for network requests. The API layer is structured to ensure modularity and maintainability, with clear separation between API endpoints, data models, and the repository layer.
### API Endpoints & Interaction
#### Base URL: https://swapi.dev/api/
### Endpoints Used:
#### GET -  /planets – Fetches a list of Star Wars planets.


# Testing Strategy - ViewModel Testing (Unit Testing)
This validate ViewModel business logic in isolation.

# Tools Used:
- JUnit for assertions.
- Mockito for mocking dependencies.
