package com.assessment.quizebuilder.data

import com.assessment.quizebuilder.domain.model.AnswerEntity
import com.assessment.quizebuilder.domain.model.QuestionType
import com.assessment.quizebuilder.presentation.takequiz.TakeQuizViewModel
import com.google.firebase.database.DataSnapshot

fun mapToAnswerEntity(children: MutableIterable<DataSnapshot>): List<AnswerEntity> {
    val list = arrayListOf<AnswerEntity>()
    children.forEach { data ->
        val entity = AnswerEntity(
            answerId = data.child("answerId").value as Long?,
            title = data.child("title").value as String?,
            isCorrect = data.child("correct").value as Boolean?
        )
        list.add(entity)
    }
    return list
}

fun mapToQuestionType(typeString: String?): QuestionType {
    return if (typeString == TakeQuizViewModel.SINGLE)
        QuestionType.SINGLE
    else QuestionType.MULTIPLE
}