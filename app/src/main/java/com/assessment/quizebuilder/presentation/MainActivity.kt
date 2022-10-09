package com.assessment.quizebuilder.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.assessment.quizebuilder.MainNavDirections
import com.assessment.quizebuilder.R
import com.assessment.quizebuilder.presentation.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()
        initObserver()
        checkDeepLink()
    }

    private fun checkDeepLink() {
        intent.data?.let { uri ->
            val parameters = uri.pathSegments
            val param = parameters[parameters.size - 1]

            navController.navigate(
                MainNavDirections.actionGlobalTakeQuizFragment(param, false)
            )
        } ?: kotlin.run {
            viewModel.checkUserAuthentication()
        }
    }

    private fun initObserver() {
        viewModel.userAuthenticated.observe(this) { isAuthenticated ->
            if (isAuthenticated != null && !isAuthenticated) {
                navController.navigateUp()
                navController.navigate(MainNavDirections.actionGlobalLoginFragment())
            }
        }
    }


    private fun setupNavigation() {
        navController = findNavController(R.id.nav_host_fragment)
    }
}