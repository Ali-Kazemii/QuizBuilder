package com.assessment.quizebuilder.domain.repository

import com.assessment.quizebuilder.core.Response
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun firebaseSignIn(
        email: String,
        password: String
    ): Flow<Response<Task<AuthResult>>>

    suspend fun firebaseSignUp(
        email: String,
        password: String
    ): Flow<Response<Task<AuthResult>>>

    suspend fun firebaseSignOut(): Flow<Response<Boolean>>

    suspend fun isUserAuthenticatedInFirebase(): Flow<Response<Boolean>>

    suspend fun getUserId(): Flow<Response<String?>>
}