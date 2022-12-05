package com.code.tusome.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.code.tusome.databinding.FragmentAssignmentsBinding
import com.code.tusome.ui.viewmodels.MainViewModel
import com.code.tusome.utils.Utils

class AssignmentsFragment : Fragment() {
    private lateinit var binding: FragmentAssignmentsBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: Fragment created")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAssignments("",binding.root).observe(viewLifecycleOwner){
            if (it.isEmpty()){
                binding.emptyBoxIv.visibility = VISIBLE
                binding.emptyBoxTv.visibility = VISIBLE
                binding.assignmentRecycler.visibility = GONE
                Utils.snackbar(binding.root,"Very empty")
            }else{
                binding.emptyBoxIv.visibility = GONE
                binding.emptyBoxTv.visibility = GONE
                binding.assignmentRecycler.visibility = VISIBLE
                Utils.snackbar(binding.root,"Not empty")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssignmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private val TAG = AssignmentsFragment::class.java.simpleName
    }
}