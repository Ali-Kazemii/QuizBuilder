package com.assessment.quizebuilder.presentation.login

import androidx.lifecycle.viewModelScope
import com.assessment.quizebuilder.core.BaseViewModel
import com.assessment.quizebuilder.core.Response
import com.assessment.quizebuilder.core.SingleLiveEvent
import com.assessment.quizebuilder.domain.model.LoginEntity
import com.assessment.quizebuilder.domain.usecase.IsUserAuthenticatedUseCase
import com.assessment.quizebuilder.domain.usecase.SignInUseCase
import com.assessment.quizebuilder.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase,
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
) : BaseViewModel() {

    val userAuthenticated = SingleLiveEvent<Boolean?>()

    fun checkUserAuthentication(){
        viewModelScope.launch {
            isUserAuthenticatedUseCase.run(Unit).collect{
                when(it){
                    is Response.Success ->{
                        userAuthenticated.postValue(it.data)
                    }
                    is Response.Failure ->{
                        userAuthenticated.postValue(false)
                    }
                }
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase.run(LoginEntity(email, password)).collect {
                when (it) {
                    is Response.Success -> {
                        it.data.addOnCompleteListener{ result ->
                            if(result.isSuccessful)
                                success.postValue(true)
                            else
                                failure.postValue("Error login user: ${result.exception?.message}")
                        }

                    }
                    is Response.Failure -> {
                        failure.postValue(it.e.message)
                    }
                }
            }
        }
    }

    fun createUser(email: String, password: String) {
        viewModelScope.launch {
            signUpUseCase.run(LoginEntity(email, password)).collect{
                when (it) {
                    is Response.Success -> {
                        it.data.addOnCompleteListener{ result ->
                            if(result.isSuccessful)
                                success.postValue(true)
                            else
                                failure.postValue("Error Create User: ${result.exception?.message}")
                        }

                    }
                    is Response.Failure -> {
                        failure.postValue(it.e.message)
                    }
                }
            }
        }
    }
}