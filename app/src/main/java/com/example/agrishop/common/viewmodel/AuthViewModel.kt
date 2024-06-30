package com.example.agrishop.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: LiveData<FirebaseUser?> = _firebaseUser

    init {
        _firebaseUser.value = auth.currentUser

    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _loginState

    fun signUp(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        userType: String,
        farmName: String?,
        farmAddress: String?,
        farmDescription: String?,
        businessRegNumber: String?,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _firebaseUser.postValue(auth.currentUser)

                        val userId = auth.currentUser?.uid ?: ""
                        val userMap = mutableMapOf<String, Any>(
                            "fullName" to fullName,
                            "email" to email,
                            "phoneNumber" to phoneNumber,
                            "userType" to userType
                        )
                        if (userType == "Farmer") {
                            userMap["farmName"] = farmName ?: ""
                            userMap["farmAddress"] = farmAddress ?: ""
                            userMap["farmDescription"] = farmDescription ?: ""
                            userMap["businessRegNumber"] = businessRegNumber ?: ""
                        }
                        database.reference.child("users").child(userId).setValue(userMap)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    onResult(true, null)
                                } else {
                                    onResult(false, dbTask.exception?.message)
                                }
                            }
                    } else {
                        onResult(false, task.exception?.message)
                    }
                }
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }


    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)

                    _loginState.value = LoginState.Success
                } else {
                    _loginState.value = LoginState.Error(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun logout() {
        auth.signOut()
        _firebaseUser.postValue(null)
        _loginState.value = LoginState.Initial
    }

    fun getUserType(onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                database.reference.child("users").child(userId).child("userType")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val userType = snapshot.getValue(String::class.java)
                            onResult(userType)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            onResult(null)
                        }
                    })
            } else {
                onResult(null) // No user logged in
            }
        }
    }
}

sealed class LoginState {
    object Initial : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val errorMessage: String) : LoginState()
}


