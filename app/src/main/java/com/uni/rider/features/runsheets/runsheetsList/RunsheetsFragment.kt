package com.uni.rider.features.runsheets.runsheetsList

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.uni.data.models.Runsheet
import com.uni.rider.R
import com.uni.rider.common.BaseAbstractFragment
import com.uni.rider.common.ViewModelFactory
import com.uni.rider.databinding.FragmentRunsheetsBinding
import kotlinx.android.synthetic.main.fragment_runsheets.*
import java.text.DateFormatSymbols
import java.util.*

class RunsheetsFragment : BaseAbstractFragment<RunsheetsViewModel, FragmentRunsheetsBinding>(R.layout.fragment_runsheets),
        RunsheetsListAdapter.ItemSelectionCallback {

    override fun setViewModel(): RunsheetsViewModel =
            ViewModelProvider(this@RunsheetsFragment, ViewModelFactory {
                RunsheetsViewModel(requireActivity().application)
            }).get(RunsheetsViewModel::class.java)

    private val mUserAdapter: RunsheetsListAdapter by lazy {
        RunsheetsListAdapter(this@RunsheetsFragment)
    }

    override fun setupViews(): FragmentRunsheetsBinding.() -> Unit = {
        toggleBottomBarVisibility(false)
        rvRunsheetsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mUserAdapter
        }
        srlRunsheets.setOnRefreshListener {
            mBinding.srlRunsheets.isRefreshing = false
            mViewModel.getRunsheetsList()
        }
        ivBack.setOnClickListener { navigateBack() }
    }

    override fun setupObservers(): RunsheetsViewModel.() -> Unit = {

        obsRunsheetssList.observe(viewLifecycleOwner, Observer { runsheets ->
            if (runsheets.isNullOrEmpty()) {
                mUserAdapter.submitList(emptyList())
            } else {
                mUserAdapter.submitList(runsheets.toMutableList())
            }
        })
        obsMonthYear.observe(viewLifecycleOwner, Observer { monthYear ->
            tvRunsheetHeading.text = "${DateFormatSymbols().months[monthYear.month - 1]} ${monthYear.year} Runsheets"
        })
    }

    override fun onSelectRunsheetClick(runsheet: Runsheet) {
        repoPrefs.saveSelectedRunsheetId(runsheet.id)
    }

    override fun onResume() {
        mViewModel.getRunsheetsList()
        super.onResume()
    }
}