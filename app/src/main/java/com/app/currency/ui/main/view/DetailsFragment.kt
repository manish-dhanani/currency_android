package com.app.currency.ui.main.view

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.currency.R
import com.app.currency.data.util.Result
import com.app.currency.databinding.FragmentDetailsBinding
import com.app.currency.ui.main.view.adapter.HistoryDataAdapter
import com.app.currency.ui.main.view.adapter.PopularConversionAdapter
import com.app.currency.ui.main.viewmodel.MainViewModel
import com.app.currency.util.Constants
import com.app.currency.util.Extensions.showError
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    lateinit var viewModel: MainViewModel

    lateinit var base: String
    lateinit var target: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            DetailsFragmentArgs.fromBundle(it).let { args ->
                base = args.base
                target = args.target
            }
        }

        setViewModel()

        setObservers()

        initHistoricalData()

        initPopularConversions()
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
        viewModel.historicalDataResult.observe(viewLifecycleOwner) { result ->
            activity?.let { activity ->
                when (result.status) {
                    Result.Status.SUCCESS -> {
                        showHistoryDataContent(true)
                        showHistoryDataProgress(false)

                        result.data?.let { dataList ->
                            // Sort historical data in descending order by date.
                            binding.layoutHistoryData.rvHistoryData.adapter =
                                HistoryDataAdapter(dataList.sortedByDescending { it.date })
                        }
                    }
                    Result.Status.ERROR -> {
                        showHistoryDataContent(false)
                        showHistoryDataProgress(false)

                        activity.showError(
                            binding.root, result.message ?: getString(R.string.err_unknown)
                        )
                    }
                    Result.Status.LOADING -> {
                        showHistoryDataContent(false)
                        showHistoryDataProgress(true)
                    }
                }
            }
        }

        viewModel.popularConversionsResult.observe(viewLifecycleOwner) { result ->
            activity?.let { activity ->
                when (result.status) {
                    Result.Status.SUCCESS -> {
                        showPopularConversionsContent(true)
                        showPopularConversionsProgress(false)

                        result.data?.let { dataList ->
                            binding.layoutPopularConversions.rvPopularConversions.adapter =
                                PopularConversionAdapter(dataList)
                        }
                    }
                    Result.Status.ERROR -> {
                        showPopularConversionsContent(false)
                        showPopularConversionsProgress(false)

                        activity.showError(
                            binding.root, result.message ?: getString(R.string.err_unknown)
                        )
                    }
                    Result.Status.LOADING -> {
                        showPopularConversionsContent(false)
                        showPopularConversionsProgress(true)
                    }
                }
            }
        }
    }

    private fun initHistoricalData() {
        binding.layoutHistoryData.tvTitle.text =
            getString(R.string.title_history_data, base, target)

        val formatter = SimpleDateFormat(Constants.API_DATE_FORMAT, Locale.ENGLISH)

        val calendar = Calendar.getInstance()
        val endDate = formatter.format(calendar.time)

        calendar.add(Calendar.DAY_OF_YEAR, -2)
        val startDate = formatter.format(calendar.time)

        viewModel.getHistoricalData(base, target, startDate, endDate)
    }

    private fun initPopularConversions() {
        binding.layoutPopularConversions.tvTitle.text =
            getString(R.string.title_popular_conversions, base)

        // Array consists of 10 popular currencies other than base and target.
        val symbolsArray = Constants.popularCurrencies.filterNot { it == base || it == target }
            .subList(0, 10)
        val symbols = TextUtils.join(",", symbolsArray)

        viewModel.getPopularConversions(base, symbols)
    }

    private fun showHistoryDataContent(show: Boolean) {
        activity?.let { binding.layoutHistoryData.contentGroup.isVisible = show }
    }

    private fun showHistoryDataProgress(show: Boolean) {
        activity.let { binding.layoutHistoryData.progress.isVisible = show }
    }

    private fun showPopularConversionsContent(show: Boolean) {
        activity?.let { binding.layoutPopularConversions.contentGroup.isVisible = show }
    }

    private fun showPopularConversionsProgress(show: Boolean) {
        activity.let { binding.layoutPopularConversions.progress.isVisible = show }
    }

}