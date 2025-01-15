package com.emrehayat.weatherproject.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.emrehayat.weatherproject.databinding.FragmentWeatherDetailBinding
import com.emrehayat.weatherproject.util.downloadImage
import com.emrehayat.weatherproject.util.makePlaceholder
import com.emrehayat.weatherproject.viewmodel.WeatherDetailViewModel
import com.emrehayat.weatherproject.viewmodel.WeatherListViewModel

class WeatherDetailFragment : Fragment() {

    private var _binding: FragmentWeatherDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WeatherDetailViewModel
    var weatherId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            weatherId = WeatherDetailFragmentArgs.fromBundle(it).weatherId
        }
        viewModel = ViewModelProvider(this)[WeatherDetailViewModel::class.java]
        viewModel.getRoomData(weatherId)
        observeLiveData()
    }

    fun observeLiveData() {
        viewModel.weatherLiveData.observe(viewLifecycleOwner) { weatherWithDetails ->
            weatherWithDetails?.let {
                binding.cityName.text = it.weatherFeatures.cityName
                binding.temperatureDetail.text = "Sıcaklık: %.1f°C".format(it.weatherFeatures.temperature)
                binding.humidityRate.text = "Nem: %${it.weatherFeatures.humidity}"
                binding.wind.text = "Rüzgar: %.1f m/s".format(it.weatherFeatures.windSpeed)
                binding.rainfall.text = if (it.weatherFeatures.rainfall != null && it.weatherFeatures.rainfall > 0) {
                    "Yağış: %.1f mm".format(it.weatherFeatures.rainfall)
                } else {
                    "Yağış: Yok"
                }

                // Weather ikonunu yükle
                it.weatherFeatures.weatherIcon?.let { iconCode ->
                    val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
                    binding.weatherImageDetail.downloadImage(iconUrl, makePlaceholder(requireContext()))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}