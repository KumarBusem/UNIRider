package com.uni.rider.common

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.uni.data.dataSources.definitions.DataSourceSharedPreferences
import com.uni.data.dataSources.repos.RepoSharedPreferences
import com.uni.rider.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class BaseFragment : Fragment() {

    private lateinit var mActivity: MainActivity
    private val mJob: Job by lazy { Job() }
    protected val ioScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.IO + mJob) }
    protected val uiScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Main + mJob) }

    protected val repoPrefs: DataSourceSharedPreferences by lazy { RepoSharedPreferences() }

    protected val TAG: String = javaClass.simpleName

    protected inline fun <T> LiveData<T>.startObserving(crossinline onChange: (T?) -> Unit) {
        this.observe(viewLifecycleOwner, Observer { onChange(it) })
    }

    protected fun navigateById(navigationId: Int) {
        findNavController().navigate(navigationId)
    }

    protected fun navigateBack() {
        findNavController().popBackStack()
    }

    protected fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    protected fun toggleBottomBarVisibility(shouldDisplay: Boolean) {
        mActivity.setBottomBarVisibility(shouldDisplay)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MainActivity
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mJob.isLazyInitialized) mJob.cancel()
    }
}