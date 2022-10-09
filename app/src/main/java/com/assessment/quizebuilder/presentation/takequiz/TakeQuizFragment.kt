package com.assessment.quizebuilder.presentation.takequiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.assessment.quizebuilder.databinding.ContainTakeQuizBinding
import com.assessment.quizebuilder.databinding.FragmentTakeQuizBinding
import com.assessment.quizebuilder.databinding.LayoutLoadingBinding
import com.assessment.quizebuilder.presentation.utils.autoCleared
import com.assessment.quizebuilder.presentation.utils.collectWhenStarted
import com.assessment.quizebuilder.presentation.utils.exitAlertDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TakeQuizFragment : Fragment() {

    private var binding by autoCleared<FragmentTakeQuizBinding>()
    private var containBinding by autoCleared<ContainTakeQuizBinding>()
    private var loadingBinding by autoCleared<LayoutLoadingBinding>()

    private val viewModel: TakeQuizViewModel by viewModels()
    private val args: TakeQuizFragmentArgs by navArgs()

    private lateinit var quizAdapter: QuizAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTakeQuizBinding.inflate(layoutInflater)
        containBinding = binding.containTakeQuiz
        loadingBinding = binding.loading
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        initObserver()
        initListener()
        initBackPressed()

        args.quizId?.let { quizId ->
            viewModel.getQuiz(quizId)
        }
    }

    private fun initBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!args.isRegisteredUser)
                        requireActivity().exitAlertDialog()
                    else
                        findNavController().popBackStack()
                }
            })
    }

    private fun initList() {
        quizAdapter = QuizAdapter()
        quizAdapter.onSingleCorrectAnswerClick = {
            viewModel.addCorrectAnswer(it)
        }
        quizAdapter.onMultipleCorrectAnswerClick = { questionId, answerId ->
            viewModel.onMultipleCorrectAnswer(questionId, answerId)
        }
        quizAdapter.onDeleteSingleCorrectAnswerClick = {
            viewModel.removeSingleAnswer(it)
        }
        quizAdapter.onDeleteMultipleCorrectAnswerClick = { questionId, answerId ->
            viewModel.removeMultipleAnswer(questionId, answerId)
        }
        containBinding.rclQuiz.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = quizAdapter
        }
    }

    private fun initObserver() {
        viewModel.listQuestion.collectWhenStarted(viewLifecycleOwner) { list ->
            if (!list.isNullOrEmpty())
                quizAdapter.setSource(list)
        }
        viewModel.failure.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        viewModel.quizResult.observe(viewLifecycleOwner) {
            containBinding.groupQuiz.isVisible = false
            containBinding.groupResult.isVisible = true
            containBinding.textviewResult.text = it
        }
        viewModel.progress.observe(viewLifecycleOwner) {
            loadingBinding.root.isVisible = it
        }
    }

    private fun initListener() {
        containBinding.buttonResult.setOnClickListener {
            viewModel.showQuizResult()
        }
        containBinding.buttonExit.setOnClickListener {
            requireActivity().exitAlertDialog()
        }
    }
}