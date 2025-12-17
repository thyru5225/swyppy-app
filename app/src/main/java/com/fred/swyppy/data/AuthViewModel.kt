package com.fred.swyppy.data

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.fred.swyppy.models.User
import com.fred.swyppy.navigation.ROUTE_ADD_PROPERTY
import com.fred.swyppy.navigation.ROUTE_MAIN_SCREEN
import com.fred.swyppy.navigation.ROUT_LOGIN
import com.fred.swyppy.navigation.ROUT_REGISTER
import com.fred.swyppy.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel(var navController: NavController, var context: Context){
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val sessionManager = SessionManager(context)  // 🆕 ADD THIS

    //Register
    fun signup(username:String, email:String, password:String, confirmpassword:String){

        if (email.isBlank() || password.isBlank() || confirmpassword.isBlank()) {
            Toast.makeText(context,"Please email and password cannot be blank", Toast.LENGTH_LONG).show()
        } else if (password != confirmpassword) {
            Toast.makeText(context,"Password do not match", Toast.LENGTH_LONG).show()
        } else {

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if (it.isSuccessful){

                    val uid = mAuth.currentUser!!.uid

                    // DEFAULT role = "user"
                    val role = "user"

                    val userdata = User(
                        username = username,
                        email = email,
                        password = password,
                        uid = uid,
                        role = role
                    )

                    val regRef = FirebaseDatabase.getInstance().getReference("Users/$uid")

                    regRef.setValue(userdata).addOnCompleteListener { result ->

                        if (result.isSuccessful){
                            // 🆕 SAVE SESSION AFTER SIGNUP
                            sessionManager.saveLoginSession(
                                userId = uid,
                                email = email,
                                name = username,
                                role = role
                            )
                            Toast.makeText(context, "Registered Successfully", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "${result.exception!!.message}", Toast.LENGTH_LONG).show()
                            navController.navigate(ROUT_REGISTER)
                        }

                    }

                } else {
                    navController.navigate(ROUT_REGISTER)
                }
            }
        }
    }

    // 🆕 UPDATED signIn with session saving
    fun signIn(
        email: String,
        password: String,
        rememberMe: Boolean = true,  // 🆕 ADD THIS PARAMETER
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onFailure("Please enter email and password.")
            return
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = mAuth.currentUser!!.uid

                val userRef = FirebaseDatabase.getInstance().getReference("Users/$uid")

                userRef.get().addOnSuccessListener { snapshot ->
                    val role = snapshot.child("role").value?.toString() ?: "user"
                    val username = snapshot.child("username").value?.toString() ?: "User"

                    // 🆕 SAVE SESSION IF REMEMBER ME IS CHECKED
                    if (rememberMe) {
                        sessionManager.saveLoginSession(
                            userId = uid,
                            email = email,
                            name = username,
                            role = role
                        )
                    }

                    onSuccess(role)
                }.addOnFailureListener {
                    onFailure("Login success, but failed to fetch user role.")
                }

            } else {
                onFailure(task.exception?.message ?: "Login failed.")
            }
        }
    }

    // 🆕 UPDATED logout with session clearing
    fun logout(){
        sessionManager.clearSession()  // 🆕 CLEAR SESSION
        mAuth.signOut()
        navController.navigate(ROUT_LOGIN) {
            popUpTo(0) { inclusive = true }
        }
    }

    fun isLoggedIn(): Boolean = mAuth.currentUser != null

    // 🆕 ADD THIS - Check if session is saved
    fun hasValidSession(): Boolean {
        return sessionManager.isLoggedIn()
    }

    // 🆕 ADD THIS - Get saved role
    fun getSavedRole(): String {
        return sessionManager.getUserRole()
    }
}