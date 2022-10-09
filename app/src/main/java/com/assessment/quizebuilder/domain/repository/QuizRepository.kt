package com.assessment.quizebuilder.domain.repository

import com.assessment.quizebuilder.core.Response
import com.assessment.quizebuilder.domain.model.QuizEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow

interface QuizRepository {

    suspend fun pushQuiz(
        request: QuizEntity
    ): Flow<Response<Task<Void>>>

    suspend fun getQuizWithQuizId(quizId: String?): Flow<Response<DatabaseReference>>

    suspend fun deleteQuiz(quizId: String?): Flow<Response<Task<Void>>>

    suspend fun getAllQuizList(): Flow<Response<DatabaseReference>>
}