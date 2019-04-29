package com.spevec.bitfinex.client.routing

import android.app.Activity
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.spevec.bitfinex.client.R
import com.spevec.bitfinex.client.ui.MainFragment
import com.spevec.bitfinex.client.util.inTransaction

@IdRes
private const val MAIN_CONTAINER_ID = R.id.container_activity_container

class RouterImpl(
    private val activity: Activity,
    private val fragmentManager: FragmentManager
) : Router {

    override fun showMainFragment() {
        fragmentManager.inTransaction { replace(MAIN_CONTAINER_ID, MainFragment.newInstance(), MainFragment.TAG) }
    }
}