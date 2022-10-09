package com.assessment.quizebuilder.presentation.quizlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.assessment.quizebuilder.R
import com.assessment.quizebuilder.databinding.FragmentQuizListBinding
import com.assessment.quizebuilder.databinding.LayoutLoadingBinding
import com.assessment.quizebuilder.presentation.utils.autoCleared
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizListFragment : Fragment() {

    private val viewModel: QuizListViewModel by viewModels()
    private var binding by autoCleared<FragmentQuizListBinding>()
    private var loadingBinding by autoCleared<LayoutLoadingBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizListBinding.inflate(layoutInflater)
        loadingBinding = binding.loading
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initListener()
        initTab()
    }

    private fun initTab() {
        val activity = activity ?: return

        val adapter = ViewPagerAdapter(
            fragmentManager = activity.supportFragmentManager,
            lifecycle = lifecycle,
            list = getTabItems()
        )

        binding.viewPager.adapter = adapter
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab, position ->
            if(position == 0)
                tab.text = getString(R.string.my_quizzes)
            else
                tab.text = getString(R.string.other_quizzes)

        }.attach()

    }

    private fun getTabItems(): List<Fragment> {
        return listOf(
            PublisherQuizListFragment(),
            OtherQuizListFragment()
        )
    }

    private fun initListener() {
        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(
                QuizListFragmentDirections
                    .actionQuizListFragmentToQuizBuilderFragment()
            )
        }
        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
            findNavController().popBackStack(R.id.nav_host_fragment, true)
            findNavController().navigate(
                QuizListFragmentDirections.actionGlobalLoginFragment()
            )
        }
    }

    private fun initObserver() {
        viewModel.progress.observe(viewLifecycleOwner) {
            loadingBinding.root.isVisible = it
        }
    }

}