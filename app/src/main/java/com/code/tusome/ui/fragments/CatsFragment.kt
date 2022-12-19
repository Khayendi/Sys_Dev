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
import com.code.tusome.adapters.CatsAdapter
import com.code.tusome.databinding.FragmentCatsBinding
import com.code.tusome.models.User
import com.code.tusome.ui.viewmodels.CatsViewModel
import com.code.tusome.ui.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

class CatsFragment : Fragment() {
    private lateinit var binding: FragmentCatsBinding
    private val catsViewModel: CatsViewModel by viewModels()
    private val mainViewModel:MainViewModel by viewModels()
    private lateinit var course: String
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: fragment started successfully")
        mainViewModel.getUser(FirebaseAuth.getInstance().uid!!).observe(viewLifecycleOwner){
            user = it!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (user.role?.roleName=="Staff"){
            binding.addCatFab.visibility = VISIBLE
        }else{
            binding.addCatFab.visibility= GONE
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
                .observe(viewLifecycleOwner) { catList ->
                    if (catList!!.isEmpty()) {
                        binding.emptyBoxIv.visibility = VISIBLE
                        binding.emptyBoxTv.visibility = VISIBLE
                        binding.catRecycler.visibility = GONE
                    } else {
                        binding.emptyBoxIv.visibility = VISIBLE
                        binding.emptyBoxTv.visibility = VISIBLE
                        binding.catRecycler.visibility = GONE
                        val mAdapter = CatsAdapter(catList)
                        binding.catRecycler.apply {
                            adapter = mAdapter
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL, false
                            )
                        }
                        mAdapter.notifyDataSetChanged()
                        mAdapter.setOnItemLongCLickListener(object : CatsAdapter.OnItemLongClick {
                            override fun onItemLongClick(position: Int) {
                                val popupMenu = PopupMenu(
                                    requireContext(),
                                    binding.catRecycler.getChildAt(position)
                                )
                                popupMenu.menu.add("Edit")
                                popupMenu.menu.add("Delete")
                                popupMenu.show()
                                popupMenu.setOnMenuItemClickListener {
                                    if (it.title == "Edit") {
                                        catsViewModel.updateCat("",catList[position])
                                    }else if(it.title=="Delete"){
                                        catsViewModel.deleteCat("",catList[position])
                                    }
                                    true
                                }
                            }
                        })
                    }
                }
        }
        catsViewModel.getAllCats("Computer Technology")
            .observe(viewLifecycleOwner) { catList ->
                if (catList!!.isEmpty()) {
                    binding.emptyBoxIv.visibility = VISIBLE
                    binding.emptyBoxTv.visibility = VISIBLE
                    binding.catRecycler.visibility = GONE
                } else {
                    binding.emptyBoxIv.visibility = VISIBLE
                    binding.emptyBoxTv.visibility = VISIBLE
                    binding.catRecycler.visibility = GONE
                    val mAdapter = CatsAdapter(catList)
                    binding.catRecycler.apply {
                        adapter = mAdapter
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL, false
                        )
                    }
                    mAdapter.notifyDataSetChanged()
                    mAdapter.setOnItemLongCLickListener(object : CatsAdapter.OnItemLongClick {
                        override fun onItemLongClick(position: Int) {
                            val popupMenu = PopupMenu(
                                requireContext(),
                                binding.catRecycler.getChildAt(position)
                            )
                            popupMenu.menu.add("Edit")
                            popupMenu.menu.add("Delete")
                            popupMenu.show()
                            popupMenu.setOnMenuItemClickListener {
                                if (it.title == "Edit") {
                                    catsViewModel.updateCat("",catList[position])
                                }else if(it.title=="Delete"){
                                    catsViewModel.deleteCat("",catList[position])
                                }
                                true
                            }
                        }
                    })
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