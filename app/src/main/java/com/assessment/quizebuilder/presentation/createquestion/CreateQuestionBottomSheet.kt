package com.assessment.quizebuilder.presentation.createquestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.assessment.quizebuilder.R
import com.assessment.quizebuilder.databinding.BottomSheetCreateQuestionBinding
import com.assessment.quizebuilder.databinding.ContainCreateQuestionBinding
import com.assessment.quizebuilder.domain.model.QuestionType
import com.assessment.quizebuilder.presentation.utils.Constants
import com.assessment.quizebuilder.presentation.utils.autoCleared
import com.assessment.quizebuilder.presentation.utils.collectWhenStarted
import com.assessment.quizebuilder.presentation.utils.setBackStackData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateQuestionBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: CreateQuestionViewModel by viewModels()
    private var binding by autoCleared<BottomSheetCreateQuestionBinding>()
    private var containBinding by autoCleared<ContainCreateQuestionBinding>()


    companion object {
        const val MAX_ANSWER = 5
    }

    var flagCorrect: Boolean = false
    private var answerAdapter: AnswerAdapter? = null

    override fun onStart() {
        super.onStart()
        val view: FrameLayout =
            dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
        view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        val behavior = BottomSheetBehavior.from(view)
        behavior.peekHeight = 3000
        behavior.isHideable = false
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetCreateQuestionBinding.inflate(layoutInflater)
        containBinding = binding.containCreateQuestion
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        initListener()
        initObserver()
    }

    private fun initList() {
        answerAdapter = AnswerAdapter()
        answerAdapter?.onItemClick = { viewModel.removeAnswer(it) }
        containBinding.rclAnswers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = answerAdapter
        }
    }

    private fun initObserver() {
        viewModel.isEnableCreateQuestion.collectWhenStarted(viewLifecycleOwner) {
            containBinding.buttonCreateQuestion.isEnabled = it
        }
        viewModel.listAnswer.observe(viewLifecycleOwner) { list ->
            answerAdapter?.submitList(list.toMutableList())
        }
    }

    private fun initListener() {
        binding.buttonBack.setOnClickListener {
            dialog?.dismiss()
        }
        containBinding.buttonCreateQuestion.setOnClickListener {
            setBackStackData(
                Constants.KEY_PASS_DATA_CREATE_QUESTION,
                viewModel.getQuestion(),
                doBack = true
            )
        }
        containBinding.buttonAddAnswer.setOnClickListener {
            if (isValidAnswer()) {
                viewModel.addAnswer(
                    containBinding.edittextAnswer.text.toString(),
                    containBinding.checkboxCorrectAnswer.isChecked
                )
                clear()
            }
        }
        containBinding.edittextAnswer.doAfterTextChanged {
            containBinding.buttonAddAnswer.isEnabled = it.toString().isNotEmpty()
        }
        containBinding.edittextQuestion.doAfterTextChanged {
            viewModel.setQuestionTitle(it.toString())
        }
        containBinding.radioButtonSingleChoice.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.setQuestionType(QuestionType.SINGLE)
        }
        containBinding.radioButtonMutliChoice.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.setQuestionType(QuestionType.MULTIPLE)
        }
    }

    private fun isValidAnswer(): Boolean {
        containBinding.radioButtonSingleChoice.isEnabled = false
        containBinding.radioButtonMutliChoice.isEnabled = false

        val isSingle = containBinding.radioButtonSingleChoice.isChecked
        val isMultiple = containBinding.radioButtonMutliChoice.isChecked
        val isCorrect = containBinding.checkboxCorrectAnswer.isChecked
        val answerCount = viewModel.getAnswerCount()

        if (isSingle && !isCorrect && !flagCorrect && (answerCount + 1) == MAX_ANSWER) {
            Toast.makeText(
                requireContext(),
                getString(R.string.select_at_least_one_correct_answer),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        if (isSingle && isCorrect && !flagCorrect && answerCount < MAX_ANSWER) {
            flagCorrect = true
            return true
        } else if (isSingle && !isCorrect && viewModel.getAnswerCount() < MAX_ANSWER) {
            return true
        }

        if (isMultiple && !isCorrect && !flagCorrect && (answerCount + 1) == MAX_ANSWER) {
            Toast.makeText(
                requireContext(),
                getString(R.string.select_at_least_one_correct_answer),
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (isMultiple && answerCount < MAX_ANSWER) {
            flagCorrect = true
            return true
        }

        Toast.makeText(requireContext(), "Answer is not valid", Toast.LENGTH_LONG).show()
        return false
    }

    private fun clear() {
        containBinding.edittextAnswer.setText("")
        containBinding.checkboxCorrectAnswer.isChecked = false
    }

    override fun onDestroyView() {
        answerAdapter?.submitList(null)
        answerAdapter = null
        super.onDestroyView()
    }

}