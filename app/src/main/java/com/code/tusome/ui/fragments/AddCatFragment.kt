package com.code.tusome.ui.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.code.tusome.R
import com.code.tusome.databinding.FragmentAddCatBinding
import com.code.tusome.models.Assignment
import com.code.tusome.models.Cat
import com.code.tusome.ui.viewmodels.CatsViewModel
import com.code.tusome.utils.Utils
import java.util.*

class AddCatFragment : DialogFragment() {
    private lateinit var binding: FragmentAddCatBinding
    private lateinit var catsViewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        catsViewModel = ViewModelProvider(this)[CatsViewModel::class.java]
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        binding.issueDateEt.setOnFocusChangeListener { v, hasFocus ->
            if (v.id == R.id.issue_date_et && hasFocus) {
                datePicker(v as EditText)
            }
        }
        binding.submitBtn.setOnClickListener {
            val unitName = binding.unitNameEt.text.toString().trim()
            val description = binding.descriptionEt.text.toString().trim()
            val course = binding.courseEt.text.toString().trim()
            val issueDate = binding.issueDateEt.text.toString().trim()
            val duration = binding.durationEt.text.toString().trim()
            val invigilator = binding.invigilator.text.toString().trim()
            if (unitName.isBlank() || description.isBlank() || issueDate.isBlank() ||
                invigilator.isBlank() || course.isBlank()) {
                Utils.snackbar(binding.root, "Please fill all fields")
                return@setOnClickListener
            }
            val assignment = Cat(UUID.randomUUID().toString(), unitName,description,course,issueDate,duration,invigilator)
            catsViewModel.addCat(course,assignment).observe(viewLifecycleOwner){
                if(it){
                    Utils.snackbar(binding.root,"CAT added successfully")
                    binding.unitNameEt.setText("")
                    binding.descriptionEt.setText("")
                    binding.courseEt.setText("")
                    binding.issueDateEt.setText("")
                    binding.durationEt.setText("")
                    dismiss()
                }else{
                    Utils.snackbar(binding.root,"Error adding CAT")
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
        binding = FragmentAddCatBinding.inflate(inflater,container,false)
        return binding.root
    }
}