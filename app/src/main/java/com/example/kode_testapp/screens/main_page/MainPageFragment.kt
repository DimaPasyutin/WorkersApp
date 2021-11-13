package com.example.kode_testapp.screens.main_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.kode_testapp.adapters.ViewPagerAdapter
import com.example.kode_testapp.databinding.FragmentMainBinding
import com.example.kode_testapp.screens.factory
import com.google.android.material.tabs.TabLayoutMediator

class MainPageFragment : Fragment() {

    private val mainPageViewModel by viewModels<MainPageViewModel> {factory()}

    var binding: FragmentMainBinding? = null

    lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            mainPageViewModel.loadWorkers()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mainPageViewModel.uiStateChanges.observe( viewLifecycleOwner, { workersUiState ->
            renderState(workersUiState)
        })

    }

    private fun renderState(workersUiState: WorkersUiState) {
        when {
            !workersUiState.isLoading && workersUiState.error == null -> {

                adapter = ViewPagerAdapter(DepartmentPage(workersUiState.workers))
                adapter.departments = mainPageViewModel.app.departmentList
                binding!!.viewPager2.adapter = adapter

                TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager2){ tab,position->
                    if (mainPageViewModel.app.departmentList[position].position == position) {
                        tab.text = mainPageViewModel.app.departmentList[position].name
                    } else {
                        tab.text = ""
                    }
                }.attach()

                binding!!.let {
                    setVisibilitySuccess(View.VISIBLE)
                    setVisibilityError(View.INVISIBLE)
                    it.shimmer.visibility = View.INVISIBLE
                    it.shimmer.stopShimmer()
                }
            }

            workersUiState.isLoading && workersUiState.error == null -> {

                binding!!.shimmer.visibility = View.VISIBLE
                binding!!.shimmer.startShimmer()

            }

            !workersUiState.isLoading && workersUiState.error !== null -> {

                binding!!.let {
                    setVisibilityError(View.VISIBLE)
                    setVisibilitySuccess(View.INVISIBLE)
                    it.shimmer.visibility = View.INVISIBLE
                    it.shimmer.stopShimmer()
                    it.textViewTryAgain.setOnClickListener {
                        mainPageViewModel.loadWorkers()
                    }
                }

            }
        }
    }

    private fun setVisibilityError(visible: Int) {
        binding!!.let {
            it.imageViewProblem.visibility = visible
            it.firstLine.visibility = visible
            it.secondLine.visibility = visible
            it.textViewTryAgain.visibility = visible
        }
    }

    private fun setVisibilitySuccess(visible: Int) {
        binding!!.let {
            it.searchBar.visibility = visible
            it.tabLayout.visibility = visible
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}