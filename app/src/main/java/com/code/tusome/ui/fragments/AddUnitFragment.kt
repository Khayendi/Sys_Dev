package com.code.tusome.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.code.tusome.R
import com.code.tusome.databinding.FragmentAddUnitBinding
import com.code.tusome.models.CourseUnit
import com.code.tusome.ui.viewmodels.UnitsViewModel
import com.code.tusome.utils.Utils
import java.util.UUID


class AddUnitFragment : DialogFragment() {
    private lateinit var binding: FragmentAddUnitBinding
    private val unitsViewModel: UnitsViewModel by viewModels()
    private lateinit var selectedCourse: String
    private lateinit var selectedYear: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: fragment started successfully")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.courses,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.courseEt.setAdapter(arrayAdapter)
        binding.courseEt.setOnItemClickListener { parent, view, position, id ->
            selectedCourse = parent.getItemAtPosition(position).toString()
        }
        val year = arrayListOf<String>("1st year", "2nd year", "3rd year", "4th year")
        val yearAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, year)
        binding.yearEt.setAdapter(yearAdapter)
        binding.yearEt.setOnItemClickListener { parent, view, position, id ->
            selectedYear = parent.getItemAtPosition(position).toString()
        }
        binding.submitBtn.setOnClickListener {
            it.isEnabled = false
            val unitName = binding.unitNameEt.text.toString().trim()
            val unitDesc = binding.descriptionEt.text.toString().trim()
            val unitDuration = binding.durationEt.text.toString().trim()
            val unitRoom = binding.lectureRoomEt.text.toString().trim()
            val unitInstructor = binding.instructorEt.text.toString().trim()
            if (unitName.isBlank() || unitDesc.isBlank() || unitDuration.isBlank() ||
                unitRoom.isBlank() || unitInstructor.isBlank() || selectedCourse.isBlank() ||
                selectedYear.isBlank()
            ) {
                Utils.snackBar(binding.root, "Please fill all fields")
                it.isEnabled = true
                return@setOnClickListener
            }
            val code = when (selectedCourse) {
                "Computer Technology" -> {
                    "SCCI"
                }

                "Computer Networks" -> {
                    "SCCJ"
                }

                else -> {
                    "SCII"
                }
            }
            val unit = CourseUnit(
                selectedCourse,
                UUID.randomUUID().toString(),
                unitName,
                unitDesc,
                unitRoom,
                selectedYear,
                unitInstructor,
                selectedYear,
                ArrayList(), ArrayList()
            )
            unitsViewModel.addCourseUnit(code, unit).observe(viewLifecycleOwner) { status ->
                if (status) {
                    Utils.snackBar(binding.root, "Course added successfully")
                } else {
                    Utils.snackBar(binding.root, "Error adding course")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddUnitBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private val TAG = AddUnitFragment::class.java.simpleName
    }
}