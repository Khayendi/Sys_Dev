package com.code.tusome.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.code.tusome.R
import com.code.tusome.adapters.ExamsAdapter
import com.code.tusome.databinding.FragmentExamBinding
import com.code.tusome.ui.viewmodels.ExamViewModel
import com.code.tusome.ui.viewmodels.MainViewModel
import com.code.tusome.utils.Utils
import com.google.firebase.auth.FirebaseAuth

class ExamFragment : Fragment() {
    private lateinit var binding:FragmentExamBinding
    private val examViewModel:ExamViewModel by viewModels()
    private val mainViewModel:MainViewModel by viewModels()
    private var selectedCourse:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: Fragment started successfully")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getUser(FirebaseAuth.getInstance().uid.toString()).observe(viewLifecycleOwner){
            if (it!=null && it.role?.roleName=="Student"){
                binding.addExamFab.visibility = GONE
            }else{
                binding.addExamFab.visibility = VISIBLE
            }
        }
        val courseAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.courses,android.R.layout.simple_spinner_dropdown_item)
        binding.courseSpinner.setAdapter(courseAdapter)
        binding.courseSpinner.setOnItemClickListener { parent, view, position, id ->
            selectedCourse = parent?.getItemAtPosition(position).toString()
        }
        binding.searchBtn.setOnClickListener {
            Log.i(TAG, "onViewCreated: $selectedCourse")
            examViewModel.getExams(selectedCourse!!).observe(viewLifecycleOwner){
                if (it!!.isEmpty()){
                    binding.emptyBoxIv.visibility = VISIBLE
                    binding.emptyBoxTv.visibility = VISIBLE
                    binding.examsRecycler.visibility = GONE
                }else{
                    binding.emptyBoxIv.visibility = GONE
                    binding.emptyBoxTv.visibility = GONE
                    binding.examsRecycler.visibility = VISIBLE
                    val mAdapter = ExamsAdapter(it)
                    binding.examsRecycler.apply {
                        adapter = mAdapter
                        layoutManager = LinearLayoutManager(requireContext(),
                            LinearLayoutManager.VERTICAL,false)
                    }
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
        examViewModel.getExams("Computer Technology").observe(viewLifecycleOwner){
            if (it!!.isEmpty()){
                binding.emptyBoxIv.visibility = VISIBLE
                binding.emptyBoxTv.visibility = VISIBLE
                binding.examsRecycler.visibility = GONE
            }else{
                binding.emptyBoxIv.visibility = GONE
                binding.emptyBoxTv.visibility = GONE
                binding.examsRecycler.visibility = VISIBLE
                val mAdapter = ExamsAdapter(it)
                binding.examsRecycler.apply {
                    adapter = mAdapter
                    layoutManager = LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL,false)
                }
                mAdapter.notifyDataSetChanged()
            }
        }
        binding.addExamFab.setOnClickListener {
            val dialog = AddExamFragment()
            dialog.show(requireActivity().supportFragmentManager, "add_exam_fragment")
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