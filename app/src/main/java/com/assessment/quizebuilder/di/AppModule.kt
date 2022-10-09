package com.assessment.quizebuilder.di

import com.assessment.quizebuilder.data.AuthRepositoryImpl
import com.assessment.quizebuilder.data.QuizRepositoryImpl
import com.assessment.quizebuilder.domain.repository.AuthRepository
import com.assessment.quizebuilder.domain.repository.QuizRepository
import com.assessment.quizebuilder.presentation.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_QUIZ)

    @Provides
    @Singleton
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(auth)

    @Provides
    fun provideQuizRepository(
        dbRef: DatabaseReference,
        auth: FirebaseAuth
    ): QuizRepository = QuizRepositoryImpl(dbRef,auth)
}