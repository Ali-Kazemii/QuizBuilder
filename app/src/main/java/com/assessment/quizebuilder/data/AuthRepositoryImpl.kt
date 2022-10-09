package com.assessment.quizebuilder.data

import com.assessment.quizebuilder.core.Response
import com.assessment.quizebuilder.domain.repository.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalCoroutinesApi
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun firebaseSignIn(
        email: String,
        password: String
    ): Flow<Response<Task<AuthResult>>> = flow {
        try {
            val result = auth.signInWithEmailAndPassword(email, password)
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }


    override suspend fun firebaseSignUp(
        email: String,
        password: String
    ): Flow<Response<Task<AuthResult>>> = flow {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password)
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun firebaseSignOut(): Flow<Response<Boolean>> = flow {
        try {
            auth.signOut()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun isUserAuthenticatedInFirebase(): Flow<Response<Boolean>> = flow {
        try {
            val result = auth.currentUser != null
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun getUserId(): Flow<Response<String?>> = flow {
        try {
            val userId = auth.uid
            emit(Response.Success(userId))

        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }
}