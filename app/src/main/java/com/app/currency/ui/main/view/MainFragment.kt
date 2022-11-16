package com.app.currency.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.app.currency.R
import com.app.currency.data.local.Symbol
import com.app.currency.databinding.FragmentMainBinding
import com.app.currency.ui.main.view.adapter.SymbolAdapter
import com.app.currency.util.Extensions.toast
import com.app.currency.util.NetworkConnection
import com.app.currency.util.PrefManager
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var networkConnection: NetworkConnection

    @Inject
    lateinit var prefManager: PrefManager

    private val symbols: ArrayList<Symbol> = arrayListOf()
    private var symbolAdapter: SymbolAdapter? = null

    private var fromDropdown: AutoCompleteTextView? = null
    private var toDropdown: AutoCompleteTextView? = null

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

        init(symbolsMap)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init(symbolsMap: Map<String, JsonElement>) {
        activity?.let { activity ->
            symbols.apply {
                symbolsMap.forEach { add(Symbol(it.key, it.value.asString)) }
            }
            symbolAdapter = SymbolAdapter(activity, symbols)

            // Bind symbols to fromDropdown and toDropdown.
            fromDropdown = binding.fromDropdown.editText as? AutoCompleteTextView
            fromDropdown?.apply {
                setAdapter(symbolAdapter)
                // Set USD as default.
                symbols.firstOrNull { it.code == "USD" }?.let { setText(it.code) }
            }

            toDropdown = binding.toDropdown.editText as? AutoCompleteTextView
            toDropdown?.apply {
                setAdapter(symbolAdapter)
                // Set EUR as default.
                symbols.firstOrNull { it.code == "EUR" }?.let { setText(it.code) }
            }
        }
    }

}