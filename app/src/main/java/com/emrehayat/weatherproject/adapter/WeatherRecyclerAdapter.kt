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
                val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
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