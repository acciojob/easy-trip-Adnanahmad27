package com.driver.repository;

import com.driver.model.Airport;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AirportRepository {

    private Map<String, Airport> airportMap = new HashMap<>();
    private Map<Integer, Passenger> passengerMap = new HashMap<>();
    private Map<Integer, Flight> flightMap = new HashMap<>();
    private Map<Integer, List<Passenger>> flightOfPassengerListMap = new HashMap<>();
    private Map<Integer,List<Flight>> passengerOfFlightListMap = new HashMap<>();

    public Map<String, Airport> getAirportMap() {
        return airportMap;
    }

    public Map<Integer, Passenger> getPassengerMap() {
        return passengerMap;
    }

    public Map<Integer, Flight> getFlightMap() {
        return flightMap;
    }

    public Map<Integer, List<Passenger>> getFlightOfPassengerListMap() {
        return flightOfPassengerListMap;
    }
    public void setFlightOfPassengerListMap(Map<Integer, List<Passenger>> flightOfpassengerListMap) {
        this.flightOfPassengerListMap = flightOfpassengerListMap;
    }
    public Map<Integer, List<Flight>> getPassengerOfFlightListMap() {
        return passengerOfFlightListMap;
    }
    public void setPassengerOfFlightListMap(Map<Integer, List<Flight>> passengerOfFlightListMap) {
        this.passengerOfFlightListMap = passengerOfFlightListMap;
    }

    public void addAirport(Airport airport) {
        String key = airport.getAirportName();
        airportMap.put(key,airport);
    }
    public void addFlight(Flight flight){
        Integer key = flight.getFlightId();
        flightMap.put(key,flight);
    }
    public void addPassenger(Passenger passenger){
        Integer key = passenger.getPassengerId();
        passengerMap.put(key,passenger);
    }
    public Flight getFlight(int flightId){
        return flightMap.get(flightId);
    }
    public Passenger getPassenger(int passengerId){
        return passengerMap.get(passengerId);
    }
    public List<Airport> getAirportList(){
        List<Airport> list = new ArrayList<>();
        for (Airport airport : airportMap.values()) {
            list.add(airport);
        }
        return list;
    }
    public List<Flight> getFlightList(){
        List<Flight> list = new ArrayList<>();
        for (Flight flight : flightMap.values()) {
            list.add(flight);
        }
        return list;
    }
}
