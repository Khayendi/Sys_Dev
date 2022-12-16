package com.code.tusome.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.code.tusome.R
import com.code.tusome.adapters.AuthAdapter
import com.code.tusome.databinding.FragmentResultsBinding
import com.code.tusome.models.FragObject
import com.code.tusome.ui.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth


class ResultsFragment : Fragment() {
    private lateinit var binding: FragmentResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = ArrayList<FragObject>()
        list.add(FragObject("ASSIGNMENTS", AssignmentsFragment()))
        list.add(FragObject("CATS", CatsFragment()))
        list.add(FragObject("EXAMS", ExamFragment()))
        val adapter = AuthAdapter(list, requireActivity().supportFragmentManager)
        binding.resultsPager.adapter = adapter
        binding.resultsTab.setupWithViewPager(binding.resultsPager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultsBinding.inflate(inflater, container, false)
        setupMenu()
        return binding.root
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.admin_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.add_assignment -> {
                        val dialog = AddAssignmentFragment()
                        dialog.show(requireActivity().supportFragmentManager, "assignment_frag")
                    }

                    R.id.add_cat -> {
                        val dialog = AddCatFragment()
                        dialog.show(requireActivity().supportFragmentManager, "cat_fragment")
                    }

                    R.id.add_exam -> {
                        val dialog = AddExamFragment()
                        dialog.show(requireActivity().supportFragmentManager, "add_exam_fragment")
                    }

                    R.id.logout -> {
                        FirebaseAuth.getInstance().signOut()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finish()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    fun createDialog() {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_add_assignment, binding.root, false)
        val alert = AlertDialog.Builder(requireContext())
            .setView(view)
            .setTitle("")
            .create()
        alert.show()
    }

    companion object {
        private val TAG = ResultsFragment::class.java.simpleName
    }
}