package com.assessment.quizebuilder.domain.usecase

import com.assessment.quizebuilder.core.FlowUseCase
import com.assessment.quizebuilder.core.Response
import com.assessment.quizebuilder.domain.repository.AuthRepository
import com.assessment.quizebuilder.domain.model.LoginEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
): FlowUseCase<LoginEntity, Task<AuthResult>>(){

    override suspend fun run(request: LoginEntity): Flow<Response<Task<AuthResult>>> {
        return  authRepository.firebaseSignUp(request.email, request.password)
    }
}