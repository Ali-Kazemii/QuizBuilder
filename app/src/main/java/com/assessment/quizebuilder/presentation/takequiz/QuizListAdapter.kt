package com.assessment.quizebuilder.presentation.takequiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.assessment.quizebuilder.R
import com.assessment.quizebuilder.databinding.ItemCheckboxBinding
import com.assessment.quizebuilder.databinding.ItemQuestionWithAnswersBinding
import com.assessment.quizebuilder.domain.model.QuestionEntity
import com.assessment.quizebuilder.domain.model.QuestionType

class QuizListAdapter : ListAdapter<QuestionEntity, RecyclerView.ViewHolder>(QuizDiffUtil()) {

    private var listItem: ArrayList<QuestionEntity> = arrayListOf()

    var onSingleCorrectAnswerClick: ((questionId: Long?) -> Unit)? = null
    var onMultipleCorrectAnswerClick: ((questionId: Long?, answerId: Long?) -> Unit)? = null

    var onDeleteSingleCorrectAnswerClick: ((questionId: Long?) -> Unit)? = null
    var onDeleteMultipleCorrectAnswerClick: ((questionId: Long?, answerId: Long?) -> Unit)? = null


    companion object {
        const val SINGLE = 1
        const val MULTIPLE = 2
    }

    override fun getItemViewType(position: Int): Int {
        val item = listItem[position]
        return when (item.type) {
            QuestionType.SINGLE -> {
                SINGLE
            }
            QuestionType.MULTIPLE -> {
                MULTIPLE
            }
            else -> {
                throw IllegalArgumentException("Unsupported layout")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SINGLE -> {
                SingleAnswerViewHolder(
                    ItemQuestionWithAnswersBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            MULTIPLE -> {
                MultiAnswerViewHolder(
                    ItemQuestionWithAnswersBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                throw IllegalArgumentException("Unsupported layout")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SingleAnswerViewHolder -> {
                holder.bindData(listItem[position])
            }
            is MultiAnswerViewHolder -> {
                holder.bindData(listItem[position])
            }
        }
    }

    override fun getItemCount(): Int = listItem.size

    inner class SingleAnswerViewHolder(private val itemBinding: ItemQuestionWithAnswersBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindData(item: QuestionEntity) {
            itemBinding.textviewTitle.text = item.title

            val radioGroup = RadioGroup(itemBinding.root.context)
            radioGroup.orientation = RadioGroup.VERTICAL
            val layoutParams = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            )

            item.list?.forEach { answer ->
                val radioButton = RadioButton(itemBinding.root.context)
                radioButton.id = View.generateViewId()
                radioButton.text = answer.title
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                )
                params.setMargins(0, 20, 0, 0)
                radioButton.layoutParams = params
                radioButton.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked && answer.isCorrect == true) {
                        onSingleCorrectAnswerClick?.invoke(item.questionId)
                    } else if (!isChecked)
                        onDeleteSingleCorrectAnswerClick?.invoke(item.questionId)
                }

                radioGroup.addView(radioButton)
            }
            itemBinding.layoutAnswer.addView(radioGroup, layoutParams)
        }
    }


    inner class MultiAnswerViewHolder(private val itemBinding: ItemQuestionWithAnswersBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindData(item: QuestionEntity) {
            val context = itemBinding.root.context

            itemBinding.textviewTitle.text = item.title
            val correctAnswerHint = context.getString(R.string.correct_answer_count)

            itemBinding.textviewCorrectAnswerCount.text = correctAnswerHint
                .replace("$", item.list?.count { it.isCorrect ?: false }.toString())

            item.list?.forEach { answer ->
                val view = ItemCheckboxBinding.inflate(
                    LayoutInflater.from(context),
                    itemBinding.root,
                    false
                )
                view.checkbox.text = answer.title
                view.checkbox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked && answer.isCorrect == true)
                        onMultipleCorrectAnswerClick?.invoke(item.questionId, answer.answerId)
                    else if (!isChecked)
                        onDeleteMultipleCorrectAnswerClick?.invoke(item.questionId, answer.answerId)
                }
                itemBinding.layoutAnswer.addView(view.root)
            }
        }
    }

    class QuizDiffUtil : DiffUtil.ItemCallback<QuestionEntity>() {

        override fun areItemsTheSame(oldItem: QuestionEntity, newItem: QuestionEntity): Boolean {
            return (oldItem.questionId == newItem.questionId && oldItem.type == newItem.type && oldItem.list == newItem.list)
        }

        override fun areContentsTheSame(oldItem: QuestionEntity, newItem: QuestionEntity): Boolean {
            return oldItem == newItem
        }
    }
}