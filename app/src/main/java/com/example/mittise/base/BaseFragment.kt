package com.example.mittise.base

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import com.example.mittise.util.Transition3D

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTransitions()
    }

    private fun setupTransitions() {
        enterTransition = Transition3D()
        exitTransition = Transition3D()
        reenterTransition = Transition3D()
        returnTransition = Transition3D()
    }

    protected fun animateView(view: View) {
        view.animate()
            .rotationY(360f)
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(300)
                    .start()
            }
            .start()
    }
} 