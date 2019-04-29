package com.spevec.bitfinex.client.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spevec.bitfinex.client.R
import com.spevec.bitfinex.client.routing.Router
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ContainerActivity : AppCompatActivity() {

    private val router: Router by inject(parameters = { parametersOf(this) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        savedInstanceState ?: router.showMainFragment()
    }

    private fun getLayout(): Int = R.layout.activity_main
}
