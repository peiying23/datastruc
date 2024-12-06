import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnualFlightScheduler {
    static HashMap<LocalDate, ArrayList<Flight>> flightsByDate;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String FILE_NAME = "C:\\Users\\Hacker\\Documents\\NetBeansProjects\\FlightBookingSystem\\src\\flightbookingsystem\\flights.csv"; 

    public AnnualFlightScheduler() {
        flightsByDate = new HashMap<>();
        loadFlightsFromCsv(); 
    }

    private void loadFlightsFromCsv() {
    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
        String line;
        reader.readLine(); // Skip the header line

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");

            if (parts.length >= 8) {
                String flightID = parts[0];
                LocalDate date;
                String confirmedPassengersStr = parts[3];
                String waitlistedPassengersStr = parts[4];

                try {
                    date = LocalDate.parse(parts[1]);
                } catch (Exception e) {
                    System.err.println("Invalid date format in CSV: " + parts[1]);
                    continue; 
                }

                int confirmedSeats = 0;
                int emptySeats = Flight.maxSeats;

                try {
                    confirmedSeats = Integer.parseInt(parts[5]);
                    emptySeats = Integer.parseInt(parts[6]);

                    if (confirmedSeats + emptySeats != Flight.maxSeats || emptySeats < 0) {
                        System.err.println("Seat data inconsistency detected in flight: " + flightID);
                        confirmedSeats = Math.min(confirmedSeats, Flight.maxSeats);
                        emptySeats = Flight.maxSeats - confirmedSeats;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse seat numbers for flight: " + flightID);
                    confirmedSeats = 0;
                    emptySeats = Flight.maxSeats; 
                }

                Flight flight = new Flight(flightID);
                try {
                    flight.setConfirmedSeats(confirmedSeats);
                    flight.setEmptySeats(emptySeats);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid seat values for flight: " + flightID + ". Skipping this flight.");
                    continue; 
                }

                List<Passenger> confirmedPassengersList = parsePassengersList(confirmedPassengersStr);
                List<Passenger> waitlistedPassengersList = parsePassengersList(waitlistedPassengersStr);

                flight.confirmedTicketList.addAll(confirmedPassengersList);
                flight.waitingList.addAll(waitlistedPassengersList);

                flightsByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(flight);
            } else {
                System.err.println("CSV format error: insufficient fields in line: " + line);
            }
        }
    } catch (IOException e) {
        System.err.println("Failed to read CSV file: " + e.getMessage());
    }
}

