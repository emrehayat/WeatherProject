import androidx.room.Embedded
import androidx.room.Relation
import com.emrehayat.weatherproject.model.Weather
import com.emrehayat.weatherproject.model.WeatherFeatures

data class WeatherWithDetails(
    @Embedded val weatherFeatures: WeatherFeatures,
    @Relation(
        parentColumn = "id",
        entityColumn = "weatherFeaturesId"
    )
    val weather: List<Weather>
) 