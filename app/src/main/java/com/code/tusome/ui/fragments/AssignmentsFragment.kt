package com.code.tusome.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.code.tusome.R
import com.code.tusome.adapters.AssignmentsAdapter
import com.code.tusome.databinding.FragmentAssignmentsBinding
import com.code.tusome.ui.viewmodels.AssignmentViewModel
import com.code.tusome.utils.Utils

class AssignmentsFragment : Fragment() {
    private lateinit var binding: FragmentAssignmentsBinding
    private val viewModel by viewModels<AssignmentViewModel>()

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