package com.assessment.quizebuilder.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.assessment.quizebuilder.BuildConfig
import com.assessment.quizebuilder.databinding.FragmentRegisterBinding
import com.assessment.quizebuilder.presentation.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private var binding by autoCleared<FragmentRegisterBinding>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initListener()
    }

    private fun initObserver(){
        viewModel.success.observe(viewLifecycleOwner){
            hideProgressBar()
            findNavController().navigate(
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            )
        }
        viewModel.failure.observe(viewLifecycleOwner) {
            hideProgressBar()
            val message = if (BuildConfig.DEBUG) "Login Error: $it" else "Login Error"
            Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun initListener() {
        binding.buttonRegister.setOnClickListener {
            val email = binding.edittextEmail.text.toString()
            val password = binding.edittextPassword.text.toString()
            if (isValidForm(email, password)) {
                showProgressBar()
                viewModel.createUser(email, password)
            }
        }
        binding.buttonLogin.setOnClickListener {
            findNavController().navigate(
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            )
        }
    }


    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
    }

    private fun showProgressBar() {
        binding.progressBar.isVisible = true
    }

    private fun isValidForm(email: String, password: String): Boolean {
        return if (email.isEmpty()) {
            binding.tilEmail.error = "email is required"
            binding.edittextEmail.requestFocus()
            false
        } else if (password.isEmpty()) {
            binding.tilPassword.error = "password is required"
            binding.edittextPassword.requestFocus()
            false
        } else {
            binding.tilEmail.error = null
            binding.tilPassword.error = null
            true
        }
    }
}