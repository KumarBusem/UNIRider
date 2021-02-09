package com.uni.rider.features.home

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
        cvProfileDetails.setOnClickListener { navigateById(R.id.action_homeFragment_to_profileFragment) }
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
                mBinding.cvDelivered.invisible()
                mBinding.cvOfd.invisible()
                mBinding.cvConversion.invisible()
                mBinding.btnRunsheets.invisible()
            } else {
                var ofd = 0f
                var delivered = 0f
                var conversion = 0f
                runsheets.forEach {
                    ofd += it.ofd
                    delivered += it.delivered
                }
                mBinding.tvOFDValue.text = String.format("%.0f", ofd)
                mBinding.tvDeliveredValue.text = String.format("%.0f", delivered)
                mBinding.pbConversion.progress = (delivered / ofd) * 100f
                mBinding.tvConversion.text = String.format("%.1f", ((delivered / ofd) * 100f)) + "%"
                mBinding.btnRunsheets.enable()

                mBinding.tvNoData.invisible()
                mBinding.cvDelivered.show()
                mBinding.cvOfd.show()
                mBinding.cvConversion.show()
                mBinding.btnRunsheets.show()
            }
        })
        obsMonthYear.observe(viewLifecycleOwner, Observer { monthYear ->
            tvDate.text = "${DateFormatSymbols().months[monthYear.month - 1]} ${monthYear.year}"
        })

        obsUser.observe(viewLifecycleOwner, Observer {
            Log.e("VIEW SETUP:::", obsUser.value?.profile_pic_url.toString())
            if (obsUser.value?.profile_pic_url.isNullOrBlank()) {
                mBinding.ivProfilePic.setImageResource(R.drawable.default_profile)
            } else {
                Glide.with(requireContext())
                        .load(obsUser.value?.profile_pic_url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mBinding.ivProfilePic)
            }
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
        checkProfilePic()
        super.onResume()
    }

    private fun checkProfilePic() {
        if (repoPrefs.isProfilePicUpdated()) {
            mViewModel.getLoggedInUser()
            repoPrefs.isProfilePicUpdated(false)
        }
    }
}