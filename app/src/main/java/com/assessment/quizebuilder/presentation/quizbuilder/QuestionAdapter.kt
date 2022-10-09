package com.assessment.quizebuilder.presentation.quizbuilder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.assessment.quizebuilder.databinding.ItemCreateQuestionBinding
import com.assessment.quizebuilder.domain.model.QuestionEntity

class QuestionAdapter :
    ListAdapter<QuestionEntity, QuestionAdapter.QuestionViewHolder>(AnswerDiffUtil()) {

    var onItemClick: ((QuestionEntity) -> Unit) = {}

    override fun submitList(list: List<QuestionEntity>?) {
        super.submitList(list?.toList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder(
            ItemCreateQuestionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class QuestionViewHolder(
        private val binding: ItemCreateQuestionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuestionEntity) {
            binding.textviewTitle.text = item.title
            binding.root.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }

    class AnswerDiffUtil : DiffUtil.ItemCallback<QuestionEntity>() {

        override fun areItemsTheSame(oldItem: QuestionEntity, newItem: QuestionEntity): Boolean {
            return (oldItem.title == newItem.title && oldItem.list?.size == newItem.list?.size && oldItem.list == newItem.list)
        }

        override fun areContentsTheSame(oldItem: QuestionEntity, newItem: QuestionEntity): Boolean {
            return oldItem == newItem
        }
    }
}