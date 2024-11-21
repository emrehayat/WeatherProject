package com.emrehayat.weatherproject.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emrehayat.weatherproject.adapter.WeatherRecyclerAdapter
import com.emrehayat.weatherproject.databinding.FragmentWeatherListBinding
import com.emrehayat.weatherproject.viewmodel.WeatherListViewModel

class WeatherListFragment : Fragment() {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WeatherListViewModel
    private val weatherRecyclerAdapter = WeatherRecyclerAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[WeatherListViewModel::class.java]
        viewModel.refreshData()

        binding.weatherRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.weatherRecyclerView.adapter = weatherRecyclerAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.weatherLoading.visibility = View.VISIBLE
            binding.weatherErrorMessage.visibility = View.GONE
            binding.weatherRecyclerView.visibility = View.GONE
            viewModel.refreshDataFromInternet()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
    }

    fun observeLiveData() {
        viewModel.weatherValues.observe(viewLifecycleOwner) {
            weatherRecyclerAdapter.updateWeatherList(it)
            binding.weatherRecyclerView.visibility = View.VISIBLE
        }

        viewModel.weatherErrorMessage.observe(viewLifecycleOwner) {
            if (it) {
                binding.weatherErrorMessage.visibility = View.VISIBLE
                binding.weatherRecyclerView.visibility = View.GONE
            } else {
                binding.weatherErrorMessage.visibility = View.GONE
            }
        }

        viewModel.weatherLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.weatherRecyclerView.visibility = View.GONE
                binding.weatherErrorMessage.visibility = View.GONE
                binding.weatherLoading.visibility = View.VISIBLE
            } else {
                binding.weatherLoading.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}