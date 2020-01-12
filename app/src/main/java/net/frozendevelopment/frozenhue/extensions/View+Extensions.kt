package net.frozendevelopment.frozenhue.extensions

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation

fun View.navigateOnClick(
    navigationAction: Int,
    args: Bundle? = null,
    canNavigate: (() -> Boolean) = { true }
) {
    this.setOnClickListener {
        if (!canNavigate()) return@setOnClickListener
        Navigation.findNavController(this).navigate(navigationAction, args)
    }
}
