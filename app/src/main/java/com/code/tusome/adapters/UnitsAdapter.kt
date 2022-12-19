package com.code.tusome.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.code.tusome.R
import com.code.tusome.databinding.UnitItemBinding
import com.code.tusome.models.CourseUnit

class UnitsAdapter(private val list: List<CourseUnit>) :
    RecyclerView.Adapter<UnitsAdapter.UnitViewHolder>() {
    private lateinit var listener: OnItemLongClick

    interface OnItemLongClick {
        fun onItemLongClick(position: Int)
    }
    fun setOnItemLongCLickListener(listener: OnItemLongClick) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder =
        UnitViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.unit_item, parent, false),
            listener
        )

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = list.size
    inner class UnitViewHolder(view: View,listener: OnItemLongClick) : RecyclerView.ViewHolder(view) {
        private val binding = UnitItemBinding.bind(view)
        init {
            binding.root.setOnLongClickListener {
            if (adapterPosition!=RecyclerView.NO_POSITION){
                listener.onItemLongClick(adapterPosition)
            }
                true
            }
        }

        fun bind(exam: CourseUnit) {
            binding.coursesTitleTv.text = exam.unitName
            binding.coursesDescriptionTv.text = exam.description
            binding.schoolTv.text = exam.instructor
            binding.departmentTv.text = exam.duration
        }
    }
}