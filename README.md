# CryptoWallet

CryptoWallet is an Android application written in Kotlin, demonstrating a basic cryptocurrency wallet concept. The project focuses on clean code structure, separation of concerns, and modern Android development practices.

The application can be used as a foundation for building a full-featured crypto wallet with balance tracking, transaction history, and blockchain integration.

#### 1. **UI Layer (Compose)**
- **Composable Screens** — UI rendering based on state
- **Composable Components** — reusable UI components
- Responsibilities:
  - Displaying UI state (`UiState`)
  - Passing user events (`UiEvent`) to ViewModel
  - Handling navigation

#### 2. **Presentation Layer (ViewModel + UDF)**
- **ViewModel** — screen state management
- **UiState** (`data class`) — immutable UI state
- **UiEvent** (`sealed interface/class`) — user events
- **UiEffect** (optional) — side effects (navigation, messages)

**UDF Flow:**
- Event → ViewModel → State Update → UI Render

#### 3. **Domain Layer (Use Cases)**
- **UseCase/Interactor** — encapsulation of business logic
- Each UseCase handles one specific operation
- Depends on Repository, independent of frameworks

#### 4. **Data Layer**
- **Repository** — single source of truth for data
- **LocalDataSource** — local storage (Room, DataStore)
- **RemoteDataSource** — remote APIs (blockchain providers)

### Tech Stack:
- **UI**: Jetpack Compose, Material 3
- **State Management**: ViewModel, StateFlow, Channels
- **Asynchronous**: Kotlin Coroutines, Flow
- **Architecture**: MVVM, UDF, Clean Architecture principles
- **DI**: Hilt / Koin (optional)
- **Navigation**: Compose Navigation

**Key Architectural Benefits:**
- Clear separation of concerns between layers
- Predictable state management through UDF
- Reactive UI with StateFlow/Compose
- Easily testable components
- Scalable for adding new features
---

## How to Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/outcastzzz/CryptoWallet.git
   ```
2. **Open the project**
- Open Android Studio.
- Select File → Open.
3. **Choose the root folder of the project**
- Sync Gradle
- Wait until Gradle sync finishes.
- Make sure you have a compatible Android SDK installed.
4. **Run the application**
- Select an emulator or physical device.

- Screenshots

# Screenshots will be added here.

**Screen 1**
<img width="200" height="600" alt="image" src="https://github.com/user-attachments/assets/26f0af5f-72c6-4282-b48a-0c843caedb33" />

**Screen 2**
<img width="200" height="600" alt="image" src="https://github.com/user-attachments/assets/1de21f6e-07e0-45a4-9a69-cd7fa2530b72" />

**Screen 3**
<img width="200" height="600" alt="image" src="https://github.com/user-attachments/assets/1d0d8e36-2a24-47eb-9857-57667cf17a94" />
