package com.spevec.bitfinex.client.util

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) = beginTransaction().func().commit()

inline fun FragmentManager.inTransactionAndAddToBackStack(func: FragmentTransaction.() -> FragmentTransaction) =
    beginTransaction().func().addToBackStack(null).commit()

fun FragmentManager.removeFragmentIfItExists(fragmentTag: String) {
    findFragmentByTag(fragmentTag)?.let {
        beginTransaction()
            .remove(it)
            .commit()
    }
}

inline fun FragmentManager.clearBackStackAndInTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    beginTransaction()
        .func()
        .commit()
}