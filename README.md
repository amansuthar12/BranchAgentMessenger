# 📱 Branch Agent Messenger – Android Take-Home Project

*A modern, production-ready Kotlin + Jetpack Compose application*

---

## 📌 Overview

Branch Agent Messenger is a mobile application built to help customer service agents respond to user inquiries “on the go.”
This app interacts with Branch’s messaging REST API (as documented in the take-home PDF) and supports login, message thread viewing, detailed conversation screens, offline usage, and background message synchronization.

This project is built using **Kotlin**, **Jetpack Compose**, **Clean Architecture**, **MVVM**, **Hilt**, **Room**, and **WorkManager** — following modern Android development best practices.

---

## 🚀 Features

### ✅ 1. Secure Login

* Login using **email** + **reversed email** as password (per spec).
* Displays server validation errors.
* Stores session/token persistently using **Encrypted DataStore**.
* Auto-redirects to inbox if logged-in.

### ✅ 2. Message Threads Screen

* Displays **grouped conversations** by `thread_id`.
* Shows **customer ID**, **latest message body**, **timestamp**, and **sender**.
* Supports **pull-to-refresh** to force server sync.
* Offline-first: When offline, data is shown from Room cache.

### ✅ 3. Conversation Screen

* Shows full conversation sorted by timestamp.
* Distinguishes **Agent** vs **User** messages visually.
* Supports composing & sending messages.
* Optimistic UI: Sent messages show immediately with "pending" status.

### ⚡ 4. Offline-First + Background Sync

* All messages cached in **Room**.
* Messages sent while offline are **queued in WorkManager**.
* Automatically syncs once internet reconnects.
* Temporary local IDs (`-1`, `-2`, …) replaced when server confirms.

---

## 🛠️ Tech Stack

| Layer           | Tools / Libraries            |
| --------------- | ---------------------------- |
| Language        | Kotlin                       |
| UI              | Jetpack Compose, Material 3  |
| Architecture    | Clean Architecture + MVVM    |
| DI              | Dagger Hilt                  |
| Networking      | Retrofit, OkHttp Logging     |
| Local Storage   | Room                         |
| Background Work | WorkManager                  |
| Concurrency     | Kotlin Coroutines, StateFlow |
| Testing         | JUnit                        |

---

## 🏗️ Architecture Overview

This project follows **Clean Architecture** with 3 layers:

---

### **1. Presentation Layer (UI + ViewModels)**

* Jetpack Compose screens (`LoginScreen`, `ThreadsScreen`, `ConversationScreen`).
* ViewModels expose `UiState` using **StateFlow**.
* Handles UI events → calls domain use cases.

---

### **2. Domain Layer (Use Cases + Models)**

Pure Kotlin logic.
Key use cases:

| Use Case                   | Responsibility                         |
| -------------------------- | -------------------------------------- |
| `LoginUseCase`             | Authenticates agent & stores token     |
| `GetMessageThreadsUseCase` | Fetch, group, sort threads             |
| `GetThreadMessagesUseCase` | Loads complete conversation            |
| `SendMessageUseCase`       | Creates local message + schedules sync |

Thread grouping logic matches PDF requirements:
Messages are grouped by `thread_id`, customer's `user_id` is used as thread title, and list sorted by latest timestamp.

---

### **3. Data Layer (Repositories + DAO + API)**

Responsible for all data operations.

#### **Network**

* Implements Branch API (`/api/login`, `/api/messages`, etc.) from PDF .

#### **Local**

* Room stores:

  * Messages
  * Threads
  * Pending outgoing messages

#### **Sync Engine**

* WorkManager worker retries sending messages until success.

---

## 🔄 Data Flow

### **Login**

```
UI → ViewModel → LoginUseCase → AuthRepository → Retrofit → Save Token → Navigate to Inbox
```

### **Thread List**

```
UI → ViewModel → GetMessageThreadsUseCase
      → Repository.fetchFromServer()
      → Cache in Room
      → Return combined (server + pending)
UI updates
```

### **Send Message**

```
UI → ViewModel → SendMessageUseCase
      → Create local pending message (temp ID)
      → Save to Room
      → Enqueue WorkManager job
WorkManager → API → On success → Replace temp ID → Refresh messages
```

---

## 🧪 Unit Tests Included

* Domain layer use-case testing (thread grouping logic, login logic).
* Repository mock tests.
* ViewModel state handling tests.

---

## 📂 Project Structure (Simplified)

```
/data
    /api
    /repository
    /room
/domain
    /model
    /usecase
/presentation
    /login
    /threads
    /conversation
/di
/workers
```

---

## 🧰 Setup Instructions

### 1. Clone / unzip project

The submission includes a **full local Git repo** as required.

### 2. Insert your email in Login Screen

Because authentication is email-based per PDF instructions.

### 3. Run the app

Requires:

* Android Studio Ladybug or later
* Min SDK 24
* Target SDK 36

### 4. No environment variables required

All networking handled using Retrofit defaults.

---

## 🔍 Key Decisions & Trade-Offs

### ✔ Offline-first approach

Chosen because agents may work in low-network areas.
Room + WorkManager ensures reliability.

### ✔ Optimistic UI for Sending Messages

Improves user experience and responsiveness.

### ✔ Clean Architecture

Makes code testable, maintainable, and scalable.

### ✔ Hilt for DI

Reduces boilerplate and improves lifecycle handling.

### ✔ Not implementing UI animations

Deprioritized since functionality > beauty for this assessment.

---

## ⚠️ Known Limitations (Honest, professional)

* The app does not include pagination (API does not support it).
* No biometric login (not required by the assignment).
* UI is functional but not deeply custom-designed.
* Push notifications not implemented (out of scope for this assessment).

---

## 🎥 Demo Video (What to Record)

Your video should include:

### **1. Login**

* Enter email + reversed email.
* Show success and token persistence.

### **2. Thread List**

* Pull-to-refresh.
* Offline mode demonstration.

### **3. Conversation**

* Send message (online).
* Turn off Wi-Fi → send → show pending spinner.
* Turn Wi-Fi on → auto-sync.

### **4. Code Walkthrough**

* ViewModel
* Repository
* UseCase
* Room Entities
* WorkManager Worker

---

## ✔ Submission Checklist (Matches PDF Requirements)

| Requirement                                  | Status                    |
| -------------------------------------------- | ------------------------- |
| Git repository included                      | ✅                         |
| Login with reversed-password rule            | ✅                         |
| Thread list grouping                         | ✅                         |
| Conversation screen                          | ✅                         |
| Compose UI                                   | ✅                         |
| MVVM + Clean Architecture                    | ✅                         |
| Hilt DI                                      | ✅                         |
| Offline caching via Room                     | ✅                         |
| Background sync via WorkManager              | ✅                         |
| Unit tests                                   | ✅                         |
| README with architecture + setup + decisions | ✅                         |
| Demo video                                   | Pending (You will record) |

---

If you'd like, I can also generate:

✅ A **shorter, recruiter-friendly README**
✅ A **video script** for your 2–3 minute recording
✅ A **Git commit plan** (what commits should look like)
✅ A **cover letter to submit with the project**

Just tell me.
