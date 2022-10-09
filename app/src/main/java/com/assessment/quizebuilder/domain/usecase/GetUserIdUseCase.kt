package com.assessment.quizebuilder.domain.usecase

import com.assessment.quizebuilder.core.FlowUseCase
import com.assessment.quizebuilder.core.Response
import com.assessment.quizebuilder.domain.repository.AuthRepository
import com.assessment.quizebuilder.domain.repository.QuizRepository
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(
    private val authRepository: AuthRepository
): FlowUseCase<Unit, String?>(){

    override suspend fun run(request: Unit): Flow<Response<String?>> {
        return authRepository.getUserId()
    }
}