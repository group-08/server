package ch.uzh.ifi.seal.soprafs20.game;

import java.util.HashMap;

public class Weather {

    HashMap<String, WeatherState> hashMapWeather = new HashMap<>();

    public Weather() {
        hashMapWeather.put("Snow", WeatherState.RAINY);
        hashMapWeather.put("Sleet", WeatherState.RAINY);
        hashMapWeather.put("Hail", WeatherState.RAINY);
        hashMapWeather.put("Thunderstorm", WeatherState.WINDY);
        hashMapWeather.put("Heavy Rain", WeatherState.RAINY);
        hashMapWeather.put("Light Rain", WeatherState.RAINY);
        hashMapWeather.put("Showers", WeatherState.RAINY);
        hashMapWeather.put("Heavy Cloud", WeatherState.WINDY);
        hashMapWeather.put("Light Cloud", WeatherState.SUNNY);
        hashMapWeather.put("Clear", WeatherState.SUNNY);
    }

    public HashMap<String, WeatherState> getHashMapWeather() {
        return hashMapWeather;
    }
}
