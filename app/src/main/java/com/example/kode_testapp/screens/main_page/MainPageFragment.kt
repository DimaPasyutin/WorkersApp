package com.example.kode_testapp.screens.main_page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kode_testapp.R
import com.example.kode_testapp.adapters.Navigator
import com.example.kode_testapp.adapters.ViewPagerAdapter
import com.example.kode_testapp.databinding.FragmentMainBinding
import com.example.kode_testapp.screens.bottom_sheet.BottomSheetFragment
import com.example.kode_testapp.screens.details_page.DetailsPageFragment
import com.example.kode_testapp.screens.factory
import com.google.android.material.tabs.TabLayoutMediator

class MainPageFragment : Fragment() {

    private val mainPageViewModel by viewModels<MainPageViewModel> {factory()}

    var binding: FragmentMainBinding? = null

    lateinit var adapter: ViewPagerAdapter

    private val navigator: Navigator = { id ->
        val fragment = DetailsPageFragment().apply {
            arguments = Bundle().apply {
                putString("ID_WORKER", id)
            }
        }
        parentFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
        .commit()
    }

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

        mainPageViewModel.uiStateChanges.observe(viewLifecycleOwner, { workersUiState ->
            renderState(workersUiState)
        })

        binding!!.searchBar.setOnTouchListener(object : View.OnTouchListener {

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val DRAWABLE_RIGHT = 2
                if(event.action == MotionEvent.ACTION_UP) {
                    if(event.rawX >= (binding!!.searchBar.right - binding!!.searchBar.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        val bottomSheetFragment = BottomSheetFragment()
                        bottomSheetFragment.show(parentFragmentManager, null)
                        return true;
                    }
                }
                return false;
            }

        })

    }

    private fun renderState(workersUiState: WorkersUiState) {
        when {
            !workersUiState.isLoading && workersUiState.error == null -> {

                adapter = ViewPagerAdapter(DepartmentPage(workersUiState.workers), navigator)
                adapter.departments = mainPageViewModel.app.departmentList
                binding!!.viewPager2.adapter = adapter

                TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager2){ tab, position->
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