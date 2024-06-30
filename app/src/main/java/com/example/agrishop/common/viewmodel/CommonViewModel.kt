package com.example.agrishop.common.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommonViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")

    fun getUsernameByUid(uid: String, callback: (String?) -> Unit) {
        usersRef.child(uid).child("fullName")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val username = dataSnapshot.getValue(String::class.java)
                    callback(username)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(null)
                }
            })
    }
}
