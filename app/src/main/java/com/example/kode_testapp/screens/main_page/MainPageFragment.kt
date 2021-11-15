package com.example.kode_testapp.screens.main_page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kode_testapp.R
import com.example.kode_testapp.adapters.ViewPagerAdapter
import com.example.kode_testapp.databinding.FragmentMainBinding
import com.example.kode_testapp.retrofit.Worker
import com.example.kode_testapp.screens.bottom_sheet.BottomSheetFragment
import com.example.kode_testapp.screens.details_page.DetailsPageFragment
import com.example.kode_testapp.screens.factory
import com.google.android.material.tabs.TabLayoutMediator


class MainPageFragment : Fragment() {

    private val mainPageViewModel by viewModels<MainPageViewModel> { factory() }

    private var binding: FragmentMainBinding? = null

    private var adapter: ViewPagerAdapter? = null

    private val requireBinding: FragmentMainBinding
        get() = binding!!

    private val requireAdapter: ViewPagerAdapter
        get() = adapter!!

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
        val viewBinding = FragmentMainBinding.inflate(layoutInflater, container, false)
        this.binding = viewBinding
        adapter = ViewPagerAdapter(object : ViewPagerAdapter.Listener {
            override fun onRefresh(page: DepartmentPage) {
                mainPageViewModel.refresh(page)
            }

            override fun onWorkerClick(worker: Worker) {
                navigateToWorker(worker)
            }
        })
        viewBinding.viewPager2.adapter = adapter

        TabLayoutMediator(
            requireBinding.tabLayout,
            requireBinding.viewPager2
        ) { tab, position ->
            tab.text = mainPageViewModel.departments[position].name
        }.attach()

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireBinding.searchBar.doAfterTextChanged {
            val selectedPosition = requireBinding.tabLayout.selectedTabPosition
            if (selectedPosition == -1) return@doAfterTextChanged
            mainPageViewModel.search(
                requireAdapter.currentList[selectedPosition],
                it?.toString() ?: ""
            )
        }
        mainPageViewModel.uiStateChanges.observe(viewLifecycleOwner, { workersUiState ->
            renderState(workersUiState)
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun renderState(workersUiState: WorkersUiState) {
        when {
            workersUiState.isRefreshing -> requireAdapter.submitList(workersUiState.pages)

            workersUiState.isLoading -> {
                requireBinding.shimmer.visibility = View.VISIBLE
                requireBinding.shimmer.startShimmer()

                binding!!.searchBar.setOnTouchListener(OnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_UP) {
                        if (event.rawX >= binding!!.searchBar.right - binding!!.searchBar.compoundDrawables[2].bounds.width()
                        ) {
                            BottomSheetFragment().show(this.parentFragmentManager, null)
                            return@OnTouchListener true
                        }
                    }
                    false
                })
            }

            workersUiState.error != null && workersUiState.pages.isNotEmpty() -> {
                requireBinding.let {
                    setVisibilityError(View.INVISIBLE)
                    setVisibilitySuccess(View.VISIBLE)
                    it.shimmer.stopShimmer()
                }
            }

            workersUiState.error != null -> {
                requireBinding.let {
                    setVisibilityError(View.VISIBLE)
                    setVisibilitySuccess(View.INVISIBLE)
                    it.shimmer.visibility = View.INVISIBLE
                    it.shimmer.stopShimmer()
                    it.textViewTryAgain.setOnClickListener {
                        mainPageViewModel.loadWorkers()
                        setVisibilityError(View.INVISIBLE)
                        setVisibilitySuccess(View.VISIBLE)
                    }
                }
            }

//            !workersUiState.isSearchSucces -> {
//                requireBinding.let {
//                    setVisibilityNotFoundSearch(View.VISIBLE)
//                    it.viewPager2.visibility = View.INVISIBLE
//                }
//            }

            else -> {
                requireAdapter.submitList(workersUiState.pages)

                requireBinding.let {
                    setVisibilitySuccess(View.VISIBLE)
                    setVisibilityError(View.INVISIBLE)
                    it.shimmer.visibility = View.INVISIBLE
                    it.shimmer.stopShimmer()
                }
            }
        }
    }

    private fun setVisibilityNotFoundSearch(visible: Int) {
        requireBinding.let {
            it.imageViewNotFind.visibility = visible
            it.firstLineNotFind.visibility = visible
            it.secondLineNotFind.visibility = visible
        }
    }

    private fun setVisibilityError(visible: Int) {
        requireBinding.let {
            it.imageViewProblem.visibility = visible
            it.firstLine.visibility = visible
            it.secondLine.visibility = visible
            it.textViewTryAgain.visibility = visible
        }
    }

    private fun setVisibilitySuccess(visible: Int) {
        requireBinding.let {
            it.searchBar.visibility = visible
            it.tabLayout.visibility = visible
        }
    }

    private fun navigateToWorker(worker: Worker) {
        val fragment = DetailsPageFragment().apply {
            arguments = Bundle().apply {
                putString("ID_WORKER", worker.id)
            }
        }
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        adapter = null
    }
}