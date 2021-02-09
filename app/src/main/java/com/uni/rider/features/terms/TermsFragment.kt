package com.uni.rider.features.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uni.rider.R
import com.uni.rider.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_terms.*


class TermsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        toggleBottomBarVisibility(false)
        return inflater.inflate(R.layout.fragment_terms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accepted.setOnClickListener {
            navigateBack()
            var user = repoPrefs.getLoggedInUser()
            user?.self_declaration = true
            repoPrefs.saveLoggedInUser(user!!)
        }
    }


}
