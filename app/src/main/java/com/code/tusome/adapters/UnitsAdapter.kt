package com.code.tusome.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.code.tusome.R
import com.code.tusome.databinding.UnitItemBinding
import com.code.tusome.models.CourseUnit

class UnitsAdapter (list: List<CourseUnit>) :
    RecyclerView.Adapter<UnitsAdapter.UnitViewHolder>() {
    private var mList = ArrayList<CourseUnit>()
    private lateinit var listener:OnItemLongClick
    interface OnItemLongClick{
        fun onItemLongClick(position: Int)
    }
    fun setOnItemLongCLickListener(listener: OnItemLongClick){
        this.listener = listener
    }
    init {
        mList.clear()
        mList = list as ArrayList<CourseUnit>
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder =
        UnitViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.unit_item, parent, false)
        )

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) =
        holder.bind(mList[position])

    override fun getItemCount(): Int = mList.size
    inner class UnitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = UnitItemBinding.bind(view)
        init {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemLongClick(adapterPosition)
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