package com.code.tusome.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.code.tusome.models.Course
import com.code.tusome.models.CourseUnit
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class UnitsViewModel(application: Application) : AndroidViewModel(application) {
    private var unitsLiveData: MutableLiveData<List<CourseUnit>?> = MutableLiveData()
    private var unitStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var updateUnitStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var deleteUnitStatus: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * @author Jamie Omondi
     * @param unit The unit that is to be added to the database
     * @param course The course which you want to add a unit
     * -> This method adds a unit to selected course
     */
    fun addCourseUnit(course: Course, unit: CourseUnit): LiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("courses/${course.courseCode}/units/${unit.uid}")
                .setValue(unit)
                .addOnSuccessListener {
                    unitStatus.postValue(true)
                }.addOnFailureListener {
                    unitStatus.postValue(false)
                }
        }
        return unitStatus
    }

    /**
     * @author Jamie Omondi
     * @param course The course for which you want to get the units
     * -> This method gets all the units in a course
     */
    fun getUnits(course: String): LiveData<List<CourseUnit>?> {
        val units = ArrayList<CourseUnit>()
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("courses/${course}/units")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val unit = it.getValue(CourseUnit::class.java)
                            if (unit != null) {
                                units.add(unit)
                            }
                        }
                        unitsLiveData.postValue(units)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        unitsLiveData.postValue(null)
                    }
                })
        }
        return unitsLiveData
    }

    /**
     * @author Jamie Omondi
     * @param unit The unit that is to be added to the database
     * @param course The course which you want to update the unit
     * -> This method updates a particular unit for a course
     */
    fun updateUnit(course: String, unit: CourseUnit): LiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("courses/$course/units")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val mUnit = it.getValue(CourseUnit::class.java)
                            if (mUnit!!.uid == unit.uid) {
                                FirebaseDatabase.getInstance()
                                    .getReference("${course}/units/${unit.uid}")
                                    .setValue(unit)
                                    .addOnSuccessListener {
                                        updateUnitStatus.postValue(false)
                                    }
                                    .addOnFailureListener {
                                        updateUnitStatus.postValue(false)
                                    }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        updateUnitStatus.postValue(false)
                    }
                })
        }
        return unitStatus
    }

    /**
     * @author Jamie Omondi
     * @param unit The unit that is to be added to the database
     * @param course The course whose unit you want to delete
     * -> This method deletes the selected unit in a course
     */
    fun deleteUnit(course: String, unit: CourseUnit): LiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("${course}/units/${unit.uid}")
                .setValue(null)
                .addOnSuccessListener {
                    deleteUnitStatus.postValue(true)
                }.addOnFailureListener {
                    deleteUnitStatus.postValue(false)
                }
        }
        return unitStatus
    }
}