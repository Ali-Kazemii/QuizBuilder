package com.assessment.quizebuilder.presentation.createquestion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.assessment.quizebuilder.domain.model.AnswerEntity
import com.assessment.quizebuilder.domain.model.QuestionEntity
import com.assessment.quizebuilder.domain.model.QuestionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class CreateQuestionViewModel @Inject constructor() : ViewModel() {
    val listAnswer = MutableLiveData(arrayListOf<AnswerEntity>())
    private var _question = MutableStateFlow("")
    private var _questionType = MutableStateFlow(QuestionType.SINGLE)

    val isEnableCreateQuestion: Flow<Boolean> by lazy {
        combine(
            listAnswer.asFlow(),
            _question
        ) { list, question ->
            return@combine list.isNotEmpty() and question.isNotEmpty()
        }
    }

    fun removeAnswer(entity: AnswerEntity) {
        val list = listAnswer.value
        list?.remove(entity)
        listAnswer.postValue(list)
    }

    fun addAnswer(title: String, isCorrect: Boolean) {
        listAnswer.value?.let { list ->
            val entity = AnswerEntity(answerId = list.size.toLong(), title = title, isCorrect = isCorrect)
            list.add(entity)

            listAnswer.postValue(list)
        }
    }

    fun setQuestionTitle(title: String) {
        _question.value = title
    }

    fun setQuestionType(type: QuestionType) {
        _questionType.value = type
    }


    fun getAnswerCount(): Int {
        return listAnswer.value?.size ?: 0
    }

    fun getQuestion(): QuestionEntity {
        return QuestionEntity(
            title = _question.value,
            list = listAnswer.value,
            type = _questionType.value
        )
    }

}