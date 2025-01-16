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

    private fun getWeatherIconUrl(iconCode: String): String {
        return when (iconCode) {
            "01d" -> "https://cdn-icons-png.flaticon.com/512/6974/6974833.png" // güneşli (gündüz)
            "01n" -> "https://cdn-icons-png.flaticon.com/512/581/581601.png"   // açık (gece)
            "02d" -> "https://cdn-icons-png.flaticon.com/512/1163/1163661.png" // parçalı bulutlu (gündüz)
            "02n" -> "https://cdn-icons-png.flaticon.com/512/1163/1163661.png" // parçalı bulutlu (gece)
            "03d", "03n" -> "https://cdn-icons-png.flaticon.com/512/414/414927.png" // bulutlu
            "04d", "04n" -> "https://cdn-icons-png.flaticon.com/512/414/414927.png" // çok bulutlu
            "09d", "09n" -> "https://cdn-icons-png.flaticon.com/512/3351/3351979.png" // yağmurlu
            "10d", "10n" -> "https://cdn-icons-png.flaticon.com/512/3351/3351979.png" // sağanak yağışlı
            "11d", "11n" -> "https://cdn-icons-png.flaticon.com/512/1959/1959317.png" // fırtınalı
            "13d", "13n" -> "https://cdn-icons-png.flaticon.com/512/642/642102.png"   // karlı
            "50d", "50n" -> "https://cdn-icons-png.flaticon.com/512/4151/4151022.png" // sisli
            else -> "https://openweathermap.org/img/wn/${iconCode}@2x.png"
        }
    }

    fun observeLiveData() {
        viewModel.weatherLiveData.observe(viewLifecycleOwner) { weatherWithDetails ->
            weatherWithDetails?.let {
                binding.cityName.text = it.weatherFeatures.cityName
                binding.temperatureDetail.text = "Sıcaklık: %.1f°C".format(it.weatherFeatures.temperature)
                binding.humidityRate.text = "Nem: %${it.weatherFeatures.humidity}"
                binding.wind.text = "Rüzgar: %.1f m/s".format(it.weatherFeatures.windSpeed)
                
                // Yağış bilgisini kontrol et
                binding.rainfall.text = when {
                    it.weatherFeatures.rainfall != null && it.weatherFeatures.rainfall > 0 -> {
                        "Yağış: %.1f mm".format(it.weatherFeatures.rainfall)
                    }
                    it.weather.any { weather -> 
                        weather.main.contains("rain", ignoreCase = true) || 
                        weather.description.contains("yağmur", ignoreCase = true)
                    } -> {
                        "Yağışlı"
                    }
                    else -> {
                        "Yağış: Yok"
                    }
                }

                // Weather ikonunu yükle
                it.weatherFeatures.weatherIcon?.let { iconCode ->
                    val iconUrl = getWeatherIconUrl(iconCode)
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