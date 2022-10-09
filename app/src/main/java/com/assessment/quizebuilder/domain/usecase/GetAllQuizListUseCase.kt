package com.assessment.quizebuilder.domain.usecase

import com.assessment.quizebuilder.core.FlowUseCase
import com.assessment.quizebuilder.core.Response
import com.assessment.quizebuilder.domain.repository.QuizRepository
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllQuizListUseCase  @Inject constructor(
    private val quizRepository: QuizRepository
): FlowUseCase<Unit, DatabaseReference>() {

    override suspend fun run(request: Unit): Flow<Response<DatabaseReference>> {
        return quizRepository.getAllQuizList()
    }
}