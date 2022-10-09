package com.assessment.quizebuilder.presentation.quizlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.assessment.quizebuilder.databinding.ItemOtherQuizBinding
import com.assessment.quizebuilder.domain.model.QuizEntity

class OtherQuizListAdapter :
    ListAdapter<QuizEntity, OtherQuizListAdapter.QuizListViewHolder>(OtherQuizDiffUtil()) {

    var onItemClick: ((String?) -> Unit) = {}

    override fun submitList(list: List<QuizEntity>?) {
        super.submitList(list?.toList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizListViewHolder {
        return QuizListViewHolder(
            ItemOtherQuizBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QuizListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class QuizListViewHolder(
        private val binding: ItemOtherQuizBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuizEntity) {
            binding.textviewTitle.text = item.title
            binding.root.setOnClickListener {
                onItemClick.invoke(item.quizId)
            }
        }
    }

    class OtherQuizDiffUtil : DiffUtil.ItemCallback<QuizEntity>() {

        override fun areItemsTheSame(oldItem: QuizEntity, newItem: QuizEntity): Boolean {
            return (oldItem.title == newItem.title && oldItem.listQuestions?.size == newItem.listQuestions?.size && oldItem.listQuestions == newItem.listQuestions)
        }

        override fun areContentsTheSame(oldItem: QuizEntity, newItem: QuizEntity): Boolean {
            return oldItem == newItem
        }
    }
}