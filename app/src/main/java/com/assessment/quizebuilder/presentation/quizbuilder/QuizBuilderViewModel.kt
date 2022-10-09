package com.assessment.quizebuilder.presentation.quizbuilder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.assessment.quizebuilder.core.BaseViewModel
import com.assessment.quizebuilder.core.Response
import com.assessment.quizebuilder.domain.model.QuestionEntity
import com.assessment.quizebuilder.domain.model.QuizEntity
import com.assessment.quizebuilder.domain.usecase.PublishQuizUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizBuilderViewModel @Inject constructor(
    private val publishQuizUseCase: PublishQuizUseCase,
) : BaseViewModel() {

    val listQuestion = MutableLiveData(arrayListOf<QuestionEntity>())

    val isEnablePublish: Flow<Boolean> by lazy {
        combine(
            listQuestion.asFlow(),
        ) { list ->
            return@combine list.isNotEmpty()
        }
    }

    fun getQuestionCount(): Int {
        return listQuestion.value?.size ?: 0
    }

    fun addQuestion(question: QuestionEntity) {
        val list = listQuestion.value
        list?.add(question)
        listQuestion.value = list
    }

    fun removeQuestion(entity: QuestionEntity) {
        val list = listQuestion.value
        list?.remove(entity)
        listQuestion.value = list
    }

    fun publishQuiz(
        title: String
    ) {
        if (isValidQuiz()) {
            showLoading()
            setQuestionIds()

            viewModelScope.launch {
                val model = QuizEntity(
                    title = title,
                    listQuestions = listQuestion.value
                )
                publishQuizUseCase.run(model).collect {
                    when (it) {
                        is Response.Success -> {
                            it.data.addOnCompleteListener {
                                success.postValue(true)
                                hideLoading()
                            }
                        }
                        is Response.Failure -> {
                            failure.postValue(it.e.message)
                            hideLoading()
                        }
                    }
                }
            }
        } else {
            failure.postValue("There is no/invalid question")
            hideLoading()
        }
    }


    private fun isValidQuiz(): Boolean {
        return !listQuestion.value.isNullOrEmpty()
    }

    private fun setQuestionIds() {
        listQuestion.value?.let { list ->
            list.forEachIndexed { index, questionEntity ->
                questionEntity.questionId = index.toLong()
            }
        }
    }

}