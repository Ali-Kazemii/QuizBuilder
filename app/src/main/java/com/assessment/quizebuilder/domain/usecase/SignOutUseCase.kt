package com.assessment.quizebuilder.domain.usecase

import com.assessment.quizebuilder.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.firebaseSignOut()
}