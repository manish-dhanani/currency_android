package com.app.currency.ui.launch.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.currency.databinding.FragmentSplashBinding
import com.app.currency.ui.launch.viewmodel.SplashViewModel
import com.app.currency.ui.main.MainActivity

class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private var _binding: FragmentSplashBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

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
        viewModel.isSplashPlayFinished.observe(viewLifecycleOwner) {
            if (it) {
                startMainActivity()
            }
        }
    }

    private fun init() {
        // Play splash animation
        viewModel.playSplash()
    }

    private fun startMainActivity() {
        activity?.let { launchActivity ->
            startActivity(Intent(launchActivity, MainActivity::class.java))
            launchActivity.finish()
        }
    }

}