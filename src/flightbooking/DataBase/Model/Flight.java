/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightbooking.DataBase.Model;

import java.time.LocalDateTime;

public class Flight {

    private int flightID;

    public Flight(String flightNumber, String origin, String destination) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
    }
    private String airline;
    private String flightNumber;
    private String origin;
    private String destination;
    private String flightStatus;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    public Flight(int flightID, String airline, String flightNumber, String origin, String destination, String flightStatus, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.flightID = flightID;
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.flightStatus = flightStatus;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public int getFlightID() {
        return flightID;
    }

    public String getAirline() {
        return airline;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getFlightStatus() {
        return flightStatus;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
}

