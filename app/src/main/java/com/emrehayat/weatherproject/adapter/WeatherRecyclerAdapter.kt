package com.emrehayat.weatherproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.emrehayat.weatherproject.databinding.WeatherRecyclerRowBinding
import com.emrehayat.weatherproject.model.WeatherFeatures
import com.emrehayat.weatherproject.util.downloadImage
import com.emrehayat.weatherproject.util.makePlaceholder
import com.emrehayat.weatherproject.view.WeatherListFragmentDirections

class WeatherRecyclerAdapter(val weatherList: ArrayList<WeatherFeatures>) : 
    RecyclerView.Adapter<WeatherRecyclerAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(var view: WeatherRecyclerRowBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = WeatherRecyclerRowBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return WeatherViewHolder(binding)
    }

    override fun getItemCount(): Int = weatherList.size

    fun updateWeatherList(newWeatherList: List<WeatherFeatures>) {
        weatherList.clear()
        weatherList.addAll(newWeatherList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = weatherList[position]
        holder.view.apply {
            locationName.text = weather.cityName
            temperature.text = "%.1f°C".format(weather.temperature)
            weatherType.text = "Nem: %${weather.humidity}"

            // Hava durumu ikonunu yükle
            weather.weatherIcon?.let { iconCode ->
                // Özel ikon URL'leri
                val iconUrl = when (iconCode) {
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
                weatherImage.downloadImage(iconUrl, makePlaceholder(holder.itemView.context))
            }
        }

        holder.itemView.setOnClickListener {
            val action = WeatherListFragmentDirections
                .actionWeatherListFragmentToWeatherDetailFragment(weather.id)
            Navigation.findNavController(it).navigate(action)
        }
    }
}