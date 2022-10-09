package com.assessment.quizebuilder.presentation.quizlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.assessment.quizebuilder.databinding.ContainOtherQuizListBinding
import com.assessment.quizebuilder.presentation.utils.autoCleared
import com.assessment.quizebuilder.presentation.utils.collectWhenStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherQuizListFragment : Fragment() {

    private val viewModel: QuizListViewModel by viewModels()
    private var binding by autoCleared<ContainOtherQuizListBinding>()

    private var otherQuizAdapter: OtherQuizListAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContainOtherQuizListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        initObserver()
        viewModel.getOtherQuizList()
    }

    private fun initList() {
        otherQuizAdapter = OtherQuizListAdapter()

        otherQuizAdapter?.onItemClick = {
            gotoTakeQuiz(it)
        }
        binding.rclOtherQuiz.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = otherQuizAdapter
        }
    }

    private fun gotoTakeQuiz(it: String?) {
        findNavController().navigate(
            PublisherQuizListFragmentDirections.actionGlobalTakeQuizFragment(it, true)
        )
    }

    private fun initObserver() {
        viewModel.listOtherQuizzes.collectWhenStarted(viewLifecycleOwner) {
            otherQuizAdapter?.submitList(it.toMutableList())
        }
    }
}