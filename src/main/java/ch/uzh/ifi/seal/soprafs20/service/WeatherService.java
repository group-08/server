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


@Service
@Transactional
public class WeatherService {

    Weather weather = new Weather();
    City city = new City();
    HashMap<String, WeatherState> weatherHashMap= weather.getHashMapWeather();


    public String getAPIResponse(String lat, String lon) throws IOException {

        String urlBuild = String.format("https://api.climacell.co/v3/" +
                "weather/realtime?lat=%s&lon=%s&unit_system=si&fields=weather_code&apikey=mbskomA6aqcmzuv4EMgg7ANB2OEyoOGi", lat, lon);

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

        String[] words = response.split(" ");
        for(String word : words){
            if(weatherHashMap.containsKey(word)){
                return weatherHashMap.get(word);
            }
        }
        return null;
    }

    public CityState randomCityChooser(){
        List<CityState> cities = new ArrayList<>(city.getHashMapCity().keySet());
        Random r = new Random();
        int i = r.nextInt(cities.size());
        return cities.get(i);
    }

    public void updateWeather (Game game){
        CityState randomCity = randomCityChooser();
        double[] coordinates = city.getHashMapCity().get(randomCity);
        String coordinatesString = Arrays.toString(coordinates);
        String lat = Arrays.asList(coordinatesString.split(",")).get(0);
        String lon = Arrays.asList(coordinatesString.split(",")).get(1);
        WeatherState weatherState;
        try {
            String response = getAPIResponse(lat, lon);
             weatherState = getWeather(response);
        }
        catch (IOException e) {
            weatherState = WeatherState.UNKNOWN;
        }
        game.setWeatherState(weatherState);
    }
}
