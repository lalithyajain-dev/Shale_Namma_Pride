# 📚 Shale–Namma Pride

**Shale–Namma Pride** is a bilingual (Kannada & English) Android application developed to improve transparency, communication, and engagement between government schools and parents, especially in rural Karnataka.

The app provides real-time updates about school meals, attendance, student achievements, facilities, announcements, and timetables while enabling anonymous parent feedback.

---

## 🚀 Features

### 🏠 Home Dashboard
- School overview
- Daily meal highlight
- Quick access navigation
- School information

### 🍱 Daily Meal Updates
- View daily meal menu
- Kannada & English support
- Meal timestamp
- Firebase Realtime Database integration

### 🏫 Facilities Tour
- School facility showcase
- Swipeable image cards
- Facility descriptions
- Demo images for school infrastructure

### 🌟 Student Stars
- Student achievements showcase
- Real-time updates
- Celebration cards

### 📅 Timetable
- Grade-wise timetable
- Weekly class schedule
- Multiple grades support

### 📢 Announcements
- PTA meeting notices
- Exam updates
- Holiday notifications
- School reminders

### 📊 Attendance
- Grade-wise attendance
- Present/Absent count
- Monthly attendance summary

### 💬 Anonymous Feedback
- Parent feedback submission
- Anonymous toggle support
- Secure Firebase integration

### 🌐 Language Support
- Kannada & English toggle
- Dynamic language switching
- User-friendly bilingual experience

---

## 🛠️ Tech Stack

### Frontend
- **Kotlin**
- **Jetpack Compose**
- **Material 3 Design**

### Architecture
- **MVVM (Model-View-ViewModel)**

### Backend
- **Firebase Realtime Database**

### Development Tools
- **Android Studio**
- **Gradle**
- **GitHub**

---

## 📂 Project Structure

```plaintext
ShaleNammaPride/
│── app/
│── ui/
│── screens/
│── viewmodel/
│── firebase/
│── data/
│── build.gradle.kts
│── settings.gradle.kts
```

---

## 📱 Screens Included

- Splash Screen
- Home Screen
- Meal Update Screen
- Facility Tour
- Student Stars
- Attendance
- Timetable
- Announcements
- Feedback Box
- School Information

---

## 🔥 Firebase Integration

The application uses **Firebase Realtime Database** for:

- Meal updates
- Student achievements
- Feedback storage
- Attendance data
- Announcements

### Firebase Setup
1. Create Firebase Project
2. Connect Android App
3. Add `google-services.json`
4. Enable Realtime Database
5. Configure rules

Example rules for demo:

```json
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
```

---

## 🎯 Problem Statement

Government schools, especially in rural areas, often face communication gaps between school administration and parents. Parents may not have access to daily school updates, attendance, facilities, meal information, or student achievements.

**Shale–Namma Pride** solves this problem by creating a centralized bilingual platform that improves transparency and strengthens school-parent communication.

---

## 🎯 Objectives

- Improve transparency in government schools
- Increase parental involvement
- Digitize school communication
- Provide real-time updates
- Support Kannada & English accessibility

---

## 📈 Future Scope

- Admin Authentication
- Push Notifications
- Academic Performance Tracking
- Cloud Storage for image uploads
- AI-based attendance insights

---

## 👨‍💻 Developed By

**Lalithya N Jain **  
MindMatrix Ed internship Project 

---
