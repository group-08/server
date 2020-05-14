package ch.uzh.ifi.seal.soprafs20.game;

import java.util.HashMap;

public class Weather {

    HashMap<String, WeatherState> hashMapWeather = new HashMap<String, WeatherState>();

    public Weather() {
        hashMapWeather.put("freezing_rain_heavy", WeatherState.RAINY);
        hashMapWeather.put("freezing_rain", WeatherState.RAINY);
        hashMapWeather.put("freezing_rain_light", WeatherState.RAINY);
        hashMapWeather.put("freezing_drizzle", WeatherState.RAINY);
        hashMapWeather.put("ice_pellets_heavy", WeatherState.RAINY);
        hashMapWeather.put("ice_pellets", WeatherState.RAINY);
        hashMapWeather.put("ice_pellets_light", WeatherState.RAINY);
        hashMapWeather.put("snow_heavy", WeatherState.RAINY);
        hashMapWeather.put("snow", WeatherState.RAINY);
        hashMapWeather.put("snow_light", WeatherState.RAINY);
        hashMapWeather.put("flurries", WeatherState.WINDY);
        hashMapWeather.put("tstorm", WeatherState.WINDY);
        hashMapWeather.put("rain", WeatherState.RAINY);
        hashMapWeather.put("rain_heavy", WeatherState.RAINY);
        hashMapWeather.put("rain_light", WeatherState.RAINY);
        hashMapWeather.put("drizzle", WeatherState.RAINY);
        hashMapWeather.put("fog_light", WeatherState.SUNNY);
        hashMapWeather.put("fog", WeatherState.SUNNY);
        hashMapWeather.put("cloudy", WeatherState.SUNNY);
        hashMapWeather.put("mostly_cloudy", WeatherState.SUNNY);
        hashMapWeather.put("partly_cloudy", WeatherState.SUNNY);
        hashMapWeather.put("mostly_clear", WeatherState.SUNNY);
        hashMapWeather.put("clear", WeatherState.SUNNY);
    }

    public HashMap<String, WeatherState> getHashMapWeather() {
        return hashMapWeather;
    }
}
