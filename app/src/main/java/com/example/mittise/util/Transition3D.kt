package com.example.mittise.util

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.transition.Transition
import androidx.transition.TransitionValues

class Transition3D : Transition() {

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private fun captureValues(transitionValues: TransitionValues) {
        val view = transitionValues.view
        transitionValues.values["rotation"] = view.rotation
        transitionValues.values["scale"] = view.scaleX
        transitionValues.values["alpha"] = view.alpha
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        if (startValues == null || endValues == null) return null

        val view = endValues.view
        val startRotation = startValues.values["rotation"] as Float
        val endRotation = endValues.values["rotation"] as Float
        val startScale = startValues.values["scale"] as Float
        val endScale = endValues.values["scale"] as Float
        val startAlpha = startValues.values["alpha"] as Float
        val endAlpha = endValues.values["alpha"] as Float

        val rotationAnimator = ObjectAnimator.ofFloat(view, "rotation", startRotation, endRotation)
        val scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", startScale, endScale)
        val scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", startScale, endScale)
        val alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", startAlpha, endAlpha)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(rotationAnimator, scaleXAnimator, scaleYAnimator, alphaAnimator)
        animatorSet.duration = 500
        animatorSet.interpolator = AccelerateDecelerateInterpolator()

        return animatorSet
    }
} 