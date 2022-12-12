package com.code.tusome.ui.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.code.tusome.R
import com.code.tusome.databinding.FragmentAddExamBinding
import com.code.tusome.models.Exam
import com.code.tusome.ui.viewmodels.ExamViewModel
import com.code.tusome.utils.Utils
import java.util.*

class AddExamFragment : DialogFragment() {
    private lateinit var binding: FragmentAddExamBinding
    private val examViewModel: ExamViewModel by viewModels()
    private lateinit var course: String
    private val listener = object : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            course = parent?.getItemAtPosition(position).toString()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            /**
             * Something is always selected
             */
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.courses,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.courseEt.apply {
            adapter = mAdapter
            onItemSelectedListener = listener
        }
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        binding.issueDateEt.setOnFocusChangeListener { v, hasFocus ->
            if (v.id == R.id.issue_date_et && hasFocus) {
                datePicker(v as EditText)
            }
        }
        binding.submitBtn.setOnClickListener {
            it.isActivated = false
            val unitName = binding.unitNameEt.text.toString().trim()
            val description = binding.descriptionEt.text.toString().trim()
            val issueDate = binding.issueDateEt.text.toString().trim()
            val duration = binding.durationEt.text.toString().trim()
            val invigilator = binding.invigilator.text.toString().trim()
            if (unitName.isBlank() || description.isBlank() || issueDate.isBlank() ||
                invigilator.isBlank() || course.isBlank()) {
                Utils.snackBar(binding.root, "Please fill all fields")
                return@setOnClickListener
            }
            val assignment = Exam(UUID.randomUUID().toString(), unitName,description,course,issueDate,duration,invigilator)
            examViewModel.addExam(assignment,course).observe(viewLifecycleOwner){status->
                if(status){
                    it.isActivated = true
                    Utils.snackBar(binding.root,"Exam added successfully")
                    binding.unitNameEt.setText("")
                    binding.descriptionEt.setText("")
                    binding.issueDateEt.setText("")
                    binding.durationEt.setText("")
                    dismiss()
                }else{
                    it.isActivated = true
                    Utils.snackBar(binding.root,"Error adding Exam")
                    return@observe
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun datePicker(parent: EditText) {
        val calender = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            R.style.Theme_Tusome,
            { view, year, month, dayOfMonth ->
                val date = "$dayOfMonth/${month + 1}/$year"
                view.fitsSystemWindows = false
                view.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                view.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                view.foregroundGravity = Gravity.CENTER_HORIZONTAL
                parent.setText(date)
            },
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddExamBinding.inflate(inflater,container,false)
        return binding.root
    }

}