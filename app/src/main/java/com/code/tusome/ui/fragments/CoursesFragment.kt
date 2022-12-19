package com.code.tusome.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.code.tusome.R
import com.code.tusome.adapters.CourseAdapter
import com.code.tusome.databinding.AddCourseLayoutBinding
import com.code.tusome.databinding.FragmentCoursesBinding
import com.code.tusome.models.Course
import com.code.tusome.ui.viewmodels.CourseViewModel
import com.code.tusome.ui.viewmodels.MainViewModel
import com.code.tusome.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

class CoursesFragment : Fragment() {
    private lateinit var binding: FragmentCoursesBinding
    private val courseViewModel:CourseViewModel by viewModels()
    private val mainViewModel:MainViewModel by viewModels()
    private lateinit var selectedCourse:String
    private lateinit var selectedCourseCode:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: fragment started successfully")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getUser(FirebaseAuth.getInstance().uid!!).observe(viewLifecycleOwner){
            if (it!!.role?.roleName=="staff"){
                binding.addCourseFab.visibility = VISIBLE
                binding.addCourseFab.setOnClickListener {
                    val dialogView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.add_course_layout, binding.root, false)
                    val bind = AddCourseLayoutBinding.bind(dialogView)
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                        .setTitle("Add Course")
                        .setView(bind.root)
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                    val arrayAdapterCourse = ArrayAdapter.createFromResource(
                        requireContext(),
                        R.array.courses,
                        android.R.layout.simple_spinner_dropdown_item
                    )
                    bind.courseNameEt.setAdapter(arrayAdapterCourse)
                    val arrayAdapterCourseCode = ArrayAdapter.createFromResource(
                        requireContext(),
                        R.array.course_codes,
                        android.R.layout.simple_spinner_dropdown_item
                    )
                    bind.courseCodeEt.setAdapter(arrayAdapterCourseCode)
                    bind.courseNameEt.setOnItemClickListener { parent, view, position, id ->
                        selectedCourse = parent?.getItemAtPosition(position).toString()
                    }
                    bind.courseCodeEt.setOnItemClickListener { parent, view, position, id ->
                        selectedCourseCode = parent?.getItemAtPosition(position).toString()
                    }
                    bind.cancelBtn.setOnClickListener {
                        alertDialog.cancel()
                    }
                    bind.submitBtn.setOnClickListener {
                        val course = selectedCourse
                        val description = bind.courseDescriptionEt.text.toString().trim()
                        val department = bind.courseDepartmentEt.text.toString().trim()
                        val school = bind.courseSchoolEt.text.toString().trim()
                        if (course.isBlank() || description.isBlank() || department.isBlank() || school.isBlank()) {
                            Utils.snackBar(binding.root, "Fill all fields")
                            return@setOnClickListener
                        }
                        val uid = UUID.randomUUID().toString()
                        val courseModel = Course(uid,selectedCourseCode,ArrayList(),department,description,school)
                        courseViewModel.addCourse(courseModel).observe(viewLifecycleOwner){
                            if (it){
                                Utils.snackBar(binding.root,"Course added successfully")
                                alertDialog.dismiss()
                            }else{
                                Utils.snackBar(binding.root,"Error adding course, ty again")
                            }
                        }
                    }
                }
            }else{
                binding.addCourseFab.visibility = GONE
            }
        }

        courseViewModel.getAllCourses().observe(viewLifecycleOwner) { courseList ->
            if (courseList!!.isEmpty()) {
                binding.emptyBoxIv.visibility = VISIBLE
                binding.emptyBoxTv.visibility = VISIBLE
                binding.coursesRecycler.visibility = GONE
            } else {
                binding.emptyBoxIv.visibility = GONE
                binding.emptyBoxTv.visibility = GONE
                binding.coursesRecycler.visibility = VISIBLE
                val mAdapter = CourseAdapter(courseList)
                binding.coursesRecycler.apply {
                    adapter = mAdapter
                    layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL, false
                    )
                }
                mAdapter.notifyDataSetChanged()
                mAdapter.setOnItemLongCLickListener(object:CourseAdapter.OnItemLongClick{
                    override fun onItemLongClick(position: Int) {
                        val popupMenu = PopupMenu(requireContext(),binding.coursesRecycler.getChildAt(position))
                        popupMenu.menu.add("Edit")
                        popupMenu.menu.add("Delete")
                        popupMenu.show()
                        popupMenu.setOnMenuItemClickListener {
                        if (it.title=="Edit"){
                            openPrefilledDialog(courseList[position])
                        }
                            true
                        }
                    }
                })
            }
        }
    }
    fun openPrefilledDialog(course: Course) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.add_course_layout, binding.root, false)
        val bind = AddCourseLayoutBinding.bind(dialogView)
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle("Update Course")
            .setView(bind.root)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
        val arrayAdapterCourse = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.courses,
            android.R.layout.simple_spinner_dropdown_item
        )
        bind.courseDepartmentEt.setText(course.department)
        bind.courseSchoolEt.setText(course.school)
        bind.courseDescriptionEt.setText(course.description)
        bind.courseNameEt.setAdapter(arrayAdapterCourse)
        val arrayAdapterCourseCode = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.course_codes,
            android.R.layout.simple_spinner_dropdown_item
        )
        bind.courseCodeEt.setAdapter(arrayAdapterCourseCode)
        bind.courseNameEt.setOnItemClickListener { parent, view, position, id ->
            selectedCourse = parent?.getItemAtPosition(position).toString()
        }
        bind.courseCodeEt.setOnItemClickListener { parent, view, position, id ->
            selectedCourseCode = parent?.getItemAtPosition(position).toString()
        }
        bind.cancelBtn.setOnClickListener {
            alertDialog.cancel()
        }
        bind.submitBtn.setOnClickListener {
            val course = selectedCourse
            val description = bind.courseDescriptionEt.text.toString().trim()
            val department = bind.courseDepartmentEt.text.toString().trim()
            val school = bind.courseSchoolEt.text.toString().trim()
            if (course.isBlank() || description.isBlank() || department.isBlank() || school.isBlank()) {
                Utils.snackBar(binding.root, "Fill all fields")
                return@setOnClickListener
            }
            val uid = UUID.randomUUID().toString()
            val courseModel = Course(uid,selectedCourseCode,ArrayList(),department,description,school)
            courseViewModel.addCourse(courseModel).observe(viewLifecycleOwner){
                if (it){
                    Utils.snackBar(binding.root,"Course updated successfully")
                    alertDialog.dismiss()
                }else{
                    Utils.snackBar(binding.root,"Error updating course, try again")
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private val TAG = CoursesFragment::class.java.simpleName
    }
}