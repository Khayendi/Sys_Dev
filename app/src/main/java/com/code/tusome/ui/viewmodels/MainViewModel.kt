package com.code.tusome.ui.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.code.tusome.Tusome
import com.code.tusome.db.TusomeDao
import com.code.tusome.models.Role
import com.code.tusome.models.User
import com.code.tusome.ui.fragments.auth.frags.SignUpFragment.Companion.TAG
import com.code.tusome.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * @author Rosemary Khayendi
 * @constructor This takes an application instance as input
 * @param application instance of the application
 * @constructor This is the main view-model where the magic happens
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var loginStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var registerStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var userLiveData:MutableLiveData<User?> = MutableLiveData()
    private var exists:Boolean = false


    /**
     * This is injected by help of dagger2 dependency injection
     */
    @Inject
    lateinit var tusomeDao: TusomeDao

    init {
        (application as Tusome).getRoomComponent().injectMainViewModel(this)
    }

    /**
     * @author Rosemary Khayendi
     * @param email provided by the user
     * @param password provided by the user
     * -> This guy performs login functionality for the current user
     */
    fun login(email: String, password: String): LiveData<Boolean> {
        viewModelScope.launch {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    loginStatus.postValue(true)
                }.addOnFailureListener {
                    loginStatus.postValue(false)
                }
        }
        return loginStatus
    }

    /**
     * @author Rosemary Khayendi
     * @return Returns the registration status of the current session
     * @param username provided by the user
     * @param email provided by the user
     * @param password provided by the user
     * @param imageUri is the URI of the selected image from gallery
     * @param view is any vie in the context of the parent
     * @param role is the role of the current user
     * -> This guy send image to firebase storage bucket and the stores the user data in the realtime db
     */
    fun register(
        username: String,
        email: String,
        password: String,
        imageUri: Uri,
        role: Role,
        view: View
    ): LiveData<Boolean> {
        viewModelScope.launch {
            if (!exists){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val fileName = UUID.randomUUID().toString()
                    val ref = FirebaseStorage.getInstance().getReference("images/$fileName")
                    ref.putFile(imageUri).addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            val uid = FirebaseAuth.getInstance().uid
                            val user = User(uid!!, username, email, imageUrl, role)
                            if (checkIfUserIsInDB(view,email))
                                Log.i(TAG, "register: Info already in DB")
                            else
                                FirebaseDatabase.getInstance().getReference("users/$uid")
                                    .setValue(user).addOnSuccessListener {
                                        registerStatus.postValue(true)
                                        Utils.snackBar(view, "Details saved successfully")
                                    }.addOnFailureListener {
                                        registerStatus.postValue(false)
                                        Utils.snackBar(view, it.message.toString())
                                    }
                        }.addOnFailureListener {
                            registerStatus.postValue(false)
                            Utils.snackBar(view,it.message!!)
                            throw it
                        }
                    }
                }.addOnFailureListener {
                    registerStatus.postValue(false)
                    Utils.snackBar(view, it.message.toString())
                }}else{
                    Utils.snackBar(view,"User already exist")
            }
        }
        return registerStatus
    }

    /**
     * @author James Omondi
     * @param view View where snackbar will be attached
     * @param email Email we want to check is in DB or not
     */
    private fun checkIfUserIsInDB(view: View,email: String):Boolean{
        FirebaseDatabase.getInstance().getReference("users/")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{
                        val user = it.getValue(User::class.java)
                        if (user!=null){
                            if (user.email==email){
                                exists = true
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Utils.snackBar(view,error.message)
                }
            })
        return exists
    }

    /**
     * @author Rosemary Khayendi
     * @param uid This is the user id of the logged in user
     */
    fun getUser(uid:String): MutableLiveData<User?> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/users")
                .addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val user = it.getValue(User::class.java)
                            if (user!=null){
                                userLiveData.postValue(user)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        userLiveData.postValue(null)
                    }
                })
        }
        return userLiveData
    }










}