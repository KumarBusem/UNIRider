package com.uni.rider.features.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.uni.rider.R
import com.uni.rider.common.BaseAbstractFragment
import com.uni.rider.common.ViewModelFactory
import com.uni.rider.databinding.FragmentLoginBinding

class LoginFragment : BaseAbstractFragment<LoginViewModel, FragmentLoginBinding>(R.layout.fragment_login) {

    override fun setViewModel(): LoginViewModel =
            ViewModelProvider(this@LoginFragment, ViewModelFactory {
                LoginViewModel(requireActivity().application)
            }).get(LoginViewModel::class.java)

    override fun setupViews(): FragmentLoginBinding.() -> Unit = {

        toggleBottomBarVisibility(false)
        btnLogin.setOnClickListener {
            if (etUserName.text.toString().isEmpty() || etPassword.text.toString().isEmpty()) {
                requireActivity().toast("Please Enter Username & Password")
            } else {
                viewModel?.loginUser(etUserName.text.toString().trim(), etPassword.text.toString().trim())
            }
        }
    }

    override fun setupObservers(): LoginViewModel.() -> Unit = {

        obsIsUserAuthenticated.observe(viewLifecycleOwner, Observer { user ->

            navigateToHome()

        })

        obsMessage.observe(viewLifecycleOwner, Observer {
            if (!(it.isNullOrEmpty())) {
                showToast(it)
            }
        })
    }

    private fun navigateToHome() {
        navigateById(R.id.action_loginFragment_to_homeFragment)
    }

}
