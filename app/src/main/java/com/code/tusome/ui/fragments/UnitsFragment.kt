package com.code.tusome.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.VISIBLE
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.GONE
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.code.tusome.R
import com.code.tusome.adapters.CourseAdapter
import com.code.tusome.adapters.UnitsAdapter
import com.code.tusome.databinding.FragmentUnitsBinding
import com.code.tusome.models.Course
import com.code.tusome.ui.viewmodels.UnitsViewModel

class UnitsFragment : Fragment() {
    private lateinit var binding: FragmentUnitsBinding
    private val unitsViewModel: UnitsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: fragment started successfully")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unitsViewModel.getUnits(Course("", "", ArrayList(), "", "")).
        observe(viewLifecycleOwner) {
            if (it!!.isEmpty()){
                binding.emptyBoxIv.visibility = VISIBLE
                binding.emptyBoxTv.visibility = VISIBLE
                binding.unitsRecycler.visibility = GONE
            }else{
                binding.emptyBoxIv.visibility = GONE
                binding.emptyBoxTv.visibility = GONE
                binding.unitsRecycler.visibility = VISIBLE
                val mAdapter = UnitsAdapter(it)
                binding.unitsRecycler.apply {
                    adapter = mAdapter
                    layoutManager = LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL,false)
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