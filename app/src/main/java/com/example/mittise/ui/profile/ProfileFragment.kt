package com.example.mittise.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mittise.databinding.FragmentProfileBinding
import com.example.mittise.ui.base.BaseFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater, container, false)

    override fun setupViews() {
        // TODO: Initialize profile UI
    }

    override fun observeData() {
        // TODO: Observe profile data
    }
} 