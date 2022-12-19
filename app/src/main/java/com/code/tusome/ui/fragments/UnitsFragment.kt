package com.code.tusome.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.GONE
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.code.tusome.R
import com.code.tusome.adapters.UnitsAdapter
import com.code.tusome.databinding.FragmentUnitsBinding
import com.code.tusome.ui.viewmodels.UnitsViewModel

class UnitsFragment : Fragment() {
    private lateinit var binding: FragmentUnitsBinding
    private val unitsViewModel: UnitsViewModel by viewModels()
    private lateinit var selectedCourse: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: fragment started successfully")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val courseAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.course_codes,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.courseCriteriaEtl.setAdapter(courseAdapter)
        binding.courseCriteriaEtl.setOnItemClickListener { parent, view, position, id ->
            selectedCourse = parent?.getItemAtPosition(position).toString()
        }
        binding.addUnitFab.setOnClickListener {
            val dialog = AddUnitFragment()
            dialog.show(requireActivity().supportFragmentManager, "add_unit_fragment")
        }
        binding.searchFab.setOnClickListener {
            unitsViewModel.getUnits(selectedCourse).observe(viewLifecycleOwner) {
                if (it!!.isEmpty()) {
                    binding.emptyBoxIv.visibility = VISIBLE
                    binding.emptyBoxTv.visibility = VISIBLE
                    binding.unitsRecycler.visibility = GONE
                } else {
                    binding.emptyBoxIv.visibility = GONE
                    binding.emptyBoxTv.visibility = GONE
                    binding.unitsRecycler.visibility = VISIBLE
                    val mAdapter = UnitsAdapter(it)
                    binding.unitsRecycler.apply {
                        adapter = mAdapter
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL, false
                        )
                    }
                    mAdapter.notifyDataSetChanged()
                    mAdapter.setOnItemLongCLickListener(object : UnitsAdapter.OnItemLongClick {
                        override fun onItemLongClick(position: Int) {
                            val popupMenu = PopupMenu(requireContext(), binding.unitsRecycler)
                            popupMenu.menu.add("Edit")
                            popupMenu.menu.add("Delete")
//                            popupMenu.setOnMenuItemClickListener {
//
//                            }
                        }
                    })
                }
            }
        }
        unitsViewModel.getUnits("SCCI").observe(viewLifecycleOwner) {
            if (it!!.isEmpty()) {
                binding.emptyBoxIv.visibility = VISIBLE
                binding.emptyBoxTv.visibility = VISIBLE
                binding.unitsRecycler.visibility = GONE
            } else {
                binding.emptyBoxIv.visibility = GONE
                binding.emptyBoxTv.visibility = GONE
                binding.unitsRecycler.visibility = VISIBLE
                val mAdapter = UnitsAdapter(it)
                binding.unitsRecycler.apply {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnitsBinding.inflate(inflater, container, false)
        setUpMenu()
        return binding.root
    }

    private fun setUpMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.units_menu, menu)
                val menuItem = menu.findItem(R.id.search)
                val searchView = menuItem.actionView as SearchView
                searchView.queryHint = "Search units"
                searchView.setOnQueryTextListener(object : OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        //TODO: Implement searching here
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                //TODO: Implement selects hers
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    companion object {
        private val TAG = UnitsFragment::class.java.simpleName
    }
}