private List<Passenger> parsePassengersList(String passengersStr) {
    List<Passenger> passengerList = new ArrayList<>();
    if (passengersStr != null && !passengersStr.trim().isEmpty()) {
        String[] passengers = passengersStr.split(";");
        for (String passengerStr : passengers) {
            passengerStr = passengerStr.trim();
            if (!passengerStr.isEmpty()) {
                int startIndex = passengerStr.indexOf("(");
                int endIndex = passengerStr.indexOf(")");

                if (startIndex > 0 && endIndex > startIndex) {
                    String name = passengerStr.substring(0, startIndex).trim();
                    String passportNum = passengerStr.substring(startIndex + 1, endIndex).trim();
                    Passenger passenger = new Passenger(passportNum, name);
                    passengerList.add(passenger);
                }
            }
        }
    }
    return passengerList;
}


    

    private void parsePassengers(Flight flight, String passengersStr, boolean isConfirmed) {
        if (!passengersStr.isEmpty()) {
            String[] passengers = passengersStr.split(",");
            for (String passengerStr : passengers) {
                String[] passengerInfo = passengerStr.split("\\(");
                String name = passengerInfo[0];
                String passportNum = passengerInfo[1].substring(0, passengerInfo[1].length() - 1);
                Passenger passenger = new Passenger(passportNum, name);
                if (isConfirmed) {
                    flight.addConfirmedPassenger(passenger);
                } else {
                    flight.addWaitlistedPassenger(passenger);
                }
            }
        }
    }

    public static void searchFlights(LocalDate date1, LocalDate date2) {
        System.out.println("\nFlight for the Weeks: ");
        System.out.println();
        while (!date1.isAfter(date2)) {
            ArrayList<Flight> temp = flightsByDate.get(date1);
            if (temp != null) { 
                for (int i = 0; i < temp.size(); i++) {
                    System.out.println(temp.get(i));
                }
            }
            System.out.println("-------------------------------------------------------------------------");
            date1 = date1.plusDays(1);
        }
    }

    public void saveFlightsToCsv() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write("Flight ID,Date,Status,Confirmed Passengers," +
                    "Waitlisted Passengers,Confirmed Seats,Empty Seats,Waitlist Count\n");
    
            for (LocalDate date : flightsByDate.keySet()) {
                for (Flight flight : flightsByDate.get(date)) {
                    String flightID = flight.flightID; 
    
                    String confirmedPassengersStr = getPassengerListString(flight.confirmedTicketList);
                    String waitlistedPassengersStr = getPassengerListString(flight.waitingList);
    
                    writer.write(flightID + "," + date + ",Available," +
                            confirmedPassengersStr + "," + waitlistedPassengersStr + "," +
                            flight.getConfirmedSeats() + "," + flight.getEmptySeats() + "," +
                            flight.getWaitlistCount() + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to write CSV file: " + e.getMessage());
        }
    }

    private String getPassengerListString(ArrayList<Passenger> passengers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < passengers.size(); i++) {
            Passenger passenger = passengers.get(i);
            sb.append(passenger.getName()).append("(").append(passenger.getPassportNum()).append(")");
            if (i < passengers.size() - 1) {
                sb.append(";");  
            }
        }
        return sb.toString();
    }
    

    private Passenger findPassengerInCsv(String passportNum) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            reader.readLine(); 
            while ((line = reader.readLine()) != null) {
                System.out.println("Checking line: " + line); 
                String[] parts = line.split(",");  
                
                if (parts.length < 8) { 
                    continue; 
                }
    
                String flightID = parts[0];
                String flightDate = parts[1];
                String status = parts[2];
                String confirmedPassengersStr = parts[3]; 
                String waitlistedPassengersStr = parts[4]; 
                int confirmedSeats = Integer.parseInt(parts[5]);
                int emptySeats = Integer.parseInt(parts[6]);
    
                Flight flight = new Flight(flightID);
                flight.setConfirmedSeats(confirmedSeats);
                flight.setEmptySeats(emptySeats);
    
                List<Passenger> matchingPassengers = new ArrayList<>(); 

                if (!confirmedPassengersStr.isEmpty()) {
                    String[] confirmedPassengers = confirmedPassengersStr.split(";");
                    for (String confirmedPassenger : confirmedPassengers) {
                        Passenger passenger = findPassengerInList(new String[]{confirmedPassenger}, passportNum);
                        if (passenger != null) {
                            flight.addConfirmedPassenger(passenger); 
                            Ticket ticket = findTicket(flight, passenger, "confirmed");
                            passenger.addTicketToBucket(ticket);
                            matchingPassengers.add(passenger); 
                        }
                    }
                }

                if (!matchingPassengers.isEmpty()) {
                    System.out.println("Matching passengers: " + matchingPassengers);
                    return matchingPassengers.get(0); 
}
    
                if (!waitlistedPassengersStr.isEmpty()) {
                    String[] waitlistedPassengers = waitlistedPassengersStr.split(";");
                    for (String waitlistedPassenger : waitlistedPassengers) {
                        Passenger passenger = findPassengerInList(new String[]{waitlistedPassenger}, passportNum);
                        if (passenger != null) {
                            flight.addWaitlistedPassenger(passenger); 
                            Ticket ticket = findTicket(flight, passenger, "waiting list");
                            passenger.addTicketToBucket(ticket);
                            return passenger; 
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read CSV file: " + e.getMessage());
        }
        return null; 
    }
    
    
    
    
    private Passenger findPassengerInList(String[] passengers, String passportNum) {
        if (passengers != null) {
            Pattern pattern = Pattern.compile("([\\w\\u4e00-\\u9fa5]+)\\((\\d+)\\)"); 
    
            for (String passengerStr : passengers) {
                passengerStr = passengerStr.trim(); 
                Matcher matcher = pattern.matcher(passengerStr);
                if (matcher.find()) {
                    String name = matcher.group(1); 
                    String passport = matcher.group(2); 
    
                    System.out.println("Checking passenger: " + name + " with passport: " + passport); 
    
                    if (passport.equals(passportNum)) {
                        return new Passenger(passport, name); 
                    }
                }
            }
        }
        return null; 
    }
    
    

    
    
    private Ticket findTicket(Flight flight, Passenger passenger, String status) {   
        Ticket ticket = new Ticket(passenger, flight);
        ticket.setStatus(status);  
        return ticket;  
    }
    
    
    

    public Passenger getPassengerInfo(String passportNum) {
        for (LocalDate date : flightsByDate.keySet()) {
        for (Flight flight : flightsByDate.get(date)) {
            for (Passenger p : flight.confirmedTicketList) {
                if (p.getPassportNum().equals(passportNum)) {
                    return p;
                }
            }
            for (Passenger p : flight.waitingList) {
                if (p.getPassportNum().equals(passportNum)) {
                    return p;
                }
            }
        }
    }
    return null;
    }

    

    private String getPassengerListString(Queue<Passenger> passengers) {
        StringBuilder sb = new StringBuilder();
        ArrayList<Passenger> passengerList = new ArrayList<>(passengers); 
        for (int i = 0; i < passengerList.size(); i++) {
            Passenger passenger = passengerList.get(i);
            sb.append(passenger.getName()).append("(").append(passenger.getPassportNum()).append(")");
            if (i < passengerList.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
   
    public static Flight processFlightID(String flightID) {
        String[] parts = flightID.split("-");
        int flightIndex = Integer.parseInt(parts[1]);
        String dateString = parts[2] + "-" + parts[3] + "-" + parts[4];
        LocalDate date = LocalDate.parse(dateString, formatter);

        return flightsByDate.get(date).get(flightIndex);
    }
}