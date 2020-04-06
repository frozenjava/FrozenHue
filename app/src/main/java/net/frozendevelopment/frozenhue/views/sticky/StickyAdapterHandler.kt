package net.frozendevelopment.frozenhue.views.sticky

import android.view.View
import androidx.core.view.ViewCompat
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

internal class StickyAdapterHandler<T : GroupieViewHolder> : GroupAdapter<T>(), StickyHeaderHandler,
    StickyHeaderHandler.ViewSetup {

    override fun isStickyHeader(position: Int): Boolean {
        val item = getItem(position)
        return item is StickyHeaderType
    }

    override fun setupStickyHeaderView(stickyHeader: View) {
        ViewCompat.setElevation(stickyHeader, 10f)
    }

    override fun teardownStickyHeaderView(stickyHeader: View) {
        ViewCompat.setElevation(stickyHeader, 0f)
    }
}