package com.uni.rider.features.salary

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.uni.rider.R
import com.uni.rider.common.BaseAbstractFragment
import com.uni.rider.common.ViewModelFactory
import com.uni.rider.common.hide
import com.uni.rider.common.show
import com.uni.rider.databinding.FragmentSalaryBinding

class SalaryFragment : BaseAbstractFragment<SalaryViewModel, FragmentSalaryBinding>(R.layout.fragment_salary) {

    override fun setViewModel(): SalaryViewModel =
            ViewModelProvider(this@SalaryFragment, ViewModelFactory {
                SalaryViewModel(requireActivity().application)
            }).get(SalaryViewModel::class.java)

    private val mUserAdapter: SalaryListAdapter by lazy { SalaryListAdapter() }

    override fun setupViews(): FragmentSalaryBinding.() -> Unit = {
        toggleBottomBarVisibility(true)

        toggleBottomBarVisibility(true)
        rvSalaryList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mUserAdapter
        }
        srlSalary.setOnRefreshListener {
            mBinding.srlSalary.isRefreshing = false
            mViewModel.getSalaryList()
        }
    }

    override fun setupObservers(): SalaryViewModel.() -> Unit = {

        obsSalaryList.observe(viewLifecycleOwner, Observer { salaryList ->
            if (salaryList.isNullOrEmpty()) {
                mUserAdapter.submitList(emptyList())
                mBinding.logoSendfast.show()
            } else {
                mUserAdapter.submitList(salaryList.toMutableList())
                mBinding.logoSendfast.hide()
            }
        })
    }

    override fun onResume() {
        mViewModel.getSalaryList()
        super.onResume()
    }

}
