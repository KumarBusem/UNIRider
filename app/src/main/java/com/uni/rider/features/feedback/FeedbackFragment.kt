package com.uni.rider.features.feedback

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.uni.rider.R
import com.uni.rider.common.*
import com.uni.rider.databinding.FragmentFeedbackBinding
import com.uni.rider.features.dialogs.FeedbackDialog
import kotlinx.android.synthetic.main.fragment_runsheets.*
import java.util.*

class FeedbackFragment : BaseAbstractFragment<FeedbackViewModel, FragmentFeedbackBinding>(R.layout.fragment_feedback) {

    override fun setViewModel(): FeedbackViewModel =
            ViewModelProvider(this@FeedbackFragment, ViewModelFactory {
                FeedbackViewModel(requireActivity().application)
            }).get(FeedbackViewModel::class.java)

    private val mUserAdapter: FeedbackListAdapter by lazy { FeedbackListAdapter() }
    private val mPermissionManager: PermissionManager by lazy { PermissionManager(this@FeedbackFragment) }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val feedbackDialog: FeedbackDialog by lazy {
        FeedbackDialog(
                onSubmitFeedbackCLicked = { orderId: String, name: String, phone: String ->
                    getLocation { location ->
                        Log.e("DETAILS:::", "$orderId, $name, $phone, $location")
                        mViewModel.saveFeedback(orderId, name, phone, location)
                    }
                })
    }

    override fun setupViews(): FragmentFeedbackBinding.() -> Unit = {
        toggleBottomBarVisibility(true)
        rvFeedbacksList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mUserAdapter
        }
        srlFeedbacks.setOnRefreshListener {
            mBinding.srlFeedbacks.isRefreshing = false
            mViewModel.getFeedbackList()
        }
        mBinding.btnAddFeedback.setOnClickListener {
            initLoation()
            if (mPermissionManager.areAllPermissionsGranted())
                feedbackDialog.show(childFragmentManager, FeedbackFragment::class.java.simpleName)
            else
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mPermissionManager.requestAllPermissions()
                }
        }
    }

    override fun setupObservers(): FeedbackViewModel.() -> Unit = {

        obsFeedbackList.observe(viewLifecycleOwner, Observer { runsheets ->
            if (runsheets.isNullOrEmpty()) {
                mUserAdapter.submitList(emptyList())
                mBinding.logoSendfast.show()
            } else {
                mUserAdapter.submitList(runsheets.toMutableList())
                mBinding.logoSendfast.hide()
            }
        })
        obsIsDetailsSubmitted.observe(viewLifecycleOwner, Observer { it ->
            if (it == true) {
                if (feedbackDialog.isVisible) {
                    feedbackDialog.dismiss()
                    mViewModel.getFeedbackList()
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun getLocation(res: (Location?) -> Unit) {
        LocationServices.getFusedLocationProviderClient(requireContext()).lastLocation
                .addOnSuccessListener { location: Location? ->
                    res(location)
                }.addOnFailureListener {
                    res(null)
                }
    }

    override fun onResume() {
        mViewModel.getFeedbackList()
        super.onResume()
    }

    @SuppressLint("MissingPermission")
    fun initLoation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        var mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        var mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {

                        Log.e("LOCAATION:::", "$location")
                    }
                }
            }
        }
        LocationServices.getFusedLocationProviderClient(requireContext())
                .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
    }
}