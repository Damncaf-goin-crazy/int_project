package com.example.vkapp.ui.editFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.vkapp.SharedPreferencesHelper
import com.example.vkapp.WebSiteConfig
import com.example.vkapp.databinding.FragmentEditBinding
import com.google.gson.Gson
import java.util.UUID

class EditFragment : Fragment() {
    private lateinit var binding: FragmentEditBinding
    private val navArgs: EditFragmentArgs by navArgs()

    private val sharedPreferencesHelper by lazy {
        SharedPreferencesHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun init(): Unit = with(binding) {
        val gson = Gson()
        val webConfig = navArgs.cell?.let { gson.fromJson(it, WebSiteConfig::class.java) }

        if (webConfig != null) {
            etEmail.setText(webConfig.login)
            etPassword.setText(webConfig.password)
            etWeb.setText(webConfig.webSite)
            etEmail.setText(webConfig.login)
        }
        btnSave.setOnClickListener {
            if (webConfig == null) {
                sharedPreferencesHelper.addWebSiteSettings(
                    WebSiteConfig(
                        id = UUID.randomUUID(),
                        webSite = etWeb.text.toString(),
                        login = etEmail.text.toString(),
                        password = etPassword.text.toString(),
                        thumbnail = "https://www." + etWeb.text.toString() + "/favicon.ico",
                    )
                )
            } else {
                sharedPreferencesHelper.updateWebSiteConfig(
                    WebSiteConfig(
                        id = webConfig.id,
                        webSite = etWeb.text.toString(),
                        login = etEmail.text.toString(),
                        password = etPassword.text.toString(),
                        thumbnail = "https://www." + etWeb.text.toString() + "/favicon.ico",
                    )
                )
            }
            findNavController().popBackStack()

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

    }
}