package com.code.tusome.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.code.tusome.R
import com.code.tusome.databinding.FragmentExamBinding
import com.code.tusome.ui.viewmodels.MainViewModel

class ExamFragment : Fragment() {
    private lateinit var binding:FragmentExamBinding
    private val mainViewModel:MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: Fragment started successfully")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.get
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