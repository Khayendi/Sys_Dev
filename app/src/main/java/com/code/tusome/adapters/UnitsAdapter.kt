package com.code.tusome.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.code.tusome.R
import com.code.tusome.databinding.UnitItemBinding
import com.code.tusome.models.CourseUnit
import com.code.tusome.models.Exam

class UnitsAdapter (private val list: List<CourseUnit>) :
    RecyclerView.Adapter<UnitsAdapter.UnitViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder =
        UnitViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.unit_item, parent, false)
        )

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = list.size
    inner class UnitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = UnitItemBinding.bind(view)
        fun bind(exam: CourseUnit) {
            binding.coursesTitleTv.text = exam.exam[0].unitName
            binding.coursesDescriptionTv.text = exam.uid
            binding.schoolTv.text = exam.instructor
            binding.departmentTv.text = exam.exam[0].courseCode
        }
    }
}