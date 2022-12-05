package com.code.tusome.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.code.tusome.R
import com.code.tusome.databinding.FragmentCatsBinding

class CatsFragment : Fragment() {
    private lateinit var binding:FragmentCatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    /**
     * This is an instance of a lifecycle of the fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatsBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
      private val TAG = CatsFragment::class.java.simpleName
    }
}