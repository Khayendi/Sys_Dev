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
import com.code.tusome.ui.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

class CatsFragment : Fragment() {
    private lateinit var binding: FragmentCatsBinding
    private val catsViewModel: CatsViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var course: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: fragment started successfully")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getUser(FirebaseAuth.getInstance().uid.toString()).observe(viewLifecycleOwner){
            if (it!=null && it.role?.roleName=="Student"){
                binding.addCatFab.visibility = GONE
            }else{
                binding.addCatFab.visibility = VISIBLE
            }
        }
        binding.addCatFab.setOnClickListener {
            val dialog = AddCatFragment()
            dialog.show(requireActivity().supportFragmentManager, "cat_fragment")
        }
        val arrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.courses,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.courseSpinner.setAdapter(arrayAdapter)
        binding.courseSpinner.setOnItemClickListener { parent, view, position, id ->
            course = parent?.getItemAtPosition(position).toString()
        }
        binding.searchBtn.setOnClickListener {
            catsViewModel.getAllCats(course)
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