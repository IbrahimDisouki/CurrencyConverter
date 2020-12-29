package com.ibrahim.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.ibrahim.core.Failure
import com.ibrahim.core.exhaustive
import com.ibrahim.currencyconverter.di.AppDependencies
import com.ibrahim.currencyconverter.di.DFMSavedStateViewModelFactory
import com.ibrahim.currencyconverter.exchangerate.ExchangeRateData
import com.ibrahim.home.databinding.FragmentHomeBinding
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var savedStateViewModelFactory: DFMSavedStateViewModelFactory

    private val viewModel by viewModels<HomeViewModel> { savedStateViewModelFactory }

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
        DaggerHomeComponent.factory()
            .homeComponent(
                this,
                EntryPointAccessors.fromApplication(
                    requireContext().applicationContext,
                    AppDependencies::class.java
                )
            ).inject(this)
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
        adapter.items.addAll(exchangeRates.exchangeRates)
        adapter.notifyDataSetChanged()
    }

    private fun onLatestExchangeRateFailure(failure: Failure) {
        Timber.i("latestExchangeRateFailure: $failure")
        Toast.makeText(
            requireContext(),
            failure.localizedMessage ?: "Something went wrong, please try again later!",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel.state.value !is HomeState.ExchangeRateSuccess) {
            lifecycleScope.launch {
                viewModel dispatch HomeIntent.GetLatestExchangeRate
            }
        }
    }

}