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
import com.code.tusome.models.Assignment
import com.code.tusome.models.AssignmentDB
import com.code.tusome.models.User
import com.code.tusome.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
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
    private var assignmentUploadStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var assignments: MutableLiveData<List<Assignment>> = MutableLiveData()
    private var updateAssignmentStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var deleteAssignmentStatus: MutableLiveData<Boolean> = MutableLiveData()

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
     * -> This guy send image to firebase storage bucket and the stores the user data in the realtime db
     */
    fun register(
        username: String,
        email: String,
        password: String,
        imageUri: Uri,
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
                                val user = User(uid, username, email, imageUrl)
                                FirebaseDatabase.getInstance().getReference("users/$uid")
                                    .setValue(user)
                                    .addOnSuccessListener {
                                        Utils.snackbar(view, "Registration successful, Login")
                                        registerStatus.postValue(true)
                                    }.addOnFailureListener {
                                        registerStatus.postValue(false)
                                        Utils.snackbar(view, "Registration Unsuccessful")
                                    }
                            }.addOnFailureListener { e ->
                                registerStatus.postValue(false)
                                Utils.snackbar(view, e.message!!)
                            }
                    }
                }
        }
        return registerStatus
    }

    /**
     * @author Jamie Omondi
     * @param assignment this is the assignment to be uploaded to the database
     * @param course This is the course that the assignment belongs to
     * @param view This is any view in the parent view
     * -> This guy uploads the assignment to the database so that it is available to the users
     */
    fun addAssignment(assignment: Assignment, course: String, view: View): LiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/assignments/$course")
                .push().setValue(assignment)
                .addOnSuccessListener {
                    Utils.snackbar(view, "Assignment uploaded successfully")
                }.addOnFailureListener {
                    Utils.snackbar(view, it.message.toString())
                }
        }
        return assignmentUploadStatus
    }


    /**
     * @author Jamie Omondi
     * @param course This is the course for which you want to get its assignments
     * @param view This is any vie in the in the parent view
     * -> This method is in charge of adding an assignment this action is only possible for the admin
     */
    fun getAssignments(course: String, view: View): LiveData<List<Assignment>> {
        val assigno = ArrayList<Assignment>()
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/assignments/$course")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach { element ->
                            val assignment = element.getValue(Assignment::class.java)
                            if (assignment != null) {
                                assigno.add(assignment)
                            }
                        }
                        assignments.postValue(assigno)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Utils.snackbar(view, error.message)
                    }
                })
            assigno.forEach { assignment ->
                tusomeDao.saveAssignment(
                    AssignmentDB(
                        0,
                        assignment.uid,
                        assignment.name,
                        assignment.unitName,
                        assignment.issueDate,
                        assignment.dueDate
                    )
                )
            }
        }
        return assignments
    }

    /**
     * @author Jamie Omondi
     * @param assignment The assignment to be updated
     * -> This method provides admin facility to update an assignment
     */
    fun updateAssignment(assignment: Assignment, course: String): LiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/assignments/$course/${assignment.uid}")
                .setValue(assignment)
                .addOnSuccessListener {
                    updateAssignmentStatus.postValue(true)
                }.addOnFailureListener {
                    updateAssignmentStatus.postValue(false)
                }
            tusomeDao.updateAssign(
                    assignment.uid,
                    assignment.unitName,
                    assignment.name,
                    assignment.issueDate,
                    assignment.dueDate
            )
        }
        return updateAssignmentStatus
    }

    /**
     * @author Jamie Omondi
     * @param assignment The assignment to be deleted
     * @param course The course to which that assignment belongs
     * -> This method deletes the specified assignment from the system
     */
    fun deleteAssignment(assignment: Assignment, course: String): LiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/assignments/$course/${assignment.uid}")
                .setValue(null)
                .addOnSuccessListener {
                    deleteAssignmentStatus.postValue(true)
                }.addOnFailureListener {
                    deleteAssignmentStatus.postValue(false)
                }
            tusomeDao.deleteAssign(assignment.uid)
        }
        return deleteAssignmentStatus
    }

}