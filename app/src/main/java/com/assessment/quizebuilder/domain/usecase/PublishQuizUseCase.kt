package com.assessment.quizebuilder.domain.usecase

import com.assessment.quizebuilder.core.FlowUseCase
import com.assessment.quizebuilder.core.Response
import com.assessment.quizebuilder.domain.repository.QuizRepository
import com.assessment.quizebuilder.domain.model.QuizEntity
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PublishQuizUseCase @Inject constructor(
    private val quizRepository: QuizRepository
): FlowUseCase<QuizEntity, Task<Void>>() {

    override suspend fun run(request: QuizEntity): Flow<Response<Task<Void>>>{
        return quizRepository.pushQuiz(request)
    }
}