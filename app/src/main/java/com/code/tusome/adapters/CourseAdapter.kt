package com.code.tusome.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.code.tusome.R
import com.code.tusome.databinding.UnitItemBinding
import com.code.tusome.models.Course
import com.code.tusome.models.CourseUnit

class CourseAdapter(private val list: List<Course>) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {
    private lateinit var listener: OnItemLongClick

    interface OnItemLongClick {
        fun onItemLongClick(position: Int)
    }

    fun setOnItemLongCLickListener(listener: OnItemLongClick) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder =
        CourseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.unit_item, parent, false)
        )

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = list.size
    inner class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = UnitItemBinding.bind(view)

        init {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemLongClick(adapterPosition)
            }
        }

        fun bind(exam: Course) {
            binding.coursesTitleTv.text = exam.courseCode
            binding.coursesDescriptionTv.text = exam.department
            binding.schoolTv.text = exam.school
            binding.departmentTv.text = exam.department
        }
    }
}