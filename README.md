# Pinpoint Works

Pinpoint Works software turns a site plan or yacht's general arrangement (GA) into an interactive work list, providing one central platform to streamline communication and collaborate with teams worldwide for faster project completion.

## Overview

Pinpoint Works is an Android application that transforms architectural plans and yacht general arrangements into interactive, collaborative work lists. The platform enables teams to communicate effectively, track progress, and complete projects more efficiently.

## Features

- **Interactive Work Lists**: Convert site plans and yacht GAs into actionable work items
- **Global Collaboration**: Connect and collaborate with teams worldwide
- **Real-time Communication**: Streamlined communication platform for project teams
- **Project Management**: Centralized platform for tracking and managing project progress
- **Offline-First Architecture**: Work seamlessly even without internet connection
- **Intelligent Data Synchronization**: Efficient sync strategies for optimal performance

## Synchronization Functionality

Pinpoint Works implements a comprehensive synchronization system that ensures data consistency across devices and enables seamless offline functionality. The app uses a hybrid sync strategy combining multiple approaches for optimal performance.

### Sync Strategy

The application follows a **hybrid sync strategy** that includes:

1. **Initial Full Sync**: Complete data download on first launch after login
2. **Incremental Sync**: Delta updates based on timestamps for efficient data transfer
3. **On-Demand Sync**: Manual refresh triggers for immediate updates
4. **Offline-First**: Local storage with sync queue for offline operations
5. **Background Sync**: Non-blocking image and avatar downloads

### Sync Types

#### 1. Initial Sync (First Launch)
On first launch after login, the app performs a complete initial sync:
- Downloads all workspaces, sites, shares, and workspace users
- Stores custom fields and user permissions
- Uses parallel API execution for faster sync times
- Establishes baseline data for incremental updates

#### 2. Workspace Data Sync
Refreshes workspace-related information:
- User data and authentication status
- Workspaces, sites, and shares
- Workspace users and permissions
- Custom field definitions
- Executes multiple API calls in parallel for efficiency

#### 3. Point Sync (Incremental)
The most efficient sync type using timestamp-based incremental updates:
- Only fetches changes since last sync timestamp
- Downloads new, updated, and removed points
- Minimizes data transfer for large point lists
- Automatically triggers background image synchronization
- Updates user avatars in the background

#### 4. Point Detail Sync
Comprehensive sync for individual point details:
- Complete point information and metadata
- Comments and comment reactions
- Related site and workspace data
- Supports offline-first: syncs offline changes first when connection is restored
- Parallel API execution for all related data

#### 5. Image Synchronization
Intelligent background image downloading:
- Permission-based filtering (only downloads images user has access to)
- Batch processing to prevent network overload
- Downloads thumbnails (200x200) and originals (bounded 1200px)
- Only downloads missing images to save bandwidth
- Non-blocking background execution

### Offline Functionality

Pinpoint Works supports comprehensive offline operations:

- **Offline Data Storage**: All data is stored locally using Room database
- **Offline Modifications**: Users can create, update, and delete points, comments, and reactions while offline
- **Sync Queue**: Offline changes are queued and automatically synced when connection is restored
- **Conflict Handling**: Offline changes are prioritized and synced before fetching latest server data
- **Supported Offline Operations**:
  - Point field updates
  - Comments and reactions
  - Image and video uploads
  - Custom field modifications

### Sync Performance

The app optimizes sync performance through:

- **Parallel Execution**: Multiple independent API calls execute concurrently
- **Incremental Updates**: Only changed data is transferred, reducing bandwidth usage
- **Batch Processing**: Images and avatars are downloaded in optimized batches
- **Background Processing**: Non-critical syncs (images, avatars) run in background without blocking UI
- **Efficient Data Transfer**: Timestamp-based incremental sync minimizes data transfer for large datasets

### Technical Implementation

- **Async Operations**: Kotlin Coroutines with Flow for asynchronous sync operations
- **Local Database**: Room database for offline storage and data persistence
- **Network Layer**: Retrofit with OkHttp for API communication
- **State Management**: Resource wrapper pattern for loading, success, and error states
- **Background Tasks**: WorkManager for scheduled and background sync operations

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
