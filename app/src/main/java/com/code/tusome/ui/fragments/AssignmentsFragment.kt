package com.code.tusome.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.code.tusome.R
import com.code.tusome.adapters.AssignmentsAdapter
import com.code.tusome.databinding.FragmentAssignmentsBinding
import com.code.tusome.ui.viewmodels.AssignmentViewModel
import com.code.tusome.utils.Utils

class AssignmentsFragment : Fragment() {
    private lateinit var binding: FragmentAssignmentsBinding
    private val viewModel by viewModels<AssignmentViewModel>()
    private lateinit var selectedCourse: String
    private lateinit var selectedUnit: String
    private val courseListener = object : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedCourse = parent?.getItemAtPosition(position).toString()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            /**
             * Something is always selected
             */
        }
    }
    private val unitListener = object : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedUnit = parent?.getItemAtPosition(position).toString()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            /**
             * Something is always selected
             */
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: Fragment created")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val courseAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.courses,android.R.layout.simple_spinner_dropdown_item)
        binding.courseSpinner.apply {
            adapter = courseAdapter
            onItemSelectedListener = courseListener
        }
        binding.searchBtn.setOnClickListener {
            viewModel.getAssignments(selectedCourse,binding.root).observe(viewLifecycleOwner){
                if (it.isEmpty()) {
                    binding.emptyBoxIv.visibility = VISIBLE
                    binding.emptyBoxTv.visibility = VISIBLE
                    binding.assignmentRecycler.visibility = GONE
                    Utils.snackBar(binding.root, "Very empty")
                } else {
                    binding.emptyBoxIv.visibility = GONE
                    binding.emptyBoxTv.visibility = GONE
                    binding.assignmentRecycler.visibility = VISIBLE
                    Utils.snackBar(binding.root,"Not empty")
                    val mAdapter = AssignmentsAdapter(it)
                    binding.assignmentRecycler.apply {
                        adapter = mAdapter
                        layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    }
                    mAdapter.notifyDataSetChanged()
                }

            }
        }
        viewModel.getAssignments("Computer Technology", binding.root).observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.emptyBoxIv.visibility = VISIBLE
                binding.emptyBoxTv.visibility = VISIBLE
                binding.assignmentRecycler.visibility = GONE
                Utils.snackBar(binding.root, "Very empty")
            } else {
                binding.emptyBoxIv.visibility = GONE
                binding.emptyBoxTv.visibility = GONE
                binding.assignmentRecycler.visibility = VISIBLE
                Utils.snackBar(binding.root,"Not empty")
                val mAdapter = AssignmentsAdapter(it)
                binding.assignmentRecycler.apply {
                    adapter = mAdapter
                    layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                }
                mAdapter.notifyDataSetChanged()
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