package com.code.tusome.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.code.tusome.databinding.FragmentExamBinding
import com.code.tusome.models.Course
import com.code.tusome.ui.viewmodels.ExamViewModel

class ExamFragment : Fragment() {
    private lateinit var binding:FragmentExamBinding
    private val examViewModel:ExamViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: Fragment started successfully")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        examViewModel.getExams(Course("","", ArrayList(),"","")).observe(viewLifecycleOwner){
            if (it!!.isEmpty()){
                binding.emptyBoxIv.visibility = VISIBLE
                binding.emptyBoxTv.visibility = VISIBLE
                binding.examsRecycler.visibility = GONE
            }else{
                binding.emptyBoxIv.visibility = GONE
                binding.emptyBoxTv.visibility = GONE
                binding.examsRecycler.visibility = VISIBLE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExamBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
       private val TAG = ExamFragment::class.java.simpleName
    }
}