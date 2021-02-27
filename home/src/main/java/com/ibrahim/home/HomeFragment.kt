package com.ibrahim.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.ibrahim.core.Failure
import com.ibrahim.core.exhaustive
import com.ibrahim.currencyconverter.di.AppDependencies
import com.ibrahim.currencyconverter.exchangerate.ExchangeRateData
import com.ibrahim.home.databinding.FragmentHomeBinding
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel by viewModels<HomeViewModel> { factory }

    private val adapter: ExchangeRatesAdapter by lazy {
        ExchangeRatesAdapter {
            val base =
                if (viewModel.state.value is HomeState.ExchangeRateSuccess) {
                    (viewModel.state.value as HomeState.ExchangeRateSuccess).exchangeRates.base
                } else {
                    ""
                }
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToExchangeRateFragment(
                    ExchangeRateData(
                        base = base,
                        target = it.name,
                        rate = it.rate
                    )
                )
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerHomeComponent.builder()
            .context(context)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    AppDependencies::class.java
                )
            ).build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRates.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rvRates.setHasFixedSize(true)
        binding.rvRates.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                render(state)
            }
        }

        if (viewModel.state.value !is HomeState.ExchangeRateSuccess) {
            lifecycleScope.launch {
                viewModel dispatch HomeIntent.GetLatestExchangeRate
            }
        }
    }

    private fun render(state: HomeState) {
        when (state) {
            is HomeState.Idle -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            is HomeState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            is HomeState.ExchangeRateSuccess -> {
                onLatestExchangeRateSuccess(state.exchangeRates)
                binding.progressBar.visibility = View.GONE
            }

            is HomeState.ExchangeRateFailure -> {
                onLatestExchangeRateFailure(state.failure)
                binding.progressBar.visibility = View.GONE
            }
        }.exhaustive
    }

    private fun onLatestExchangeRateSuccess(exchangeRates: ExchangeRates) {
        Timber.i("latestExchangeRate: ${exchangeRates.exchangeRates.size}")
        requireActivity().title = exchangeRates.base
        if (adapter.items.isNullOrEmpty()) {
            adapter.items.addAll(exchangeRates.exchangeRates)
            adapter.notifyDataSetChanged()
        }
    }

    private fun onLatestExchangeRateFailure(failure: Failure) {
        Timber.i("latestExchangeRateFailure: $failure")
        var errorMessage: String = getString(R.string.something_went_wrong_please_try_again_later)
        when (failure) {
            is Failure.NetworkConnection -> {
                errorMessage =
                    if (failure.throwable is SocketTimeoutException)
                        getString(R.string.looks_like_the_server_is_taking_too_long_to_respond_please_try_again_later)
                    else
                        getString(R.string.no_internet_connection)
            }
            is Failure.ServerError -> {
                errorMessage = getString(R.string.no_internet_connection)
            }
            is Failure.FeatureFailure -> {
            }
        }
        Toast.makeText(
            requireContext(),
            errorMessage,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("onDestroyView")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.i("onDetach")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy")
    }

}