package com.fernandocejas.sample.features.home.ui

import android.content.Context
import android.content.Intent
import com.fernandocejas.sample.core.platform.BaseActivity
import com.fernandocejas.sample.core.platform.BaseFragment

class HomeActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    override fun fragment(): BaseFragment = HomeFragment()
}
