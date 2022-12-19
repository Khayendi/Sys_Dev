package com.code.tusome.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.code.tusome.models.Course
import com.code.tusome.models.Exam
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class ExamViewModel(application: Application) : AndroidViewModel(application) {
    private var examStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var examDeleteStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var examsLiveData: MutableLiveData<List<Exam>?> = MutableLiveData()
    private var examUpdateStatus: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * @author Rosemary Khayendi
     * @param exam The exam that is to be uploaded to the database
     * -> This method uploads an exam to the database
     */
    fun addExam(exam: Exam, course: String): LiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/exams/${course}")
                .push().setValue(exam)
                .addOnSuccessListener {
                    examStatus.postValue(true)
                }.addOnFailureListener {
                    examStatus.postValue(false)
                }
        }
        return examStatus
    }

    /**
     * @author Rosemary Khayendi
     * @param course The course you want to get the exams for
     * -> This method fetches exams associated to a particular course
     */
    fun getExams(course: String): MutableLiveData<List<Exam>?> {
        viewModelScope.launch {
            val exams = ArrayList<Exam>()
            FirebaseDatabase.getInstance().getReference("/exams/${course}")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val ex = it.getValue(Exam::class.java)
                            if (ex != null) {
                                exams.add(ex)
                            }
                        }
                        examsLiveData.postValue(exams)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        examsLiveData.postValue(null)
                    }
                })
        }
        return examsLiveData
    }

    /**
     * @author Rosemary Khayendi
     * @param course Course for which you want ot update the exam
     * @param exam The examination for which you want to update
     * -> This method updates the exam for the course which is parsed to it
     */
    fun updateExam(course: String, exam: Exam): LiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/exams/${course}")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val ex = it.getValue(Exam::class.java)
                            if (ex != null) {
                                if (ex.uid == exam.uid) {
                                    FirebaseDatabase.getInstance()
                                        .getReference("/exams/${course}")
                                        .setValue(exam)
                                        .addOnSuccessListener {
                                            examUpdateStatus.postValue(true)
                                        }
                                        .addOnFailureListener {
                                            examUpdateStatus.postValue(false)
                                        }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        examUpdateStatus.postValue(false)
                    }
                })
        }
        return examUpdateStatus
    }

    /**
     * @author Jamie Omndi
     * @param course The course for which you want to delete the exam
     * @param exam The exam you want to delete
     * -> This method deletes the exam entity which is parsed to it from the system
     */
    fun deleteExam(exam: Exam, course: String): LiveData<Boolean> {
        viewModelScope.launch {
            FirebaseDatabase.getInstance().getReference("/exams/${course}/${exam.uid}")
                .setValue(null)
                .addOnSuccessListener {
                    examDeleteStatus.postValue(true)
                }
                .addOnFailureListener {
                    examUpdateStatus.postValue(false)
                }
        }
        return examDeleteStatus
    }

}