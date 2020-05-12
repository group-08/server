package ch.uzh.ifi.seal.soprafs20.game;

import java.util.ArrayList;
import java.util.HashMap;

public class City {

    HashMap<CityState, double[]> hashMapCity = new HashMap<CityState, double[]>();

    public City(){
        hashMapCity.put(CityState.NEWYORK, new double[]{40.7,-74.0});
        hashMapCity.put(CityState.LISBON, new double[]{38.7,-9.1});
        hashMapCity.put(CityState.CAPEVERDE, new double[]{16.5,-23});
        hashMapCity.put(CityState.ZURICH, new double[]{47.3,8.5});
        hashMapCity.put(CityState.TULUM, new double[]{20.2,-87.4});
        hashMapCity.put(CityState.SANFRANCISCO, new double[]{37.7,-122.4});
        hashMapCity.put(CityState.RIO, new double[]{-22.9,-43.1});
        hashMapCity.put(CityState.MACHUPICCHU, new double[]{-13.1,-72.5});
        hashMapCity.put(CityState.KILIMANJARO, new double[]{-3.0,37.3});
        hashMapCity.put(CityState.CASABLANCA, new double[]{33.5,-7.5});
        hashMapCity.put(CityState.HANOI, new double[]{21.0,105.0});
        hashMapCity.put(CityState.LAHASA, new double[]{29.6,91.1});
        hashMapCity.put(CityState.TOKYO, new double[]{35.6,139.6});
        hashMapCity.put(CityState.BALI, new double[]{-8.3,115.0});
        hashMapCity.put(CityState.CAIRNS, new double[]{-16.9,145.7});
        hashMapCity.put(CityState.PERTH, new double[]{-31.9,115.8});
        hashMapCity.put(CityState.CHRISTCHURCH, new double[]{-43.5,172.6});

    }

    public HashMap<CityState, double[]> getHashMapCity() {
        return hashMapCity;
    }
}
