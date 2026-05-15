package com.example.shalenammapride.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shalenammapride.data.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

enum class AppLanguage { KANNADA, ENGLISH }

class FirebaseViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance("https://shalenammapride-cb30c-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val storage = FirebaseStorage.getInstance("gs://shalenammapride-cb30c.firebasestorage.app")
    private val auth = FirebaseAuth.getInstance()
    
    private val announcementsRef = database.getReference("announcements")
    private val reportsRef = database.getReference("reports")
    private val galleryRef = database.getReference("gallery")
    private val mealsRef = database.getReference("meals")
    private val starsRef = database.getReference("stars")
    private val feedbackRef = database.getReference("feedback")
    private val attendanceRef = database.getReference("attendance")

    private val _language = MutableStateFlow(AppLanguage.KANNADA)
    val language: StateFlow<AppLanguage> = _language

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements

    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports

    private val _gallery = MutableStateFlow<List<GalleryImage>>(emptyList())
    val gallery: StateFlow<List<GalleryImage>> = _gallery

    private val _meals = MutableStateFlow<List<MealUpdate>>(emptyList())
    val meals: StateFlow<List<MealUpdate>> = _meals

    private val _stars = MutableStateFlow<List<StudentStar>>(emptyList())
    val stars: StateFlow<List<StudentStar>> = _stars

    private val _feedbacks = MutableStateFlow<List<Feedback>>(emptyList())
    val feedbacks: StateFlow<List<Feedback>> = _feedbacks

    private val _studentAttendance = MutableStateFlow<List<StudentAttendance>>(emptyList())
    val studentAttendance: StateFlow<List<StudentAttendance>> = _studentAttendance

    private val _todayMeal = MutableStateFlow<MealUpdate?>(null)
    val todayMeal: StateFlow<MealUpdate?> = _todayMeal

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _isUploading.value = false
        val msg = throwable.localizedMessage ?: "Unknown Firebase Error"
        _error.value = "Error: $msg"
        Log.e("FirebaseVM", "Crash prevented", throwable)
    }

    init {
        _isAdmin.value = auth.currentUser != null
        setupRealtimeListeners()
    }

    fun clearError() { _error.value = null }

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.KANNADA) AppLanguage.ENGLISH else AppLanguage.KANNADA
    }

    fun adminLogin(password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (password == "admin123") {
            viewModelScope.launch(exceptionHandler) {
                try {
                    if (auth.currentUser == null) {
                        auth.signInAnonymously().await()
                    }
                    _isAdmin.value = true
                    onSuccess()
                } catch (e: Exception) {
                    Log.e("FirebaseVM", "Login Error", e)
                    _isAdmin.value = true // demo fallback
                    onSuccess()
                }
            }
        } else {
            onError("Incorrect Admin Password")
        }
    }

    fun logout() {
        auth.signOut()
        _isAdmin.value = false
    }

    private fun setupRealtimeListeners() {
        announcementsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(Announcement::class.java) }
                _announcements.value = list.sortedByDescending { it.timestamp }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        reportsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(Report::class.java) }
                _reports.value = list.sortedByDescending { it.timestamp }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        galleryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(GalleryImage::class.java) }
                _gallery.value = list.sortedByDescending { it.timestamp }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        mealsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(MealUpdate::class.java) }
                val sorted = list.sortedByDescending { it.timestamp }
                _meals.value = sorted
                _todayMeal.value = sorted.find { it.date == dateFormatter.format(Date()) }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        starsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _stars.value = snapshot.children.mapNotNull { it.getValue(StudentStar::class.java) }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        feedbackRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _feedbacks.value = snapshot.children.mapNotNull { it.getValue(Feedback::class.java) }.sortedByDescending { it.timestamp }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        attendanceRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _studentAttendance.value = snapshot.children.mapNotNull { it.getValue(StudentAttendance::class.java) }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private suspend fun uploadFile(uri: Uri, folder: String): String {
        return try {
            val fileName = "${UUID.randomUUID()}.jpg"
            val ref = storage.reference.child("$folder/$fileName")
            ref.putFile(uri).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("FirebaseVM", "Upload error", e)
            throw e 
        }
    }

    fun addMeal(menuEn: String, menuKn: String, imageUri: Uri? = null) {
        viewModelScope.launch(exceptionHandler) {
            _isUploading.value = true
            val today = dateFormatter.format(Date())
            val id = _todayMeal.value?.id ?: mealsRef.push().key ?: return@launch
            var imageUrl = _todayMeal.value?.imageUrl ?: ""
            
            if (imageUri != null) {
                imageUrl = uploadFile(imageUri, "meals")
            }
            
            val meal = MealUpdate(id, menuEn, menuKn, imageUrl, today, System.currentTimeMillis())
            mealsRef.child(id).setValue(meal).await()
            _isUploading.value = false
        }
    }

    fun addAnnouncement(titleEn: String, titleKn: String, contentEn: String, contentKn: String, type: String) {
        viewModelScope.launch(exceptionHandler) {
            val id = announcementsRef.push().key ?: return@launch
            val ann = Announcement(id, titleEn, titleKn, contentEn, contentKn, type, System.currentTimeMillis())
            announcementsRef.child(id).setValue(ann).await()
        }
    }

    fun addReport(titleEn: String, titleKn: String, fileUri: Uri? = null) {
        viewModelScope.launch(exceptionHandler) {
            _isUploading.value = true
            val id = reportsRef.push().key ?: return@launch
            var fileUrl = ""
            if (fileUri != null) {
                try {
                    fileUrl = uploadFile(fileUri, "reports")
                } catch (e: Exception) {
                    fileUrl = "demo_url" // Fallback
                }
            }
            val report = Report(id, titleEn, titleKn, fileUrl, "PDF", "1.2 MB", System.currentTimeMillis())
            reportsRef.child(id).setValue(report).await()
            _isUploading.value = false
        }
    }

    fun addGalleryImage(descEn: String, descKn: String, uri: Uri) {
        viewModelScope.launch(exceptionHandler) {
            _isUploading.value = true
            val id = galleryRef.push().key ?: return@launch
            val url = uploadFile(uri, "gallery")
            val img = GalleryImage(id, url, descEn, descKn, System.currentTimeMillis())
            galleryRef.child(id).setValue(img).await()
            _isUploading.value = false
        }
    }

    fun deleteMeal(meal: MealUpdate) = mealsRef.child(meal.id).removeValue()
    
    fun addStudentStar(name: String, ach: String, uri: Uri?) = viewModelScope.launch(exceptionHandler) {
        _isUploading.value = true
        val id = starsRef.push().key ?: return@launch
        var url = ""
        if (uri != null) url = uploadFile(uri, "stars")
        starsRef.child(id).setValue(StudentStar(id, name, ach, url)).await()
        _isUploading.value = false
    }

    fun deleteStudentStar(id: String) = starsRef.child(id).removeValue()

    fun addFeedback(comment: String, anon: Boolean, user: String) {
        val id = feedbackRef.push().key ?: return
        feedbackRef.child(id).setValue(Feedback(id, if (anon) "Anonymous" else user, comment, anon, System.currentTimeMillis()))
    }

    fun addAttendance(name: String, grade: String, status: String) {
        val id = attendanceRef.push().key ?: return
        attendanceRef.child(id).setValue(StudentAttendance(id, name, grade, status, dateFormatter.format(Date())))
    }
}
