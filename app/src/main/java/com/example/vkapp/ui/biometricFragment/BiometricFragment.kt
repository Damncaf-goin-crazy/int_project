package com.example.vkapp.ui.biometricFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vkapp.SharedPreferencesHelper
import com.example.vkapp.WebSiteConfig
import com.example.vkapp.databinding.FragmentBiometricBinding


class BiometricFragment : Fragment() {

    private lateinit var binding: FragmentBiometricBinding

    private val sharedPreferences by lazy {
        SharedPreferencesHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBiometricBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(): Unit = with(binding) {
        if (sharedPreferences.getPassword() != null) {
            input.hint = "Put password"
            tvContinue.setOnClickListener {
                val str = etPassword.text.toString()
                if (str == sharedPreferences.getPassword()) {
                    findNavController().navigate(BiometricFragmentDirections.biometricToRecycler())
                } else {
                    //TODO bad password
                }
            }
            authenticate()
        } else {
            input.hint = "Create password"
            tvContinue.setOnClickListener {
                val str = etPassword.text.toString()
                if (str.length >= 6) {
                    sharedPreferences.setPassword(str)
                    findNavController().navigate(BiometricFragmentDirections.biometricToRecycler())
                } else {
                    //TODO bad password
                }
            }
        }
    }


    private fun authenticate() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val prompt = createBiometricPrompt()
                prompt.authenticate(createPromptInfo())
            }

            else -> Unit
        }
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                findNavController().navigate(BiometricFragmentDirections.biometricToRecycler())
            }
        }

        val executor = ContextCompat.getMainExecutor(requireContext())
        return BiometricPrompt(this, executor, callback)
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate with biometrics")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setConfirmationRequired(false)
            .setNegativeButtonText("Login with password")
            .build()

}