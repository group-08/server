package ch.uzh.ifi.seal.soprafs20.game;

import java.util.HashMap;

public class City {

    HashMap<CityState, Integer> hashMapCity = new HashMap<>();

    public City(){
        hashMapCity.put(CityState.NEWYORK, 2459115);
        hashMapCity.put(CityState.LISBON, 742676);
        hashMapCity.put(CityState.ZURICH, 784794);
        hashMapCity.put(CityState.SANFRANCISCO, 2487956);
        hashMapCity.put(CityState.CARACAS, 395269);
        hashMapCity.put(CityState.LIMA, 418440);
        hashMapCity.put(CityState.NAIROBI, 1528488);
        hashMapCity.put(CityState.CASABLANCA, 1532755);
        hashMapCity.put(CityState.HANOI, 1236594);
        hashMapCity.put(CityState.DHAKA, 1915035);
        hashMapCity.put(CityState.TOKYO, 1118370);
        hashMapCity.put(CityState.JAKARTA, 1047378);
        hashMapCity.put(CityState.BRISBANE, 1100661);
        hashMapCity.put(CityState.PERTH, 1098081);
        hashMapCity.put(CityState.CHRISTCHURCH, 2348327);
        hashMapCity.put(CityState.DUBAI, 1940345);

    }

    public HashMap<CityState, Integer> getHashMapCity() {
        return hashMapCity;
    }
}
