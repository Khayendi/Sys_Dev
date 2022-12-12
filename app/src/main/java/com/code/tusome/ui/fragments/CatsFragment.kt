package com.code.tusome.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.code.tusome.R
import com.code.tusome.adapters.AssignmentsAdapter
import com.code.tusome.adapters.CatsAdapter
import com.code.tusome.databinding.FragmentCatsBinding
import com.code.tusome.models.Cat
import com.code.tusome.models.Course
import com.code.tusome.ui.viewmodels.CatsViewModel
import java.util.UUID

class CatsFragment : Fragment() {
    private lateinit var binding: FragmentCatsBinding
    private val catsViewModel: CatsViewModel by viewModels()
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
        Log.i(TAG, "onCreate: fragment started successfully")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.courses,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.courseSpinner.apply {
            adapter = arrayAdapter
            onItemSelectedListener = listener
        }
        catsViewModel.getAllCats("Computer Technology")
            .observe(viewLifecycleOwner) {
                if (it!!.isEmpty()) {
                    binding.emptyBoxIv.visibility = VISIBLE
                    binding.emptyBoxTv.visibility = VISIBLE
                    binding.catRecycler.visibility = GONE
                } else {
                    binding.emptyBoxIv.visibility = VISIBLE
                    binding.emptyBoxTv.visibility = VISIBLE
                    binding.catRecycler.visibility = GONE
                    val mAdapter = CatsAdapter(it)
                    binding.catRecycler.apply {
                        adapter = mAdapter
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL, false
                        )
                    }
                    mAdapter.notifyDataSetChanged()
                }
            }
    }

    /**
     * This is an instance of a lifecycle of the fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private val TAG = CatsFragment::class.java.simpleName
    }
}