package com.example.vkapp.ui.recyclerFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkapp.MyAdapter
import com.example.vkapp.SharedPreferencesHelper
import com.example.vkapp.databinding.FragmentRecyclerBinding
import com.google.gson.Gson
import kotlinx.coroutines.launch

class RecyclerFragment : Fragment() {
    private lateinit var binding: FragmentRecyclerBinding
    private val sharedPreferencesHelper by lazy {
        SharedPreferencesHelper(requireContext())
    }

    private val rvAdapter: MyAdapter by lazy {
        MyAdapter { cell ->
            val gson = Gson()
            val cellJson = gson.toJson(cell)
            val action = RecyclerFragmentDirections.actionRecyclerFragmentToEditFragment(cellJson)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTodos.apply {
            val myLayoutManager = LinearLayoutManager(context)
            layoutManager = myLayoutManager
            adapter = rvAdapter
        }
        binding.fabAddToDo.setOnClickListener {
            findNavController().navigate(
                RecyclerFragmentDirections.actionRecyclerFragmentToEditFragment(
                    null
                )
            )
        }
        lifecycleScope.launch {
            sharedPreferencesHelper.getListFlow().collect {
                rvAdapter.submitList(it)
            }
        }

    }


}