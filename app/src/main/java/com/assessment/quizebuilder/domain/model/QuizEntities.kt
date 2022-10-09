package com.assessment.quizebuilder.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnswerEntity(
    val answerId: Long?,
    val title: String?,
    val isCorrect: Boolean?
) : Parcelable


@Parcelize
data class QuestionEntity(
    var questionId: Long? = 0,
    val title: String?,
    val list: List<AnswerEntity>? = listOf(),
    val type: QuestionType? = null
) : Parcelable

enum class QuestionType {
    SINGLE,
    MULTIPLE
}

data class QuizEntity(
    var quizId: String? = null,
    var publisherId: String? = null,
    val title: String,
    val listQuestions: ArrayList<QuestionEntity>?
)

data class UserMultiAnswerEntity(val questionId: Long?, val answerId: Long?)
