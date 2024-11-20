package com.emrehayat.weatherproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.emrehayat.weatherproject.databinding.WeatherRecyclerRowBinding
import com.emrehayat.weatherproject.model.Weather
import com.emrehayat.weatherproject.model.WeatherFeatures
import com.emrehayat.weatherproject.util.downloadImage
import com.emrehayat.weatherproject.util.makePlaceholder
import com.emrehayat.weatherproject.view.WeatherListFragmentDirections

class WeatherRecyclerAdapter(val weatherList: ArrayList<WeatherFeatures>) : RecyclerView.Adapter<WeatherRecyclerAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(var view: WeatherRecyclerRowBinding) : RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding: WeatherRecyclerRowBinding = WeatherRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    fun updateWeatherList(newWeatherList: List<WeatherFeatures>) {
        weatherList.clear()
        weatherList.addAll(newWeatherList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.view.locationName.text = weatherList[position].lat.toString()
        holder.view.temperature.text = weatherList[position].current.temp.toString()

        holder.itemView.setOnClickListener {
            if (weatherList[position].current.weather.isNotEmpty()) {
                val action = WeatherListFragmentDirections.actionWeatherListFragmentToWeatherDetailFragment(weatherList[position].current.weather[0].uuid)
                Navigation.findNavController(it).navigate(action)
            }
        }

        holder.view.weatherImage.downloadImage(weatherList[position].current.weather[0].icon, makePlaceholder(holder.itemView.context))
    }
}