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
        viewModel.weatherLiveData.observe(viewLifecycleOwner) { weather ->
            weather?.let {
                binding.cityName.text = it.weatherFeatures.cityName
                binding.temperatureDetail.text = "Sıcaklık: ${it.weatherFeatures.temperature} °C"
                binding.humidityRate.text = "Nem: ${it.weatherFeatures.humidity}%"
                binding.wind.text = "Rüzgar: ${it.weatherFeatures.windSpeed} m/s"
                binding.rainfall.text = if (it.weatherFeatures.rainfall != null) {
                    "Yağış: ${it.weatherFeatures.rainfall} mm"
                } else {
                    "Yağış: Bilgi yok"
                }
                val iconUrl = "https://openweathermap.org/img/wn/${it.weatherFeatures.weatherIcon}.png"
                binding.weatherImageDetail.downloadImage(iconUrl, makePlaceholder(requireContext()))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}