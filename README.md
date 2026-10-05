# SpinTodo

A quick look at PinTodo's basic task management workflow.
<p align="center">
<img width="426" height="930" alt="pintodo-demo" src="https://github.com/user-attachments/assets/7849ffb6-fa20-4db3-9acc-7fed839fed7e" />
</p>

## 🛠️ Built With

- **[Kotlin](https://kotlinlang.org/)** – Primary programming language
- **[Jetpack Compose](https://developer.android.com/jetpack/compose)** – Modern declarative UI toolkit
- **[Material 3](https://m3.material.io/)** – Design system and UI components
- **[Room](https://developer.android.com/training/data-storage/room)** – Local SQLite database for persisting tasks
- **[Kotlin Coroutines & Flow](https://kotlinlang.org/docs/coroutines-overview.html)** – Asynchronous programming and reactive data streams
- **[Android Architecture Components](https://developer.android.com/topic/architecture)** – ViewModel & Lifecycle (MVVM architecture)
- **[AlarmManager](https://developer.android.com/reference/android/app/AlarmManager)** – Exact-time task reminders
- **[NotificationCompat](https://developer.android.com/develop/ui/views/notifications)** – Reminder notifications
- **[SplashScreen API](https://developer.android.com/develop/ui/views/launch/splash-screen)** – Native launch screen
- **[KSP](https://kotlinlang.org/docs/ksp-overview.html)** – Annotation processing for Room

## 📥 Download & Installation

### Requirements
- Android **7.0 (API 24)** or higher

### Install the APK
1. Go to the [**Releases**](https://github.com/phanhung-dev/PinTodo/releases/latest) page.
2. Under **Assets**, download the latest `.apk` file.
3. Open the downloaded file on your Android device.
4. If prompted, allow **Install unknown apps** for your browser or file manager.
5. Tap **Install** and launch **PinTodo**.

> [!NOTE]
> Since the app is not distributed through Google Play, Google Play Protect may show a warning. Tap **More details → Install anyway** to continue.

### Permissions
To receive task reminders on time, please grant the following when prompted:

| Permission | Purpose |
|---|---|
| **Notifications** | Display task reminders (Android 13+) |
| **Alarms & reminders** | Schedule reminders at the exact time (Android 12+) |
| **Run at startup** | Restore scheduled reminders after the device restarts |

### Build from Source
```bash
git clone https://github.com/phanhung-dev/PinTodo.git
```
1. Open the project in the latest version of **Android Studio**.
2. Wait for Gradle to sync.
3. Click **Run ▶** to install the app on an emulator or a physical device.

## 🚧 Project Status

> [!NOTE]
> PinTodo is currently in **active development** (`v0.1.0`). This is a personal learning project, so features and UI may change between releases.

### ✅ Completed
- [x] Create, edit, and delete tasks
- [x] Mark tasks as completed
- [x] Filter tasks: **All**, **Active**, **Completed**
- [x] Task categories and priority levels
- [x] Due date & time picker with a custom calendar
- [x] Reminder options (15 min, 30 min, 1 hour, 1 day before)
- [x] Exact-time reminder notifications
- [x] Local data persistence with Room
- [x] Splash screen

### 🔨 In Progress
- [ ] Edit due date, time, and reminder of existing tasks
- [ ] Repeating tasks (daily, weekly, monthly, yearly)
- [ ] Reliably restore reminders after reboot or when the app is force-stopped

### 📌 Planned
- [ ] Search tasks
- [ ] Sort tasks by due date or priority
- [ ] Dark mode
- [ ] Home screen widget
- [ ] Multi-language support (English / Vietnamese)

### 🐞 Known Issues
- Reminders whose time passes while the device is powered off are skipped.
