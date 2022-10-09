package com.assessment.quizebuilder.presentation.quizbuilder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.assessment.quizebuilder.R
import com.assessment.quizebuilder.databinding.BottomSheetQuizNameBinding
import com.assessment.quizebuilder.presentation.utils.Constants
import com.assessment.quizebuilder.presentation.utils.autoCleared
import com.assessment.quizebuilder.presentation.utils.setBackStackData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class QuizNameBottomSheet : BottomSheetDialogFragment() {

    private var binding by autoCleared<BottomSheetQuizNameBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetQuizNameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            val quizName = binding.edittextQuizName.text.toString()
            if (quizName.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_enter_a_name),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                setBackStackData(
                    Constants.KEY_PASS_DATA_QUIZ_NAME,
                    quizName,
                    doBack = true
                )
            }
        }
    }
}