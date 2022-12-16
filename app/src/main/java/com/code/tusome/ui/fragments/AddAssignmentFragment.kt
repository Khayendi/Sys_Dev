package com.code.tusome.ui.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.code.tusome.R
import com.code.tusome.databinding.FragmentAddAssignmentBinding
import com.code.tusome.models.Assignment
import com.code.tusome.ui.viewmodels.AssignmentViewModel
import com.code.tusome.utils.Utils
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddAssignmentFragment : DialogFragment() {
    private lateinit var binding: FragmentAddAssignmentBinding
    private val assignmentViewModel: AssignmentViewModel by viewModels()
    private lateinit var selectedCourse: String
    private val listener = object : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedCourse = parent?.getItemAtPosition(position).toString()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            /**
             * Something is always selected
             */
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: fragment started successfully")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.courses,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.courseSpinnerEt.apply {
            adapter = mAdapter
            onItemSelectedListener = listener
        }
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        binding.dueDateEt.setOnFocusChangeListener { v, hasFocus ->
            if (v.id == R.id.due_date_et && hasFocus) {
                datePicker(v as EditText)
            }
        }
        binding.issueDateEt.setOnFocusChangeListener { v, hasFocus ->
            if (v.id == R.id.issue_date_et && hasFocus) {
                datePicker(v as EditText)
            }
        }
        binding.submitBtn.setOnClickListener { btn ->
            btn.isActivated = false
            val unitName = binding.unitNameEt.text.toString().trim()
            val description = binding.descriptionEt.text.toString().trim()
            val issueDate = binding.issueDateEt.text.toString().trim()
            val dueDate = binding.dueDateEt.text.toString().trim()
            if (unitName.isBlank() || selectedCourse.isBlank() || issueDate.isBlank() ||
                dueDate.isBlank() || description.isBlank()) {
                Utils.snackBar(binding.root, "Please fill all fields")
                return@setOnClickListener
            }
            val assignment = Assignment(UUID.randomUUID().toString(), unitName, description, issueDate, dueDate)
            Log.i(TAG, "onViewCreated: ${assignment.toString()}")
            assignmentViewModel.addAssignment(assignment,selectedCourse,binding.root).observe(viewLifecycleOwner){status->
                if(status){
                    btn.isActivated = true
                    Utils.snackBar(binding.root,"Assignment added successfully")
                    binding.unitNameEt.setText("")
                    binding.descriptionEt.setText("")
                    binding.issueDateEt.setText("")
                    binding.dueDateEt.setText("")
                    dismiss()
                }else{
                    btn.isActivated = true
                    Utils.snackBar(binding.root,"Error adding assignment")
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
                view.layoutParams.height = LayoutParams.WRAP_CONTENT
                view.layoutParams.width = LayoutParams.WRAP_CONTENT
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
        binding = FragmentAddAssignmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    companion object{
        private val TAG = AddAssignmentFragment::class.java.simpleName
    }
}