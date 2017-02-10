package gr.gandg.george.gairtickets;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by george on 1/2/2017.
 */

public class Itinerary implements Serializable {
    public ArrayList<Flight> outbound = new ArrayList<Flight>();
    public ArrayList<Flight> inbound = new ArrayList<Flight>();

    public double totalPrice;
    public double pricePerAdult;
    public double pricePerChild;
    public double pricePerInfant;
    public boolean refundable;
    public boolean changePenalties;
    public String allAirlines = "";

    public String detailView() {
        StringBuilder txt = new StringBuilder("");
        Itinerary it = this;
        txt.append("Αναχώρηση: \n");
        for (int j=0; j<it.outbound.size(); j++) {
            Flight f = it.outbound.get(j);
            txt.append(f.departsAt);
            txt.append(" ");
            txt.append(f.originAirport);
            txt.append("->");
            txt.append(f.arrivesAt);
            txt.append(" ");
            txt.append(f.destinationAirport);
            txt.append(" \n");
            txt.append("Marketing Airline: =" + f.marketingAirline + "=\n");
            if (allAirlines.indexOf("," + f.marketingAirline + ",")==0) {
                allAirlines += "," + f.marketingAirline + ",";
            }
            txt.append("Operationg Airline: =" + f.operatingAirline + "=\n");
            if (allAirlines.indexOf(",=" + f.operatingAirline + "=,")==0) {
                allAirlines += "," + f.operatingAirline + ",";
            }
            txt.append("Flight No: " + f.flightNumber + " - Aircraft: " + f.aircraft +  "\n");
            txt.append("Travel Class: " + f.travelClass + "\n");
            txt.append("Booking code: " + f.bookingCode + "  - Seats remaining: " + f.seatsRemaining + "\n");

        }
        if (it.inbound.size()>0) {
            txt.append("\n\nΕπιστροφή: \n");
            for (int j = 0; j < it.inbound.size(); j++) {
                Flight f = it.inbound.get(j);
                txt.append(f.departsAt);
                txt.append(" ");
                txt.append(f.originAirport);
                txt.append("->");
                txt.append(f.arrivesAt);
                txt.append(" ");
                txt.append(f.destinationAirport);
                txt.append(" ");
                txt.append(f.marketingAirline + "\n");
                txt.append("Marketing Airline: =" + f.marketingAirline + "=\n");
                if (allAirlines.indexOf("," + f.marketingAirline + ",")==0) {
                    allAirlines += "," + f.marketingAirline + ",";
                }
                txt.append("Operationg Airline: =" + f.operatingAirline + "=\n");
                if (allAirlines.indexOf("," + f.operatingAirline + ",")==0) {
                    allAirlines += "," + f.operatingAirline + ",";
                }
                txt.append("Flight No: " + f.flightNumber + " - Aircraft: " + f.aircraft +  "\n");
                txt.append("Travel Class: " + f.travelClass + "\n");
                txt.append("Booking code: " + f.bookingCode + "  - Seats remaining: " + f.seatsRemaining + "\n");

            }
        }
        txt.append("\n\nΤιμή: ");
        txt.append(it.totalPrice);
        txt.append("\n");
        txt.append("Επιστροφή χρημάτων: " + (refundable ? "ναι" : "οχι") + "\n");
        txt.append("Χρέωση αλλαγής: " + (changePenalties ? "ναι" : "οχι") + "\n");

        allAirlines = allAirlines.replaceAll(",,,,,", ",");
        allAirlines = allAirlines.replaceAll(",,,,", ",");
        allAirlines = allAirlines.replaceAll(",,,", ",");
        allAirlines = allAirlines.replaceAll(",,", ",");

        return txt.toString();
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder("");
        Itinerary it = this;
        txt.append("Αναχώρηση: \n");
        for (int j=0; j<it.outbound.size(); j++) {
            Flight f = it.outbound.get(j);
            txt.append(f.departsAt);
            txt.append(" ");
            txt.append(f.originAirport);
            txt.append("->");
            txt.append(f.arrivesAt);
            txt.append(" ");
            txt.append(f.destinationAirport);
            txt.append(" ");
            txt.append(f.marketingAirline + "\n");
        }
        if (it.inbound.size()>0) {
            txt.append("Επιστροφή: \n");
            for (int j = 0; j < it.inbound.size(); j++) {
                Flight f = it.inbound.get(j);
                txt.append(f.departsAt);
                txt.append(" ");
                txt.append(f.originAirport);
                txt.append("->");
                txt.append(f.arrivesAt);
                txt.append(" ");
                txt.append(f.destinationAirport);
                txt.append(" ");
                txt.append(f.marketingAirline + "\n");

            }
        }
        txt.append("Τιμή: ");
        txt.append(it.totalPrice);
        txt.append("\n");


        return txt.toString();
    }
}
