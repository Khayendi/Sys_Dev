package com.code.tusome.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import com.code.tusome.R
import com.code.tusome.databinding.FragmentUnitsBinding

class UnitsFragment : Fragment() {
    private lateinit var binding: FragmentUnitsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnitsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.units_menu, menu)
        val menuItem = menu.findItem(R.id.search)
        val searchView = MenuItemCompat.getActionView(menuItem) as SearchView
        searchView.queryHint = "Search units"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //TODO : Implement search
                return false
            }
        })
    }

    companion object {
        private val TAG = UnitsFragment::class.java.simpleName
    }
}