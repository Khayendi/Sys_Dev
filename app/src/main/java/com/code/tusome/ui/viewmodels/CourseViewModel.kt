package com.code.tusome.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.code.tusome.models.Course
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class CourseViewModel(application: Application) : AndroidViewModel(application) {
    private var courseStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var courseUpdateStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var coursesLiveData: MutableLiveData<List<Course>?> = MutableLiveData()
    private var deleteCourseStatus: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * @author Jamie Omondi
     * @param course The course that is to be added to the database
     * -> This method is responsible for adding cost to the database
     */
    fun addCourse(course: Course): LiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/courses/${course.courseCode}")
                .setValue(course)
                .addOnSuccessListener {
                    courseStatus.postValue(true)
                }.addOnFailureListener {
                    courseStatus.postValue(false)
                }
        }
        return courseStatus
    }

    /**
     * @author Jamie Omondi
     * This method is responsible for getting all courses
     */
    fun getAllCourses(): MutableLiveData<List<Course>?> {
        val courses = ArrayList<Course>()
        courses.clear()
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/courses")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val course = it.getValue(Course::class.java)
                            if (course != null) {
                                courses.add(course)
                            }
                        }
                        coursesLiveData.postValue(courses)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        coursesLiveData.postValue(null)
                    }
                })
        }
        return coursesLiveData
    }

    /**
     * @author Jamie Omondi
     * @param course The course that is to be added to the database
     * -> This method is responsible for updating course in the database
     */
    fun updateCourse(course: Course): LiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/courses/${course.courseCode}")
                .setValue(course)
                .addOnSuccessListener {
                    courseUpdateStatus.postValue(true)
                }.addOnFailureListener {
                    courseUpdateStatus.postValue(false)
                }
        }
        return courseStatus
    }

    /**
     * @author Jamie Omondi
     * @param course The course that is to be added to the database
     * -> This method is responsible for deleting course in the database
     */
    fun deleteCourse(course: Course): LiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/courses/${course.courseCode}")
                .setValue(null)
                .addOnSuccessListener {
                    deleteCourseStatus.postValue(true)
                }.addOnFailureListener {
                    deleteCourseStatus.postValue(false)
                }
        }
        return courseStatus
    }
}