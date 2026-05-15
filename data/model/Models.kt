package com.example.shalenammapride.data.model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

@IgnoreExtraProperties
data class MealUpdate(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("menuEn") @set:PropertyName("menuEn") var menuEn: String = "",
    @get:PropertyName("menuKn") @set:PropertyName("menuKn") var menuKn: String = "",
    @get:PropertyName("imageUrl") @set:PropertyName("imageUrl") var imageUrl: String = "",
    @get:PropertyName("date") @set:PropertyName("date") var date: String = "",
    @get:PropertyName("timestamp") @set:PropertyName("timestamp") var timestamp: Long = System.currentTimeMillis()
)

@IgnoreExtraProperties
data class StudentStar(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("achievement") @set:PropertyName("achievement") var achievement: String = "",
    @get:PropertyName("imageUrl") @set:PropertyName("imageUrl") var imageUrl: String = ""
)

@IgnoreExtraProperties
data class Feedback(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("user") @set:PropertyName("user") var user: String = "Anonymous",
    @get:PropertyName("comment") @set:PropertyName("comment") var comment: String = "",
    @get:PropertyName("anonymous") @set:PropertyName("anonymous") var anonymous: Boolean = true,
    @get:PropertyName("timestamp") @set:PropertyName("timestamp") var timestamp: Long = System.currentTimeMillis()
)

@IgnoreExtraProperties
data class Announcement(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("titleEn") @set:PropertyName("titleEn") var titleEn: String = "",
    @get:PropertyName("titleKn") @set:PropertyName("titleKn") var titleKn: String = "",
    @get:PropertyName("contentEn") @set:PropertyName("contentEn") var contentEn: String = "",
    @get:PropertyName("contentKn") @set:PropertyName("contentKn") var contentKn: String = "",
    @get:PropertyName("type") @set:PropertyName("type") var type: String = "General",
    @get:PropertyName("timestamp") @set:PropertyName("timestamp") var timestamp: Long = System.currentTimeMillis()
)

@IgnoreExtraProperties
data class Report(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("titleEn") @set:PropertyName("titleEn") var titleEn: String = "",
    @get:PropertyName("titleKn") @set:PropertyName("titleKn") var titleKn: String = "",
    @get:PropertyName("format") @set:PropertyName("format") var format: String = "PDF",
    @get:PropertyName("size") @set:PropertyName("size") var size: String = "0 MB",
    @get:PropertyName("fileUrl") @set:PropertyName("fileUrl") var fileUrl: String = "",
    @get:PropertyName("timestamp") @set:PropertyName("timestamp") var timestamp: Long = System.currentTimeMillis()
)

@IgnoreExtraProperties
data class StudentAttendance(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("studentName") @set:PropertyName("studentName") var studentName: String = "",
    @get:PropertyName("grade") @set:PropertyName("grade") var grade: String = "",
    @get:PropertyName("status") @set:PropertyName("status") var status: String = "Present",
    @get:PropertyName("date") @set:PropertyName("date") var date: String = ""
)

data class Teacher(val name: String, val designationEn: String, val designationKn: String, val subjectEn: String, val subjectKn: String)

data class SchoolInfo(
    val hmName: String = "Dr. Manjunath B.S.",
    val addressEn: String = "GHPS Rural, Hassan District, Karnataka",
    val addressKn: String = "ಸರ್ಕಾರಿ ಹಿರಿಯ ಪ್ರಾಥಮಿಕ ಶಾಲೆ, ಹಾಸನ ಜಿಲ್ಲೆ, ಕರ್ನಾಟಕ",
    val contact: String = "+91 9876543210",
    val totalStudents: String = "450+",
    val teachersCount: String = "12",
    val timingsEn: String = "9:30 AM - 4:30 PM",
    val timingsKn: String = "ಬೆಳಿಗ್ಗೆ 9:30 - ಸಂಜೆ 4:30",
    val missionEn: String = "To provide quality education and foster a nurturing environment for every child.",
    val missionKn: String = "ಪ್ರತಿಯೊಂದು ಮಗುವಿಗೆ ಗುಣಮಟ್ಟದ ಶಿಕ್ಷಣ ಮತ್ತು ಪೋಷಣೆಯ ವಾತಾವರಣವನ್ನು ಒದಗಿಸುವುದು."
)
