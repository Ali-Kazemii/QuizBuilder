package com.assessment.quizebuilder.domain.usecase

import com.assessment.quizebuilder.core.FlowUseCase
import com.assessment.quizebuilder.core.Response
import com.assessment.quizebuilder.domain.repository.QuizRepository
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteQuizUseCase @Inject constructor(
    private val quizRepository: QuizRepository
): FlowUseCase<String?, Task<Void>>(){

    override suspend fun run(request: String?): Flow<Response<Task<Void>>> {
        return quizRepository.deleteQuiz(quizId = request)
    }
}