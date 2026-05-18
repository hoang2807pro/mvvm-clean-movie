package com.fernandocejas.sample.features.language.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.extension.invisible
import com.fernandocejas.sample.core.extension.visible
import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.core.failure.Failure.NetworkConnection
import com.fernandocejas.sample.core.failure.Failure.ServerError
import com.fernandocejas.sample.core.platform.BaseFragment
import com.fernandocejas.sample.databinding.FragmentLanguageBinding
import com.fernandocejas.sample.features.language.failure.LanguageFailure
import com.fernandocejas.sample.features.language.interactor.LanguageModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LanguageFragment : BaseFragment() {

    private val viewModel: LanguageViewModel by inject()

    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is LanguageUiState.Loading -> showProgress()
                        is LanguageUiState.Success -> renderList(state.items)
                        is LanguageUiState.Error   -> handleFailure(state.failure)
                    }
                }
            }
        }
    }

    private fun renderList(items: List<LanguageModel>) {
        hideProgress()
        // TODO: bind items vào RecyclerView / views
    }

    private fun handleFailure(failure: Failure) {
        when (failure) {
            is NetworkConnection          -> renderFailure(R.string.failure_network_connection)
            is ServerError                -> renderFailure(R.string.failure_server_error)
            is LanguageFailure.ListEmpty    -> renderFailure(R.string.failure_server_error)
            else                          -> renderFailure(R.string.failure_server_error)
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        hideProgress()
        notifyWithAction(message, R.string.action_refresh) { viewModel.load() }
    }
}
