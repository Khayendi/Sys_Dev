package com.code.tusome.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.code.tusome.R
import com.code.tusome.adapters.AssignmentsAdapter
import com.code.tusome.databinding.FragmentAssignmentsBinding
import com.code.tusome.models.User
import com.code.tusome.ui.viewmodels.AssignmentViewModel
import com.code.tusome.ui.viewmodels.MainViewModel
import com.code.tusome.utils.Utils
import com.google.firebase.auth.FirebaseAuth

class AssignmentsFragment : Fragment() {
    private lateinit var binding: FragmentAssignmentsBinding
    private val viewModel by viewModels<AssignmentViewModel>()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var selectedCourse: String
    private lateinit var selectedUnit: String
    private lateinit var user:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: Fragment created")
        mainViewModel.getUser(FirebaseAuth.getInstance().uid!!).observe(viewLifecycleOwner){
            user = it!!
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getUser(FirebaseAuth.getInstance().uid.toString()).observe(viewLifecycleOwner){
            if (it!=null && it.role?.roleName=="Student"){
                binding.addAssignmentFab.visibility = GONE
            }else{
                binding.addAssignmentFab.visibility = VISIBLE
            }
        }
        val courseAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.courses,android.R.layout.simple_spinner_dropdown_item)
        binding.courseSpinner.setAdapter(courseAdapter)
        if (user.role?.roleName!! =="staff"){
            binding.addAssignmentFab.visibility = VISIBLE
        }else{
            binding.addAssignmentFab.visibility = GONE
        }
        binding.courseSpinner.setOnItemClickListener { parent, view, position, id ->
            selectedCourse = parent?.getItemAtPosition(position).toString()
        }
        binding.addAssignmentFab.setOnClickListener {
            val dialog = AddAssignmentFragment()
            dialog.show(requireActivity().supportFragmentManager,"add_assignment_frag")
        }
        binding.searchBtn.setOnClickListener {
            viewModel.getAssignments(selectedCourse,binding.root).observe(viewLifecycleOwner){ list ->
                if (list.isEmpty()) {
                    binding.emptyBoxIv.visibility = VISIBLE
                    binding.emptyBoxTv.visibility = VISIBLE
                    binding.assignmentRecycler.visibility = GONE
                    Utils.snackBar(binding.root, "Very empty")
                } else {
                    binding.emptyBoxIv.visibility = GONE
                    binding.emptyBoxTv.visibility = GONE
                    binding.assignmentRecycler.visibility = VISIBLE
                    Utils.snackBar(binding.root,"Not empty")
                    val mAdapter = AssignmentsAdapter(list)
                    binding.assignmentRecycler.apply {
                        adapter = mAdapter
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                    }
                    mAdapter.notifyDataSetChanged()
                    mAdapter.setOnItemLongClick(object :AssignmentsAdapter.OnItemLongCLick{
                        override fun onItemLongClick(position: Int) {
                            val popupMenu = PopupMenu(
                                requireContext(),
                                binding.assignmentRecycler.getChildAt(position)
                            )
                            popupMenu.menu.add("Edit")
                            popupMenu.menu.add("Delete")
                            popupMenu.show()
                            popupMenu.setOnMenuItemClickListener {
                                if (it.title == "Edit") {
                                    viewModel.updateAssignment(list[position], "")
                                } else if (it.title == "Delete") {
                                    viewModel.deleteAssignment(list[position], "")
                                }
                                true
                            }
                        }
                    })

            }
        }
        viewModel.getAssignments("Computer Technology", binding.root)
            .observe(viewLifecycleOwner) { list ->
                if (list.isEmpty()) {
                    binding.emptyBoxIv.visibility = VISIBLE
                    binding.emptyBoxTv.visibility = VISIBLE
                    binding.assignmentRecycler.visibility = GONE
                } else {
                    binding.emptyBoxIv.visibility = GONE
                    binding.emptyBoxTv.visibility = GONE
                    binding.assignmentRecycler.visibility = VISIBLE
                    val mAdapter = AssignmentsAdapter(list)
                    binding.assignmentRecycler.apply {
                        adapter = mAdapter
                        layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                    }
                    mAdapter.notifyDataSetChanged()
                    mAdapter.setOnItemLongClick(object : AssignmentsAdapter.OnItemLongCLick {
                        override fun onItemLongClick(position: Int) {
                            val popupMenu = PopupMenu(requireContext(), binding.assignmentRecycler.getChildAt(position))
                            popupMenu.menu.add("Edit")
                            popupMenu.menu.add("Delete")
                            popupMenu.show()
                            popupMenu.setOnMenuItemClickListener {
                                if (it.title == "Edit") {
                                    viewModel.updateAssignment(list[position], "")
                                } else if (it.title == "Delete") {
                                    viewModel.deleteAssignment(list[position], "")
                                }
                                true
                            }
                        }
                    })
                }
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