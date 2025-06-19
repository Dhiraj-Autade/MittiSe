package com.example.mittise.ui.schemes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mittise.databinding.FragmentSchemesBinding
import com.example.mittise.ui.base.BaseFragment

class SchemesFragment : BaseFragment<FragmentSchemesBinding>() {
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSchemesBinding.inflate(inflater, container, false)

    override fun setupViews() {
        // TODO: Initialize schemes UI
    }

    override fun observeData() {
        // TODO: Observe schemes data
    }
} 