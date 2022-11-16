package com.app.currency.ui.launch.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.currency.data.util.Result
import com.app.currency.databinding.FragmentSplashBinding
import com.app.currency.ui.launch.viewmodel.SplashViewModel
import com.app.currency.ui.main.MainActivity
import com.app.currency.util.DataState
import com.app.currency.util.NetworkConnection
import com.app.currency.util.PrefManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private var _binding: FragmentSplashBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var networkConnection: NetworkConnection

    @Inject
    lateinit var prefManager: PrefManager

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()

        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setObservers() {
        viewModel.symbolsResult.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    viewModel.symbolsDataState = DataState.FETCHED

                    result.data?.let {
                        // Save symbols json object as string in prefManager.
                        prefManager.setSymbols(it)
                    }
                }
                Result.Status.ERROR -> {
                    viewModel.symbolsDataState = DataState.FAILED
                }
                Result.Status.LOADING -> {
                    viewModel.symbolsDataState = DataState.FETCHING
                }
            }
        }

        viewModel.isSplashPlayFinished.observe(viewLifecycleOwner) {
            if (it) startMainActivity()
        }
    }

    private fun init() {
        activity?.let {
            // Get symbols.
            if (networkConnection.isActive()) viewModel.getSymbols()

            // Play splash animation.
            viewModel.playSplash(SplashViewModel.SPLASH_PLAY_DURATION)
        }
    }

    private fun startMainActivity() {
        activity?.let { launchActivity ->
            startActivity(Intent(launchActivity, MainActivity::class.java))
            launchActivity.finish()
        }
    }

}