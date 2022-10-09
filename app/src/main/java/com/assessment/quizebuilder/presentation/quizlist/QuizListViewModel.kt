package com.assessment.quizebuilder.presentation.quizlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.assessment.quizebuilder.core.BaseViewModel
import com.assessment.quizebuilder.core.Response
import com.assessment.quizebuilder.domain.usecase.*
import com.assessment.quizebuilder.domain.model.QuestionEntity
import com.assessment.quizebuilder.domain.model.QuizEntity
import com.assessment.quizebuilder.presentation.utils.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizListViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val getAllQuizListUseCase: GetAllQuizListUseCase,
    private val deleteQuizUseCase: DeleteQuizUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
) : BaseViewModel() {

    private var _listPublisherQuizzes = MutableStateFlow<ArrayList<QuizEntity>>(arrayListOf())
    val listPublisherQuizzes =_listPublisherQuizzes.asStateFlow()

    private var _listOtherQuizzes = MutableStateFlow<ArrayList<QuizEntity>>(arrayListOf())
    val listOtherQuizzes = _listOtherQuizzes.asStateFlow()

    private var _userId = MutableStateFlow<String?>(null)
    private var _listQuizzes = MutableStateFlow<ArrayList<QuizEntity>>(arrayListOf())

    init{
        viewModelScope.launch {
            showLoading()
            getAllQuizListUseCase.run(Unit).combine(
                getUserIdUseCase.run(Unit)
            ) { quizListResponse, userIdResponse ->

                var userId: String? = null

                when (userIdResponse) {
                    is Response.Success -> {
                        userId = userIdResponse.data
                    }
                    is Response.Failure -> {
                        failure.postValue(userIdResponse.e.message)
                        hideLoading()
                    }
                }

                userId?.let {
                    _userId.value = it
                    when (quizListResponse) {
                        is Response.Success -> {
                            mapDataToQuizList(quizListResponse.data)
                               /* getOtherQuizList(userId = userId, listQuiz = list)
                                getPublisherQuizList(userId = userId, listQuiz = list)*/


                        }
                        is Response.Failure -> {
                            failure.postValue(quizListResponse.e.message)
                            hideLoading()
                        }
                    }
                }?: kotlin.run {
                    failure.postValue("Fail to load data.. please check your internet and retry")
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getOtherQuizList() {
        viewModelScope.launch {
            combine(
                _userId,
                _listQuizzes
            ){ userId, list ->
                val otherQuizzes = list.filter { it.publisherId != userId}
                _listOtherQuizzes.value = ArrayList(otherQuizzes)
            }.launchIn(viewModelScope)
        }

    }


    fun getPublisherQuizList() {
        viewModelScope.launch {
            combine(
                _userId,
                _listQuizzes
            ){ userId, list ->
                val publisherQuizzes = list.filter { it.publisherId == userId}
                _listPublisherQuizzes.value = ArrayList(publisherQuizzes)
            }.launchIn(viewModelScope)
        }
    }


    private fun mapDataToQuizList(
        data: DatabaseReference
    ) {
        data.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<QuizEntity>()
                snapshot.children.forEach { data ->
                    val quiz = QuizEntity(
                        quizId = data.child("quizId").value as String,
                        publisherId = data.child("publisherId").value as String,
                        title = data.child("title").value as String,
                        listQuestions = (data.child(Constants.QUESTION_PREFIX).value as ArrayList<QuestionEntity>)
                    )
                    list.add(quiz)
                }
                hideLoading()
                _listQuizzes.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                failure.postValue(error.message)
                hideLoading()
            }
        })
    }

    fun logout() {
        viewModelScope.launch {
            signOutUseCase.invoke()
        }
    }


    fun deleteQuiz(quizId: String?) {
        viewModelScope.launch {
            deleteQuizUseCase.run(quizId).collect {
                when (it) {
                    is Response.Success -> {

                    }
                    is Response.Failure -> {
                        failure.postValue(it.e.message)
                        hideLoading()
                    }
                }
            }
        }
    }
}