package com.code.tusome.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.code.tusome.R
import com.code.tusome.databinding.UnitItemBinding
import com.code.tusome.models.Course
import com.code.tusome.models.CourseUnit

class CourseAdapter (private val list: List<Course>) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {
    private lateinit var listener:OnLongClickListener
    interface OnLongClickListener{
        fun onLongClick(position: Int)
    }
    fun setOnLongClickListener(listener: OnLongClickListener){
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder =
        CourseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.unit_item, parent, false),
            listener
        )

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = list.size
    inner class CourseViewHolder(view: View,listener: OnLongClickListener) : RecyclerView.ViewHolder(view) {
        private val binding = UnitItemBinding.bind(view)
        init {
            binding.root.setOnLongClickListener {
                if (adapterPosition!=RecyclerView.NO_POSITION){
                    listener.onLongClick(adapterPosition)
                }
                true
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