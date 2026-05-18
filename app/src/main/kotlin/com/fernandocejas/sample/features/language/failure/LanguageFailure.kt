package com.fernandocejas.sample.features.language.failure

import com.fernandocejas.sample.core.failure.Failure

sealed class LanguageFailure : Failure.FeatureFailure() {
    object NotFound  : LanguageFailure()
    object ListEmpty : LanguageFailure()
}
