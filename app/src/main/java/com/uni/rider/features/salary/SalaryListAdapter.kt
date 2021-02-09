package com.uni.rider.features.salary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uni.data.models.Salary
import com.uni.rider.databinding.ItemSalaryBinding

class SalaryListAdapter : ListAdapter<Salary, SalaryListAdapter.SalaryViewHolder>(DIFF_UTILS) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalaryViewHolder {
        return SalaryViewHolder(ItemSalaryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SalaryListAdapter.SalaryViewHolder, pos: Int) {
        holder.bind(getItem(pos))
    }

    inner class SalaryViewHolder(private val item: ItemSalaryBinding) : RecyclerView.ViewHolder(item.root) {


        fun bind(data: Salary) {
            item.data = data
        }
    }


    companion object {

        val TAG: String = SalaryListAdapter::class.java.name

        val DIFF_UTILS: DiffUtil.ItemCallback<Salary> = object : DiffUtil.ItemCallback<Salary>() {
            override fun areItemsTheSame(old: Salary, new: Salary): Boolean {
                return old == new
            }

            override fun areContentsTheSame(old: Salary, new: Salary): Boolean {
                return old == new
            }
        }
    }
}