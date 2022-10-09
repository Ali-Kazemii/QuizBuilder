package com.assessment.quizebuilder.domain.usecase

import com.assessment.quizebuilder.core.FlowUseCase
import com.assessment.quizebuilder.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsUserAuthenticatedUseCase  @Inject constructor(
    private val authRepository: AuthRepository
): FlowUseCase<Unit, Boolean>() {

    override suspend fun run(request: Unit): Flow<com.assessment.quizebuilder.core.Response<Boolean>> {
        return authRepository.isUserAuthenticatedInFirebase()
    }
}