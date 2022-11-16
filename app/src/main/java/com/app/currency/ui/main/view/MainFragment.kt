package com.app.currency.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.currency.R
import com.app.currency.data.local.ConvertResult
import com.app.currency.data.local.Symbol
import com.app.currency.data.util.Result
import com.app.currency.databinding.FragmentMainBinding
import com.app.currency.ui.main.view.adapter.SymbolAdapter
import com.app.currency.ui.main.viewmodel.MainViewModel
import com.app.currency.util.Extensions.showError
import com.app.currency.util.Extensions.toast
import com.app.currency.util.NetworkConnection
import com.app.currency.util.PrefManager
import com.app.currency.util.getFormattedAmount
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var networkConnection: NetworkConnection

    @Inject
    lateinit var prefManager: PrefManager

    lateinit var viewModel: MainViewModel

    private val symbols: ArrayList<Symbol> = arrayListOf()
    private var symbolAdapter: SymbolAdapter? = null

    private var tvFromDropdown: AutoCompleteTextView? = null
    private var tvToDropdown: AutoCompleteTextView? = null
    private var edtFromAmount: EditText? = null
    private var edtToAmount: EditText? = null

    private var convertInfo: ConvertResult.Info? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val symbolsJson = JsonParser.parseString(prefManager.getSymbols()) as JsonObject
        val symbolsMap: MutableMap<String, JsonElement> = symbolsJson.asMap()

        if (symbolsMap.isEmpty()) {
            when {
                networkConnection.isActive() -> activity?.toast(getString(R.string.err_no_currency))
                else -> activity?.toast(getString(R.string.err_no_network))
            }

            activity?.finish()
            return
        }

        setViewModel()

        setObservers()

        bindViews()

        setListeners()

        init(symbolsMap)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setViewModel() {
        activity?.let { activity ->
            viewModel = ViewModelProvider(activity)[MainViewModel::class.java]
        }
    }

    private fun setObservers() {
        viewModel.convertResult.observe(viewLifecycleOwner) { result ->
            activity?.let { activity ->
                when (result.status) {
                    Result.Status.SUCCESS -> {
                        result.data?.let { convertResult ->
                            convertInfo = convertResult.info
                            edtToAmount?.setText(convertResult.amount.toString())

                            convertInfo?.rate?.let { enableAll() }
                        }
                    }
                    Result.Status.ERROR -> {
                        enableDropdown()

                        activity.showError(
                            binding.root, result.message ?: getString(R.string.err_unknown)
                        )
                    }
                    Result.Status.LOADING -> {
                        // Do nothing.
                    }
                }
            }
        }
    }

    private fun bindViews() {
        tvFromDropdown = binding.fromDropdown.editText as? AutoCompleteTextView
        tvToDropdown = binding.toDropdown.editText as? AutoCompleteTextView
        edtFromAmount = binding.tilFromAmount.editText
        edtToAmount = binding.tilToAmount.editText
    }

    private fun setListeners() {
        tvFromDropdown?.doOnTextChanged { _, _, _, _ -> onCurrencyChanged() }

        tvToDropdown?.doOnTextChanged { _, _, _, _ -> onCurrencyChanged() }

        edtFromAmount?.apply {
            doOnTextChanged { _, _, _, _ ->
                if (isEnabled && convertInfo?.rate != null) {
                    val toAmount = try {
                        when {
                            text.isNullOrEmpty() || text.toString() == "." -> ""
                            else -> getFormattedAmount(
                                text.toString().toDouble() * convertInfo?.rate!!
                            )
                        }
                    } catch (e: NumberFormatException) {
                        ""
                    }

                    if (edtToAmount?.isFocused == false && edtToAmount?.text.toString() != toAmount) {
                        edtToAmount?.setText(toAmount)
                    }
                }
            }
        }

        edtToAmount?.apply {
            doOnTextChanged { _, _, _, _ ->
                if (isEnabled && convertInfo?.rate != null) {
                    val fromAmount = try {
                        when {
                            text.isNullOrEmpty() || text.toString() == "." -> ""
                            else -> getFormattedAmount(
                                text.toString().toDouble() / convertInfo?.rate!!
                            )
                        }
                    } catch (e: NumberFormatException) {
                        ""
                    }

                    if (edtFromAmount?.isFocused == false && edtFromAmount?.text.toString() != fromAmount) {
                        edtFromAmount?.setText(fromAmount)
                    }
                }
            }
        }

        binding.ivSwap.setOnClickListener {
            // view.isEnabled = false
            val fromCode = tvFromDropdown?.text.toString()
            val toCode = tvToDropdown?.text.toString()

            // Clear the text.
            // This ensures convert API will be called once both code swap.
            tvFromDropdown?.setText("", false)
            tvToDropdown?.setText("", false)

            tvFromDropdown?.setText(toCode, false)
            tvToDropdown?.setText(fromCode, false)
        }

        binding.btnDetails.setOnClickListener { navigateToDetails() }
    }

    private fun init(symbolsMap: Map<String, JsonElement>) {
        activity?.let { activity ->
            disableAll()

            symbols.apply {
                symbolsMap.forEach { add(Symbol(it.key, it.value.asString)) }
            }
            symbolAdapter = SymbolAdapter(activity, symbols)

            // Bind symbols to fromDropdown and toDropdown.
            tvFromDropdown?.apply {
                setAdapter(symbolAdapter)
                // Set USD as default.
                symbols.firstOrNull { it.code == "USD" }?.let { setText(it.code, false) }
            }

            tvToDropdown?.apply {
                setAdapter(symbolAdapter)
                // Set EUR as default.
                symbols.firstOrNull { it.code == "EUR" }?.let { setText(it.code, false) }
            }
        }
    }

    private fun onCurrencyChanged() {
        activity?.let { activity ->
            disableAll()

            if (tvFromDropdown?.text?.isNotEmpty() == true && tvToDropdown?.text?.isNotEmpty() == true) {
                // Clear toAmount.
                edtToAmount?.setText("")

                if (edtFromAmount?.text?.isEmpty() == true) {
                    // Set 1 as default.
                    edtFromAmount?.setText("1")
                }

                when {
                    networkConnection.isActive() -> {
                        viewModel.convert(
                            tvFromDropdown?.text.toString(),
                            tvToDropdown?.text.toString(),
                            edtFromAmount?.text.toString().toDouble()
                        )
                    }
                    else -> {
                        activity.showError(binding.root, getString(R.string.err_no_network))
                    }
                }
            }
        }
    }

    private fun disableAll() {
        binding.apply {
            fromDropdown.isEnabled = false
            toDropdown.isEnabled = false
            tilFromAmount.isEnabled = false
            tilToAmount.isEnabled = false
            ivSwap.isEnabled = false
        }
    }

    private fun enableAll() {
        binding.apply {
            fromDropdown.isEnabled = true
            toDropdown.isEnabled = true
            tilFromAmount.isEnabled = true
            tilToAmount.isEnabled = true
            ivSwap.isEnabled = true
        }
    }

    private fun enableDropdown() {
        binding.apply {
            fromDropdown.isEnabled = true
            toDropdown.isEnabled = true
        }
    }

    private fun navigateToDetails() {
        if (tvFromDropdown?.text?.isNotEmpty() == true && tvToDropdown?.text?.isNotEmpty() == true) {
            val directions = MainFragmentDirections.actionDetails(
                base = tvFromDropdown?.text.toString(),
                target = tvToDropdown?.text.toString()
            )
            findNavController().navigate(directions)
        }
    }

}