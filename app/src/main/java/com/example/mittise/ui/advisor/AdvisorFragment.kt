package com.example.mittise.ui.advisor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mittise.databinding.FragmentAdvisorBinding
import com.example.mittise.ui.base.BaseFragment

class AdvisorFragment : BaseFragment<FragmentAdvisorBinding>() {
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAdvisorBinding.inflate(inflater, container, false)

    override fun setupViews() {
        // TODO: Initialize advisor UI
    }

    override fun observeData() {
        // TODO: Observe advisor data
    }
} 