package com.fernandocejas.sample.features.start.ui

import android.content.Intent
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
import com.fernandocejas.sample.databinding.FragmentStartBinding
import com.fernandocejas.sample.features.home.ui.HomeActivity
import com.fernandocejas.sample.features.language.ui.LanguageActivity

import com.fernandocejas.sample.features.start.failure.StartFailure
import com.fernandocejas.sample.features.start.interactor.StartModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class StartFragment : BaseFragment() {

    private val viewModel: StartViewModel by inject()

    private var _binding: FragmentStartBinding? = null
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
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideProgress()
//        viewModel.load()
        binding.btnStart.setOnClickListener {
            startActivity(
                Intent(requireContext(), HomeActivity::class.java)
            )
        }
        binding.btnLanguage.setOnClickListener {
            startActivity(
                Intent(requireContext(), LanguageActivity::class.java)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.uiState.collect { state ->
//                    when (state) {
//                        is StartUiState.Loading -> showProgress()
//                        is StartUiState.Success -> renderList(state.items)
//                        is StartUiState.Error   -> handleFailure(state.failure)
//                    }
//                }
//            }
//        }
    }

    private fun renderList(items: List<StartModel>) {
        hideProgress()
        // TODO: bind items vào RecyclerView / views
    }

    private fun handleFailure(failure: Failure) {
        when (failure) {
            is NetworkConnection          -> renderFailure(R.string.failure_network_connection)
            is ServerError                -> renderFailure(R.string.failure_server_error)
            is StartFailure.ListEmpty    -> renderFailure(R.string.failure_server_error)
            else                          -> renderFailure(R.string.failure_server_error)
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        hideProgress()
        notifyWithAction(message, R.string.action_refresh) { viewModel.load() }
    }
}
