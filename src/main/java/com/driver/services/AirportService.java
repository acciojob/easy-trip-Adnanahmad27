package com.driver.services;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AirportService {
    @Autowired
    private AirportRepository airportRepository = new AirportRepository();

    public void addAirport(Airport airport) {
        airportRepository.addAirport(airport);
    }
    public String getLargestAirportName(){
        List<Airport> airportList = airportRepository.getAirportList();
        String ans = "";
        int terminal = 0;
        for(Airport airport : airportList){
            if(airport.getNoOfTerminals() == terminal){
                if(airport.getAirportName().compareTo(ans) < 0){
                    ans = airport.getAirportName();
                }
            } else if (airport.getNoOfTerminals() > terminal) {
                terminal = airport.getNoOfTerminals();
                ans = airport.getAirportName();
            }
        }
        return ans;
    }
    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        List<Flight> flightList = airportRepository.getFlightList();
        List<Flight> list = new ArrayList<>();
        for(Flight flight : flightList){
            if(flight.getFromCity()==fromCity && flight.getToCity()==toCity){
                list.add(flight);
            }
        }
        if(list.size()==0) return -1;
        return shortestDuration(list);
    }
    private double shortestDuration(List<Flight> list) {
        Double ans = list.get(0).getDuration();
        for(Flight flight : list){
            if(flight.getDuration() < ans){
                ans = flight.getDuration();
            }
        }
        return ans;
    }
    public int getNumberOfPeopleOn(Date date, String airportName) {
        Map<Integer,List<Passenger>> flightPassengerList = airportRepository.getFlightOfPassengerListMap();
        Map<Integer,Flight> flightMap = airportRepository.getFlightMap();
        Map<String,Airport> airportMap = airportRepository.getAirportMap();
        int cnt = 0;
        for(int flightId : flightPassengerList.keySet()){
            if(flightMap.get(flightId).getFlightDate().compareTo(date)==0){
                if(flightMap.get(flightId).getFromCity().equals(airportMap.get(airportName)) || flightMap.get(flightId).getToCity().equals(airportMap.get(airportName))){
                    cnt ++;
                }
            }
        }
        return cnt;
    }

    public void addFlight(Flight flight) {
        airportRepository.addFlight(flight);
    }
    public void addPassenger(Passenger passenger) {
        airportRepository.addPassenger(passenger);
    }
    public int calculateFlightFare(Integer flightId) {
        Map<Integer,List<Passenger>> flightPassengerList = airportRepository.getFlightOfPassengerListMap();
        int extra = flightPassengerList.get(flightId).size() * 50;
        return 3000+extra;
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        Map<Integer,List<Passenger>> flightPassengerList = airportRepository.getFlightOfPassengerListMap();
        Map<Integer,List<Flight>> passengerFlightList = airportRepository.getPassengerOfFlightListMap();
        List<Passenger> passengerList = flightPassengerList.getOrDefault(flightId,new ArrayList<>());
        List<Flight> flightList = passengerFlightList.getOrDefault(passengerId,new ArrayList<>());
        for(Flight flight : flightList){
            if(flight.getFlightId()==flightId) return "FAILURE";
        }
        if(airportRepository.getFlight(flightId).getMaxCapacity()<=passengerList.size()){
            return "FAILURE";
        }
        passengerList.add(airportRepository.getPassenger(passengerId));
        flightList.add(airportRepository.getFlight(flightId));
        flightPassengerList.put(flightId,passengerList);
        passengerFlightList.put(passengerId,flightList);
        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {

        if(airportRepository.getFlight(flightId)==null) return "FAILURE";
        if(airportRepository.getPassenger(passengerId)==null) return "FAILURE";

        Map<Integer,List<Passenger>> flightPassengerList = airportRepository.getFlightOfPassengerListMap();
        if(!flightPassengerList.containsKey(flightId)) return "FAILURE";
        Map<Integer,List<Flight>> passengerFlightList = airportRepository.getPassengerOfFlightListMap();
        if(!passengerFlightList.containsKey(passengerId)) return "FAILURE";
        List<Passenger> passengerList = flightPassengerList.get(flightId);
        if(!passengerList.contains(airportRepository.getPassenger(passengerId))) return "FAILURE";
        List<Flight> flightList = passengerFlightList.get(passengerId);
        if(!flightList.contains(airportRepository.getFlight(flightId))) return "FAILURE";

        passengerList.remove(airportRepository.getPassenger(passengerId));
        if(passengerList.size()==0) flightPassengerList.remove(flightId);
        else flightPassengerList.put(flightId,passengerList);
        flightList.remove(airportRepository.getFlight(flightId));
        if(flightList.size()==0) passengerFlightList.remove(passengerId);
        else passengerFlightList.put(passengerId,flightList);

        return "SUCCESS";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        Map<Integer , List<Flight>> passengerFlightList = airportRepository.getPassengerOfFlightListMap();
        if(passengerFlightList.containsKey(passengerId)) return passengerFlightList.get(passengerId).size();
        return 0;
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        Flight flight = airportRepository.getFlight(flightId);
        if(flight==null) return null;
        City city = flight.getFromCity();
        Map<String , Airport> airportMap = airportRepository.getAirportMap();
        for(Airport airport : airportMap.values()){
            if(airport.getCity()==city) return airport.getAirportName();
        }
        return null;
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        Map<Integer,List<Passenger>> flightPassengerList = airportRepository.getFlightOfPassengerListMap();
        List<Passenger> passengerList = flightPassengerList.getOrDefault(flightId,new ArrayList<>());
        if(passengerList.size()==0) return 0;
        int totalRevenue = 0;
        for(int i=0 ; i<passengerList.size() ; i++){
            totalRevenue += (3000+i*50);
        }
        return totalRevenue;
    }
}
