package com.example.mittise.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mittise.databinding.FragmentWeatherBinding
import com.example.mittise.ui.base.BaseFragment

class WeatherFragment : BaseFragment<FragmentWeatherBinding>() {
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentWeatherBinding.inflate(inflater, container, false)

    override fun setupViews() {
        // TODO: Initialize weather UI
    }

    override fun observeData() {
        // TODO: Observe weather data
    }
} 