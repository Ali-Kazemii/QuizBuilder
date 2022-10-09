package com.assessment.quizebuilder.presentation.quizbuilder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.assessment.quizebuilder.R
import com.assessment.quizebuilder.databinding.ContainQuizBuilderBinding
import com.assessment.quizebuilder.databinding.FragmentQuizBuilderBinding
import com.assessment.quizebuilder.databinding.LayoutLoadingBinding
import com.assessment.quizebuilder.domain.model.QuestionEntity
import com.assessment.quizebuilder.presentation.utils.Constants
import com.assessment.quizebuilder.presentation.utils.autoCleared
import com.assessment.quizebuilder.presentation.utils.collectWhenStarted
import com.assessment.quizebuilder.presentation.utils.getBackStackDataFromDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizBuilderFragment : Fragment() {

    private var binding by autoCleared<FragmentQuizBuilderBinding>()
    private var containBinding by autoCleared<ContainQuizBuilderBinding>()
    private var loadingBinding by autoCleared<LayoutLoadingBinding>()

    private val viewModel: QuizBuilderViewModel by viewModels()
    private lateinit var questionAdapter: QuestionAdapter

    companion object {
        const val MAX_QUESTION = 10
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBuilderBinding.inflate(layoutInflater)
        containBinding = binding.containQuizBuilder
        loadingBinding = binding.loading
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        initList()
        initObserver()
        initCallback()
        initListener()
    }

    private fun initList() {
        questionAdapter = QuestionAdapter()
        questionAdapter.onItemClick = { viewModel.removeQuestion(it) }
        containBinding.rclQuestion.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = questionAdapter
        }
    }

    private fun initObserver() {
        viewModel.listQuestion.observe(viewLifecycleOwner) { list ->
            questionAdapter.submitList(list.toMutableList())
        }
        viewModel.isEnablePublish.collectWhenStarted(viewLifecycleOwner) {
            containBinding.buttonPublish.isEnabled = it
        }
        viewModel.success.observe(viewLifecycleOwner) {
            findNavController().navigate(
                QuizBuilderFragmentDirections.actionQuizBuilderFragmentToQuizListFragment()
            )
        }
        viewModel.failure.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        viewModel.progress.observe(viewLifecycleOwner){
            loadingBinding.root.isVisible = it
        }
    }

    private fun initCallback(){
        getBackStackDataFromDialog<QuestionEntity>(
            R.id.quizBuilderFragment,
            Constants.KEY_PASS_DATA_CREATE_QUESTION,
        ){
            viewModel.addQuestion(it)
        }

        getBackStackDataFromDialog<String>(
            R.id.quizBuilderFragment,
            Constants.KEY_PASS_DATA_QUIZ_NAME,
        ){
            viewModel.publishQuiz(it)
        }
    }
    private fun initListener() {
        binding.buttonAdd.setOnClickListener {
            if (viewModel.getQuestionCount() <= MAX_QUESTION)
                findNavController().navigate(
                    QuizBuilderFragmentDirections.actionQuizBuilderFragmentToCreateQuestionBottomSheet()
                )
            else
                Toast.makeText(requireContext(), "Max question is $MAX_QUESTION", Toast.LENGTH_LONG).show()
        }
        containBinding.buttonPublish.setOnClickListener {
            findNavController().navigate(
                QuizBuilderFragmentDirections.actionQuizBuilderFragmentToQuizNameBottomSheet()
            )
        }
    }
}