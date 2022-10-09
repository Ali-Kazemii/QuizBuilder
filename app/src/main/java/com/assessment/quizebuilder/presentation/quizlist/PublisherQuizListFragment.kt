package com.assessment.quizebuilder.presentation.quizlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.assessment.quizebuilder.R
import com.assessment.quizebuilder.databinding.ContainPublisherQuizListBinding
import com.assessment.quizebuilder.presentation.utils.Constants.QUIZ_URL
import com.assessment.quizebuilder.presentation.utils.autoCleared
import com.assessment.quizebuilder.presentation.utils.collectWhenStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PublisherQuizListFragment : Fragment() {

    private val viewModel: QuizListViewModel by viewModels()
    private var binding by autoCleared<ContainPublisherQuizListBinding>()

    private var publisherQuizAdapter: PublisherQuizListAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContainPublisherQuizListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        initObserver()
        viewModel.getPublisherQuizList()
    }

    private fun initList() {
        publisherQuizAdapter = PublisherQuizListAdapter()

        publisherQuizAdapter?.onItemClick = {
            gotoTakeQuiz(it)
        }
        publisherQuizAdapter?.onShareItemClick = { quizId ->
            shareQuiz(quizId)
        }
        publisherQuizAdapter?.onDeleteItemClick = { quizId ->
            viewModel.deleteQuiz(quizId)
        }
        binding.rclPublisherQuiz.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = publisherQuizAdapter
        }
    }

    private fun gotoTakeQuiz(it: String?) {
        findNavController().navigate(
            PublisherQuizListFragmentDirections.actionGlobalTakeQuizFragment(it, true)
        )
    }

    private fun shareQuiz(title: String?) = ShareCompat.IntentBuilder(requireContext())
        .setType("text/plain")
        .setChooserTitle(getString(R.string.share_quiz_url))
        .setText("$QUIZ_URL/$title")
        .startChooser()

    private fun initObserver() {
        viewModel.listPublisherQuizzes.collectWhenStarted(viewLifecycleOwner) {
            publisherQuizAdapter?.submitList(it.toMutableList())
        }
    }
}