package com.code.tusome.ui.viewmodels

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.code.tusome.Tusome
import com.code.tusome.db.TusomeDao
import com.code.tusome.models.*
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
 * @author Jamie Omondi
 * @constructor This takes an application instance as input
 * @param application instance of the application
 * @constructor This is the main view-model where the magic happens
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var loginStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var registerStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var userLiveData:MutableLiveData<User?> = MutableLiveData()


    /**
     * This is injected by help of dagger2 dependency injection
     */
    @Inject
    lateinit var tusomeDao: TusomeDao

    init {
        (application as Tusome).getRoomComponent().injectMainViewModel(this)
    }

    /**
     * @author Jamie Omondi
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
     * @author Jamie Omondi
     * @return Returns the registration status of the current session
     * @param username provided by the user
     * @param email provided by the user
     * @param password provided by the user
     * @param imageUri is the URI of the selected image from gallery
     * @param view is any vie in the context of the parent
     * @param role is the role of the current user
     * @param isAdmin whether the user is an administrator or not
     * -> This guy send image to firebase storage bucket and the stores the user data in the realtime db
     */
    fun register(
        username: String,
        email: String,
        password: String,
        imageUri: Uri,
        role: Role,
        isAdmin: Boolean,
        view: View
    ): LiveData<Boolean> {
        viewModelScope.launch {
            val ref = FirebaseStorage.getInstance().getReference("images/")
            ref.putFile(imageUri)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        val imageUrl = it.toString()
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                val uid = FirebaseAuth.getInstance().currentUser!!.uid
                                val user = User(uid, username, email, imageUrl, role, isAdmin)
                                FirebaseDatabase.getInstance().getReference("users/$uid")
                                    .setValue(user)
                                    .addOnSuccessListener {
                                        Utils.snackBar(view, "Registration successful, Login")
                                        registerStatus.postValue(true)
                                    }.addOnFailureListener {
                                        registerStatus.postValue(false)
                                        Utils.snackBar(view, "Registration Unsuccessful")
                                    }
                            }.addOnFailureListener { e ->
                                registerStatus.postValue(false)
                                Utils.snackBar(view, e.message!!)
                            }
                    }
                }
        }
        return registerStatus
    }

    /**
     * @author Jamie Omondi
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