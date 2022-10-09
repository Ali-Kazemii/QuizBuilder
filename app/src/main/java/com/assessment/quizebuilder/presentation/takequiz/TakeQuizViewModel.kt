package com.assessment.quizebuilder.presentation.takequiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.assessment.quizebuilder.core.BaseViewModel
import com.assessment.quizebuilder.core.Response
import com.assessment.quizebuilder.data.mapToAnswerEntity
import com.assessment.quizebuilder.data.mapToQuestionType
import com.assessment.quizebuilder.domain.usecase.GetQuizWithQuizIdUseCase
import com.assessment.quizebuilder.domain.model.QuestionEntity
import com.assessment.quizebuilder.domain.model.QuestionType
import com.assessment.quizebuilder.domain.model.UserMultiAnswerEntity
import com.assessment.quizebuilder.presentation.utils.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TakeQuizViewModel @Inject constructor(
    private val getQuizWithQuizIdUseCase: GetQuizWithQuizIdUseCase
) : BaseViewModel() {

    private var _listQuestion = MutableStateFlow<List<QuestionEntity>?>(null)
    val listQuestion = _listQuestion.asStateFlow()

    private val listCorrectAnswer: ArrayList<Long?> = arrayListOf()
    private val listMultiAnswer: ArrayList<UserMultiAnswerEntity> = arrayListOf()
    val quizResult = MutableLiveData<String>()

    fun getQuiz(quizId: String) {
        _listQuestion.value = null
        showLoading()
        viewModelScope.launch {
            getQuizWithQuizIdUseCase.run(quizId).collect {
                when (it) {
                    is Response.Success -> {
                        mapDataToQuestionList(it.data)
                    }
                    is Response.Failure -> {
                        failure.postValue("Fail to get questions: ${it.e.message}")
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun mapDataToQuestionList(data: DatabaseReference) {
        data.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<QuestionEntity>()

                snapshot.child(Constants.QUESTION_PREFIX).children.forEach { item ->
                    val entity = QuestionEntity(
                        questionId = item.child("questionId").value as Long,
                        title = item.child("title").value as String,
                        list = mapToAnswerEntity(item.child("list").children),
                        type = mapToQuestionType(item.child("type").value as String)
                    )
                    list.add(entity)
                }
                _listQuestion.value = list
                hideLoading()
            }

            override fun onCancelled(error: DatabaseError) {
                failure.postValue(error.message)
                hideLoading()
            }
        })
    }


    fun addCorrectAnswer(questionId: Long?) {
        listCorrectAnswer.add(questionId)
    }

    fun showQuizResult() {
        val listQuestions = _listQuestion.value
        val filterMultiple = listQuestions?.filter { it.type == QuestionType.MULTIPLE }

        filterMultiple?.forEach { item ->
            listMultiAnswer.forEach { userAnswer ->

                if (listCorrectAnswer.contains(userAnswer.questionId))
                    return@forEach

                if (item.questionId == userAnswer.questionId) {
                    val count = item.list?.count { it.isCorrect ?: false }
                    val answerCount = listMultiAnswer.count { it.questionId == item.questionId }

                    if (count == answerCount)
                        listCorrectAnswer.add(userAnswer.questionId)
                }
            }
        }
        quizResult.postValue("Quiz Score: ${listCorrectAnswer.size}/${listQuestions?.size}")
    }

    fun onMultipleCorrectAnswer(questionId: Long?, answerId: Long?) {
        listMultiAnswer.add(
            UserMultiAnswerEntity(questionId = questionId, answerId = answerId)
        )
    }

    fun removeSingleAnswer(questionId: Long?) {
        listCorrectAnswer.remove(questionId)
    }

    fun removeMultipleAnswer(questionId: Long?, answerId: Long?) {
        listMultiAnswer.remove(UserMultiAnswerEntity(questionId, answerId))
    }

    companion object {
        const val SINGLE = "SINGLE"
    }
}