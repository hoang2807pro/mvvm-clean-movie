package com.fernandocejas.sample.features.home.failure

import com.fernandocejas.sample.core.failure.Failure

sealed class HomeFailure : Failure.FeatureFailure() {
    object NotFound  : HomeFailure()
    object ListEmpty : HomeFailure()
}
