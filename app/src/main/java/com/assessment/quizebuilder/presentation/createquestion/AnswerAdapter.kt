package com.assessment.quizebuilder.presentation.createquestion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.assessment.quizebuilder.databinding.ItemCreateAnswerBinding
import com.assessment.quizebuilder.domain.model.AnswerEntity

class AnswerAdapter :
    ListAdapter<AnswerEntity, AnswerAdapter.AnswerViewHolder>(AnswerDiffUtil()) {

    var onItemClick: ((AnswerEntity) -> Unit) = {}

    override fun submitList(list: List<AnswerEntity>?) {
        super.submitList(list?.toMutableList())

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        return AnswerViewHolder(
            ItemCreateAnswerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AnswerViewHolder(
        private val binding: ItemCreateAnswerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AnswerEntity) {
            binding.textviewTitle.text = item.title
            binding.root.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }

    class AnswerDiffUtil : DiffUtil.ItemCallback<AnswerEntity>() {

        override fun areItemsTheSame(oldItem: AnswerEntity, newItem: AnswerEntity): Boolean {
            return (oldItem.title == newItem.title && oldItem.isCorrect == newItem.isCorrect)
        }

        override fun areContentsTheSame(oldItem: AnswerEntity, newItem: AnswerEntity): Boolean {
            return oldItem == newItem
        }
    }
}