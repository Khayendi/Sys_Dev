package com.code.tusome.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.code.tusome.R
import com.code.tusome.databinding.FragmentExamBinding

class ExamFragment : Fragment() {
    private lateinit var binding:FragmentExamBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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