package com.uni.rider.features.help

import androidx.lifecycle.ViewModelProvider
import com.uni.rider.R
import com.uni.rider.common.BaseAbstractFragment
import com.uni.rider.common.ViewModelFactory
import com.uni.rider.databinding.FragmentHelpBinding

class HelpFragment : BaseAbstractFragment<HelpViewModel, FragmentHelpBinding>(R.layout.fragment_help) {

    override fun setViewModel(): HelpViewModel =
            ViewModelProvider(this@HelpFragment, ViewModelFactory {
                HelpViewModel(requireActivity().application)
            }).get(HelpViewModel::class.java)

    override fun setupViews(): FragmentHelpBinding.() -> Unit = {
        toggleBottomBarVisibility(false)

    }

    override fun setupObservers(): HelpViewModel.() -> Unit = {


    }

}
