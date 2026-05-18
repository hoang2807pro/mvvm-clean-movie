package com.fernandocejas.sample.features.start.failure

import com.fernandocejas.sample.core.failure.Failure

sealed class StartFailure : Failure.FeatureFailure() {
    object NotFound  : StartFailure()
    object ListEmpty : StartFailure()
}
