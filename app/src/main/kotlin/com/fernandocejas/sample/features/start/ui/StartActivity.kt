package com.fernandocejas.sample.features.start.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.fernandocejas.sample.core.platform.BaseActivity
import com.fernandocejas.sample.core.platform.BaseFragment

class StartActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, StartActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

    }

    override fun fragment(): BaseFragment = StartFragment()
}
