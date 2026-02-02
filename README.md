# Pinpoint Works

Pinpoint Works software turns a site plan or yacht's general arrangement (GA) into an interactive work list, providing one central platform to streamline communication and collaborate with teams worldwide for faster project completion.

## Overview

Pinpoint Works is an Android application that transforms architectural plans and yacht general arrangements into interactive, collaborative work lists. The platform enables teams to communicate effectively, track progress, and complete projects more efficiently.

## Features

- **Interactive Work Lists**: Convert site plans and yacht GAs into actionable work items
- **Global Collaboration**: Connect and collaborate with teams worldwide
- **Real-time Communication**: Streamlined communication platform for project teams
- **Project Management**: Centralized platform for tracking and managing project progress

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Hilt
- **Database**: Room
- **Networking**: Retrofit with OkHttp
- **Image Processing**: Coil, Photo Editor, Sketchbook
- **Media**: ExoPlayer for video playback
- **Background Tasks**: WorkManager

## Requirements

- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Java Version**: 11

## Building the Project

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd Pinpointworksnew
   ```

2. Open the project in Android Studio

3. Sync Gradle files

4. Build and run the application

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/sleetworks/serenity/android/newone/
│   │   │   ├── presentation/    # UI components and screens
│   │   │   ├── domain/          # Business logic
│   │   │   └── data/            # Data layer and repositories
│   │   ├── res/                 # Resources (layouts, drawables, etc.)
│   │   └── AndroidManifest.xml
│   ├── test/                    # Unit tests
│   └── androidTest/             # Instrumented tests
```

## Configuration

The app supports multiple environments configured in `build.gradle.kts`:
- Production: `https://app.pinpointworks.com/`
- Development: `https://dev.pinpointworks.com/`
- Release: `https://release.pinpointworks.com/`
- ASM Yachts: `https://pinpoint.asm-yachts.com/`

## Permissions

The app requires the following permissions:
- Internet access
- Network state
- Camera
- External storage (read/write)
- Media images (Android 13+)

## License


## Contact

For questions or support, please contact the development team.
