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
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.content.Context

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

        binding.showAllButton.setOnClickListener {
            binding.searchEditText.text?.clear()
            viewModel.refreshDataFromInternet()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.weatherLoading.visibility = View.VISIBLE
            binding.weatherErrorMessage.visibility = View.GONE
            binding.weatherRecyclerView.visibility = View.GONE
            binding.searchEditText.text?.clear()
            viewModel.refreshDataFromInternet()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.searchEditText.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val cityName = textView.text.toString()
                if (cityName.isNotEmpty()) {
                    viewModel.searchCity(cityName)
                    // Klavyeyi gizle
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(textView.windowToken, 0)
                    true
                } else {
                    false
                }
            } else {
                false
            }
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