package com.example.agrishop.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agrishop.buyer.data.models.Farm
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FarmDetailViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")

    private val _farmDetails = MutableLiveData<Farm?>()
    val farmDetails: MutableLiveData<Farm?> = _farmDetails

    fun fetchFarmDetails(farmUid: String) {
        usersRef.child(farmUid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val farm = snapshot.getValue(Farm::class.java)
                farm?.let {
                    _farmDetails.value = it
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
