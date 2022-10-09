package com.assessment.quizebuilder.data

import com.assessment.quizebuilder.core.Response
import com.assessment.quizebuilder.domain.model.QuizEntity
import com.assessment.quizebuilder.domain.repository.QuizRepository
import com.assessment.quizebuilder.presentation.utils.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalCoroutinesApi
class QuizRepositoryImpl @Inject constructor(
    private val dbRef: DatabaseReference,
    private val auth: FirebaseAuth
) : QuizRepository {


    override suspend fun pushQuiz(request: QuizEntity): Flow<Response<Task<Void>>> = flow {
        try {
            val userId = auth.uid
            userId?.let {
                val quizId = dbRef.push().key?.replace("-", "")
                    ?.replace("_", "")
                    ?.substring(0, 6)

                request.publisherId= userId
                request.quizId = quizId

                val result = dbRef
                    .child("${Constants.QUIZ_ID_PREFIX}$quizId")
                    .setValue(request)

                emit(Response.Success(result))
            } ?: kotlin.run {
                emit(Response.Failure(Exception("UserId is Null")))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun getQuizWithQuizId(quizId: String?): Flow<Response<DatabaseReference>> =
        flow {
            try {
                val result = dbRef.child("${Constants.QUIZ_ID_PREFIX}$quizId")
                emit(Response.Success(result))
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }
        }

    override suspend fun deleteQuiz(quizId: String?): Flow<Response<Task<Void>>> = flow {
        try {
            val result = dbRef.child("${Constants.QUIZ_ID_PREFIX}$quizId").removeValue()
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun getAllQuizList(): Flow<Response<DatabaseReference>> = flow {
        try {
            emit(Response.Success(dbRef))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }
}