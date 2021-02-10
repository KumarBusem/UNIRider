package com.uni.rider.features.home

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.uni.data.models.MonthYear
import com.uni.rider.R
import com.uni.rider.common.*
import com.uni.rider.databinding.FragmentHomeBinding
import com.whiteelephant.monthpicker.MonthPickerDialog
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_runsheets.*
import java.text.DateFormatSymbols
import java.util.*

class HomeFragment : BaseAbstractFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home) {

    override fun setViewModel(): HomeViewModel =
            ViewModelProvider(this@HomeFragment, ViewModelFactory {
                HomeViewModel(requireActivity().application)
            }).get(HomeViewModel::class.java)

    override fun setupViews(): FragmentHomeBinding.() -> Unit = {
        toggleBottomBarVisibility(true)

        cvDateLayout.setOnClickListener { showMonthPicker() }

        srlRunsheets.setOnRefreshListener {
            mBinding.srlRunsheets.isRefreshing = false
            mViewModel.getRunsheetsList()
        }

        btnAddRunsheet.setOnClickListener { navigateById(R.id.action_homeFragment_to_addRunsheetFragment) }
        btnRunsheets.setOnClickListener { navigateById(R.id.action_homeFragment_to_runsheetsFragment) }

        checkTerms()
    }

    private fun checkTerms() {
        if (repoPrefs.getLoggedInUser()?.self_declaration == false) {
            navigateById(R.id.action_homeFragment_to_termsFragment)
        }
    }

    override fun setupObservers(): HomeViewModel.() -> Unit = {

        obsRunsheetssList.observe(viewLifecycleOwner, Observer { runsheets ->
            if (runsheets.isNullOrEmpty()) {
                mBinding.tvNoData.show()
                mBinding.cvData.invisible()
                mBinding.btnRunsheets.invisible()
            } else {
                var ofd = 0f
                var delivered = 0f
                var noOfRunsheets = 0
                var conversion = 0f
                runsheets.forEach {
                    ofd += it.ofd
                    delivered += it.delivered
                    noOfRunsheets++
                }
                mBinding.tvOFDValue.text = String.format("%.0f", ofd)
                mBinding.tvDeliveredValue.text = String.format("%.0f", delivered)
                mBinding.tvRunsheetsValue.text = noOfRunsheets.toString()
                mBinding.tvConversionValue.text = String.format("%.1f", ((delivered / ofd) * 100f)) + "%"
                mBinding.btnRunsheets.enable()

                mBinding.tvNoData.invisible()
                mBinding.cvData.show()
                mBinding.btnRunsheets.show()
            }
        })
        obsMonthYear.observe(viewLifecycleOwner, Observer { monthYear ->
            mBinding.tvDate.text = "${DateFormatSymbols().months[monthYear.month - 1]} ${monthYear.year}"
            mBinding.tvRunsheetsForSelectedMonth.text = "${DateFormatSymbols().months[monthYear.month - 1]} ${monthYear.year} Runsheets"
        })


    }

    private fun showMonthPicker() {
        var builder = MonthPickerDialog.Builder(requireContext(), { selectedMonth, selectedYear ->
            repoPrefs.saveMonthYear(MonthYear(selectedMonth + 1, selectedYear))
            mViewModel.getRunsheetsList()
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH))
        builder.build().show()
    }

    override fun onResume() {
        mViewModel.getRunsheetsList()
        super.onResume()
    }


}