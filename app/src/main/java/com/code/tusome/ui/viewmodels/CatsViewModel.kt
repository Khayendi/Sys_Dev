package com.code.tusome.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.code.tusome.models.Cat
import com.code.tusome.models.Course
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class CatsViewModel(application: Application) : AndroidViewModel(application) {
    private var catStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var catUpdateStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var deleteStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var catsLiveData: MutableLiveData<List<Cat>?> = MutableLiveData()

    /**
     * @author Jamie Omondi
     * @param cat The cat that is to be added to the database
     * -> This method is responsible for adding cat to the database
     */
    fun addCat(course: Course, cat: Cat): LiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/cats/${course.courseCode}")
                .push().setValue(cat)
                .addOnSuccessListener {
                    catStatus.postValue(true)
                }.addOnFailureListener {
                    catStatus.postValue(false)
                }
        }
        return catStatus
    }

    /**
     * @author Jamie Omondi
     * @param course
     * -> This method get all the cats associated with a unit
     */
    fun getAllCats(course: Course): MutableLiveData<List<Cat>?> {
        val cats = ArrayList<Cat>()
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/cats/${course.courseCode}")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val cat = it.getValue(Cat::class.java)
                            if (cat != null) {
                                cats.add(cat)
                            }
                        }
                        catsLiveData.postValue(cats)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        catsLiveData.postValue(null)
                    }
                })
        }
        return catsLiveData
    }

    /**
     * @author Jamie Omondi
     * @param course
     * @param cat
     * -> This method updates cats associated with a unit
     */
    fun updateCat(course: Course, cat: Cat): MutableLiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/cats/${course.courseCode}/${cat.uid}")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val cat1 = it.getValue(Cat::class.java)
                            if (cat1 != null && cat.uid == cat1.uid) {
                                FirebaseDatabase.getInstance()
                                    .getReference("/cats/${course.courseCode}/${cat.uid}")
                                    .setValue(cat)
                                    .addOnSuccessListener {
                                        catUpdateStatus.postValue(true)
                                    }
                                    .addOnFailureListener {
                                        catUpdateStatus.postValue(false)
                                    }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        catUpdateStatus.postValue(false)
                    }
                })
        }
        return catUpdateStatus
    }

    /**
     * @author Jamie Omondi
     * @param course
     * @param cat
     * -> This method deletes cat associated with a unit
     */
    fun deleteCat(course: Course, cat: Cat): MutableLiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/cats/${course.courseCode}/${cat.uid}")
                .setValue(null)
                .addOnSuccessListener {
                    deleteStatus.postValue(true)
                }
                .addOnFailureListener {
                    deleteStatus.postValue(false)
                }
        }
        return deleteStatus
    }

}