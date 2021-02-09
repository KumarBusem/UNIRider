package com.uni.rider.features.feedback

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uni.data.models.Feedback
import com.uni.rider.databinding.ItemFeedbackBinding

class FeedbackListAdapter : ListAdapter<Feedback, FeedbackListAdapter.FeedbackViewHolder>(DIFF_UTILS) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        return FeedbackViewHolder(ItemFeedbackBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FeedbackListAdapter.FeedbackViewHolder, pos: Int) {
        holder.bind(getItem(pos))
    }

    inner class FeedbackViewHolder(private val item: ItemFeedbackBinding) : RecyclerView.ViewHolder(item.root) {


        fun bind(data: Feedback) {
            item.data = data
        }
    }


    companion object {

        val TAG: String = FeedbackListAdapter::class.java.name

        val DIFF_UTILS: DiffUtil.ItemCallback<Feedback> = object : DiffUtil.ItemCallback<Feedback>() {
            override fun areItemsTheSame(old: Feedback, new: Feedback): Boolean {
                return old.order_id == new.order_id
            }

            override fun areContentsTheSame(old: Feedback, new: Feedback): Boolean {
                return old == new
            }
        }
    }
}