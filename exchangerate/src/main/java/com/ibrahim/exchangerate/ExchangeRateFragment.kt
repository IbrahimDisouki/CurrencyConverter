package com.ibrahim.exchangerate

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ibrahim.currencyconverter.exchangerate.ExchangeRateData
import com.ibrahim.exchangerate.databinding.FragmentExchangeRateBinding

class ExchangeRateFragment : Fragment() {

    private val args: ExchangeRateFragmentArgs by navArgs()
    private val exchangeRateData: ExchangeRateData by lazy {
        args.exchangeRateData
    }

    private var _binding: FragmentExchangeRateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentExchangeRateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(exchangeRateData) {
            binding.tvBaseName.text = base
            binding.etBaseValue.setText(amount.toString())
            binding.tvTargetValue.text = rate.toString()
            binding.tvTargetName.text = target
        }

        binding.etBaseValue.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(
                textView: TextView?,
                actionId: Int,
                event: KeyEvent?
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.tvTargetValue.text =
                        (binding.etBaseValue.text.toString()
                            .toDouble() * exchangeRateData.rate).toString()
                }
                return false
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}