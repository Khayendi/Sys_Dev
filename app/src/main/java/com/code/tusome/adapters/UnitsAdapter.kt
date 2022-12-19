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
<<<<<<< HEAD
    fun setOnItemLongClock(listener:OnItemLongClick){
=======
    fun setOnItemLongCLickListener(listener: OnItemLongClick){
>>>>>>> 867a51dfc6d9564d807ac6ddc9e94d5fa4efaec0
        this.listener = listener
    }
    init {
        mList.clear()
        mList = list as ArrayList<CourseUnit>
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder =
        UnitViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.unit_item, parent, false),
            listener
        )

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) =
        holder.bind(mList[position])

    override fun getItemCount(): Int = mList.size
    inner class UnitViewHolder(view: View,listener: OnItemLongClick) : RecyclerView.ViewHolder(view) {
        private val binding = UnitItemBinding.bind(view)
        init {
<<<<<<< HEAD
            binding.root.setOnLongClickListener {
            if (adapterPosition!=RecyclerView.NO_POSITION){
                listener.onItemLongClick(adapterPosition)
            }
                true
            }
=======
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemLongClick(adapterPosition)
            }
>>>>>>> 867a51dfc6d9564d807ac6ddc9e94d5fa4efaec0
        }
        fun bind(exam: CourseUnit) {
            binding.coursesTitleTv.text = exam.unitName
            binding.coursesDescriptionTv.text = exam.description
            binding.schoolTv.text = exam.instructor
            binding.departmentTv.text = exam.duration
        }
    }
}