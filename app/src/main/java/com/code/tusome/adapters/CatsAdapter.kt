package com.code.tusome.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.code.tusome.R
import com.code.tusome.databinding.UnitItemBinding
import com.code.tusome.models.Cat

class CatsAdapter(private val list: List<Cat>) :
    RecyclerView.Adapter<CatsAdapter.CatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder =
        CatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.unit_item, parent, false)
        )

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = list.size
    inner class CatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = UnitItemBinding.bind(view)
        fun bind(exam: Cat) {
            binding.coursesTitleTv.text = exam.unitName
            binding.coursesDescriptionTv.text = exam.description
            binding.schoolTv.text = exam.invigilator
            binding.departmentTv.text = exam.duration
        }
    }
}