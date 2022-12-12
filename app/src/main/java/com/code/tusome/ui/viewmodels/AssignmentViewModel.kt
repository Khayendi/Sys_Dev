package com.code.tusome.ui.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.code.tusome.Tusome
import com.code.tusome.db.TusomeDao
import com.code.tusome.models.Assignment
import com.code.tusome.models.AssignmentDB
import com.code.tusome.utils.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import javax.inject.Inject

class AssignmentViewModel(application: Application) : AndroidViewModel(application) {
    private var assignmentUploadStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var assignments: MutableLiveData<List<Assignment>> = MutableLiveData()
    private var updateAssignmentStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var deleteAssignmentStatus: MutableLiveData<Boolean> = MutableLiveData()

    @Inject
    lateinit var tusomeDao: TusomeDao

    init {
        (application as Tusome).getRoomComponent().injectAssignmentViewModel(this)
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
                    Utils.snackBar(view, "Assignment uploaded successfully")
                }.addOnFailureListener {
                    Utils.snackBar(view, it.message.toString())
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
                        Utils.snackBar(view, error.message)
                    }
                })
            assigno.forEach { assignment ->
                tusomeDao.saveAssignment(
                    AssignmentDB(
                        0,
                        assignment.uid,
                        assignment.description,
                        assignment.unitName,
                        assignment.issueDate,
                        assignment.dueDate,
                        assignment.submitted
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
                assignment.description,
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