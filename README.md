# Branch Agent Messenger (Android)

A production-ready, offline-first mobile application built for Customer Service Agents to manage inquiries on the go. This project serves as a submission for the Branch Android Engineer Intern role.

---

## ✅ Submission Checklist

This project fulfills all requirements outlined in the *Branch Take-Home Project (Version 1.1.3)* document:

| Category | Requirement | Status |
| :--- | :--- | :--- |
| **Core Features** | **Login Screen:** Validates credentials (reversed email) & handles errors. | ✅ |
| | **Session Retention:** Users remain logged in across app launches via DataStore. | ✅ |
| | **Thread List:** Grouped by `thread_id` with latest message preview. | ✅ |
| | **Pull-to-Refresh:** Force fetches messages from server. | ✅ |
| | **Conversation Screen:** Sorted chronological history with distinct message bubbles. | ✅ |
| **Architecture** | **Tech Stack:** 100% Kotlin & Jetpack Compose. | ✅ |
| | **Pattern:** MVVM with Clean Architecture (Presentation, Domain, Data). | ✅ |
| | **Dependency Injection:** Implemented using Dagger Hilt. | ✅ |
| **Quality** | **Unit Tests:** Comprehensive testing for Domain UseCases. | ✅ |
| | **Git History:** Local `.git` repository included with atomic commits. | ✅ |
| **Bonus / "On-the-Go"** | **Offline Caching:** Implemented Room Database for full offline viewing. | ✅ |
| | **Background Sync:** WorkManager implemented for reliable message sending in poor network conditions. | ✅ |
| **Deliverables** | **Demo Video:** 2-3 minute walkthrough included in the zip. | 📹 |

---

## 🏗 Architecture Overview

This application follows **Clean Architecture** principles combined with the **MVVM (Model-View-ViewModel)** pattern to ensure scalability, testability, and clear separation of concerns.

### 1. Presentation Layer (UI)
* **Tech:** Jetpack Compose, Material 3, ViewModels.
* **Styling:** Custom "Branch Teal" premium theme with floating cards and gradient backgrounds.
* **State Management:** ViewModels expose `UiState` (Loading/Success/Error) via `StateFlow`.
* **Interaction:** Implements **Optimistic UI**, showing messages as "Pending" (spinner) immediately before server confirmation.

### 2. Domain Layer (Business Logic)
* **Tech:** Pure Kotlin (No Android dependencies).
* **Responsibility:** Encapsulates business rules.
* **Key Logic:**
  * `GetMessageThreadsUseCase`: Handles the complex logic of grouping raw messages by `thread_id`, identifying the correct Customer Name (ignoring Agent messages), and sorting by the latest timestamp.
  * `LoginUseCase`: Enforces the specific "password must be email reversed" validation rule.

### 3. Data Layer (Data Source)
* **Tech:** Repositories, Retrofit, Room, WorkManager.
* **Responsibility:** Acts as the Single Source of Truth.
* **Sync Strategy:** The Repository coordinates fetching data from the Remote API and caching it in the Local Room Database. It automatically merges "Pending" offline messages with Server data to present a unified list to the UI.

---

## 🛠 Tech Stack

| Component | Library | Purpose |
| :--- | :--- | :--- |
| **Language** | Kotlin | Primary development language. |
| **UI** | Jetpack Compose | Modern declarative UI toolkit (Material 3). |
| **DI** | Dagger Hilt | Dependency Injection for modularity. |
| **Network** | Retrofit + OkHttp | REST API communication. |
| **Local DB** | Room | Offline data persistence (Single source of truth). |
| **Async** | Coroutines & Flow | Asynchronous programming. |
| **Background** | WorkManager | Reliable background task execution for offline sync. |
| **Testing** | JUnit + Mockito | Unit testing Domain logic. |

---

## ⚙️ Setup Instructions

1.  **Extract:** Unzip the project folder (ensure the hidden `.git` folder is included).
2.  **Open:** Open the project in **Android Studio** (Ladybug or newer recommended).
3.  **Sync:** Allow Gradle to sync dependencies.
4.  **Run:** Select an emulator or physical device and click **Run**.
5.  **Login Credentials:**
  * **Username:** `amanallbackup@gmail.com`
  * **Password:** `moc.liamg@pukcabllanama` (Email reversed)

---

## 💡 Key Decisions

### 1. Offline-First Architecture (Room)
I decided to implement a full offline-first architecture. The UI **only** observes the local database. Network calls update the database, which then triggers a UI update. This ensures the app works perfectly even in "Airplane Mode."

### 2. Background Synchronization (WorkManager)
Instead of a simple API call that fails when offline, I used **WorkManager**.
* **Flow:** Send Message -> Save to DB (Status: Pending) -> Enqueue Worker.
* **Result:** The worker automatically sends the message when the internet connection is restored, even if the app is closed.

### 3. Custom Thread Grouping
Since the API returns a flat list of messages, I implemented a dedicated UseCase in the Domain layer to handle grouping. This keeps the ViewModel clean and makes the grouping logic easily testable.

---

## ⚠️ Trade-offs & Known Limitations

* **Pagination:** The app currently fetches all messages at once upon refresh. For a production app with thousands of threads, I would implement `Paging 3` to load data incrementally.
* **Push Notifications:** Real-time updates currently rely on "Pull-to-Refresh" or background sync. Integrating Firebase Cloud Messaging (FCM) would be the next step for instant message delivery.
* **Temporary IDs:** Offline messages use negative temporary IDs to avoid collision with server IDs. These are replaced once the server confirms the message.

---

Thank you for reviewing my submission!