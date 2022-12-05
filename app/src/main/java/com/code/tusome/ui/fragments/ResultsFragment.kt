package com.code.tusome.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.code.tusome.R
import com.code.tusome.adapters.AuthAdapter
import com.code.tusome.databinding.FragmentResultsBinding
import com.code.tusome.models.FragObject


class ResultsFragment : Fragment() {
    private lateinit var binding:FragmentResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = ArrayList<FragObject>()
        list.add(FragObject("ASSIGNMENTS",AssignmentsFragment()))
        list.add(FragObject("CATS",CatsFragment()))
        list.add(FragObject("EXAMS",ExamFragment()))
        val adapter = AuthAdapter(list,requireActivity().supportFragmentManager)
        binding.resultsPager.adapter = adapter
        binding.resultsTab.setupWithViewPager(binding.resultsPager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultsBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
        private val TAG = ResultsFragment::class.java.simpleName
    }
}