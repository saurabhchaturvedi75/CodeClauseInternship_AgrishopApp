package com.example.agrishop.common.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agrishop.common.data.models.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class NotificationViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val notificationsRef = database.getReference("notifications")
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val uid = auth.currentUser?.uid

    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> = _notifications

    init {
        fetchNotifications()
    }


    fun fetchNotifications() {
        notificationsRef.child(uid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val notificationList = mutableListOf<Notification>()
                for (notificationSnapshot in dataSnapshot.children) {
                    val notification = notificationSnapshot.getValue(Notification::class.java)
                    notification?.let { notificationList.add(it) }
                }
                _notifications.value = notificationList
                Log.d("NotificationViewModel", "Fetched ${notificationList.size} notifications")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Failed to fetch notifications: ${databaseError.message}")
            }
        })
    }
}