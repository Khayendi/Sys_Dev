package com.code.tusome.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.code.tusome.R
import com.code.tusome.databinding.FragmentCatsBinding
import com.code.tusome.models.Cat
import com.code.tusome.models.Course
import com.code.tusome.ui.viewmodels.CatsViewModel
import java.util.UUID

class CatsFragment : Fragment() {
    private lateinit var binding:FragmentCatsBinding
    private val catsViewModel:CatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: fragment started successfully")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        catsViewModel.getAllCats("")
            .observe(viewLifecycleOwner){
                if (it!!.isEmpty()){
                    binding.emptyBoxIv.visibility = VISIBLE
                    binding.emptyBoxTv.visibility = VISIBLE
                    binding.catRecycler.visibility = GONE
                }else{
                    binding.emptyBoxIv.visibility = VISIBLE
                    binding.emptyBoxTv.visibility = VISIBLE
                    binding.catRecycler.visibility = GONE
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
        binding = FragmentCatsBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
      private val TAG = CatsFragment::class.java.simpleName
    }
}