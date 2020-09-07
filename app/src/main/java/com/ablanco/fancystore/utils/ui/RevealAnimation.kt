package com.ablanco.fancystore.utils.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Point
import android.view.View
import android.view.ViewAnimationUtils
import androidx.annotation.IntDef
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import kotlin.math.hypot

/**
 * Created by Álvaro Blanco Cabrero on 05/09/2020.
 * FancyStore.
 */

@Retention(AnnotationRetention.SOURCE)
@IntDef
annotation class RevealOrigin

const val Center = 0
const val Left = 1 shl 0
const val Top = 1 shl 1
const val Right = 1 shl 2
const val Bottom = 1 shl 3

fun View.showReveal(@RevealOrigin origin: Int = Center) {
    val (cx, cy) = calculateCords(origin)
    val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
    val anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, 0f, finalRadius)
    visible()
    anim.start()
}

fun View.hideReveal(@RevealOrigin origin: Int = Center) {
    val (cx, cy) = calculateCords(origin)
    val initialRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
    val anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, initialRadius, 0f)
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            invisible()
        }
    })
    anim.start()
}

private fun View.calculateCords(@RevealOrigin origin: Int): Point {
    val cx = when {
        origin and Left != 0 -> 0
        origin and Right != 0 -> width
        else -> width / 2
    }
    val cy = when {
        origin and Top != 0 -> 0
        origin and Bottom != 0 -> height
        else -> height / 2
    }
    return Point(cx, cy)
}
