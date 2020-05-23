package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.game.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.json.*;


@Service
@Transactional
public class WeatherService {

    Weather weather = new Weather();
    City city = new City();
    HashMap<String, WeatherState> weatherHashMap= weather.getHashMapWeather();
    Random random = new Random();

    public String getAPIResponse(int woeid) throws IOException {

        String urlBuild = String.format("https://www.metaweather.com/api/location/%d/", woeid);

        URL url = new URL(urlBuild);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");

        int status = con.getResponseCode();

        Reader streamReader = null;

        if (status>299){
            streamReader = new InputStreamReader(con.getErrorStream());
        }
        else{
            streamReader = new InputStreamReader(con.getInputStream());
        }
        BufferedReader in = new BufferedReader(
               streamReader);
        String inputLine;
        StringBuilder content = new StringBuilder();
        while((inputLine = in.readLine()) != null){
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        String response = content.toString();

        return response;
    }

    public WeatherState getWeather(String response){
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray array = (JSONArray) obj.get("consolidated_weather");
            JSONObject today_weather = (JSONObject) array.get(1);
            String weather_state_name = (String) today_weather.get("weather_state_name");
            if(weatherHashMap.containsKey(weather_state_name)){
                return weatherHashMap.get(weather_state_name);
            }
        }
        catch (Exception e) {
        }
        return null;
    }

    public CityState randomCityChooser(){
        List<CityState> cities = new ArrayList<>(city.getHashMapCity().keySet());
        int i = random.nextInt(cities.size());
        return cities.get(i);
    }

    public void updateWeather (Game game){
        CityState randomCity = randomCityChooser();
        game.setCity(randomCity);
        int woeid = city.getHashMapCity().get(randomCity);

        WeatherState weatherState;
        try {
            String response = getAPIResponse(woeid);
            weatherState = getWeather(response);
        }
        catch (IOException e) {
            weatherState = WeatherState.UNKNOWN;
        }
        game.setWeatherState(weatherState);
    }
}
