package com.fernandocejas.sample.features.language.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.fernandocejas.sample.core.platform.BaseActivity
import com.fernandocejas.sample.core.platform.BaseFragment

class LanguageActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, LanguageActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

    }

    override fun fragment(): BaseFragment = LanguageFragment()
}
