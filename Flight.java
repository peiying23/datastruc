import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Flight {
    String flightID;
    public static final int maxSeats = 5;
    ArrayList<Passenger> confirmedTicketList;
    Queue<Passenger> waitingList;
    boolean vacancyStatus;
    private int confirmedSeats; 
    private int emptySeats;    

    public Flight(String flightID) {
        this.flightID = flightID;
        this.confirmedTicketList = new ArrayList<>();
        this.waitingList = new LinkedList<>();
        vacancyStatus = true;
        confirmedSeats = 0;
        emptySeats = maxSeats;
    }

    public boolean isFull() {
        return confirmedTicketList.size() >= maxSeats;
    }

    public void updateVacancyStatus() {
        vacancyStatus = !isFull();
    }

    public void bookFlight(Passenger passenger, Flight flight) {
        
    if (confirmedTicketList.contains(passenger)) {
        System.out.println("Passenger " + passenger.getName() + " has already booked this flight.");
        return;
    }
   
    if (waitingList.contains(passenger)) {
        System.out.println("Passenger " + passenger.getName() + " is already in the waiting list.");
        return;
    }       
        Ticket bookingTicket = new Ticket(passenger, flight);
        passenger.addTicketToBucket(bookingTicket);

        if (!isFull()) {
            bookingTicket.setStatus("confirmed");
            addConfirmedPassenger(passenger);
            System.out.println("Ticket confirmed for passenger: " + passenger.getName());
        } else {
            bookingTicket.setStatus("waiting list");
            addWaitlistedPassenger(passenger);
            System.out.println("The flight is fully booked. Passenger " + passenger.getName() + " added to the waiting list.");
        }

        updateVacancyStatus();
        Main.afs.saveFlightsToCsv();
    }

    public void addConfirmedPassenger(Passenger passenger) {
        confirmedTicketList.add(passenger);
        confirmedSeats++;
        emptySeats--;
    }

    public void addWaitlistedPassenger(Passenger passenger) {
        waitingList.add(passenger);
    }

    public void processWaitlist() {
        while (!isFull() && !waitingList.isEmpty()) {
            Passenger nextPassenger = waitingList.poll();
            addConfirmedPassenger(nextPassenger);
        }
    }

    public boolean cancelTicket(Passenger passenger){
        if(confirmedTicketList.remove(passenger)){
            confirmedSeats--;
            emptySeats++;
            System.out.println("Ticket canceled for passenger: " + passenger.getName());
            
        if(!waitingList.isEmpty()){
            Passenger nextPassenger = waitingList.poll();
            addConfirmedPassenger(nextPassenger);
            System.out.println("Passenger " + nextPassenger.getName() + "moved from waiting list to confirmed.");
        }    
        updateVacancyStatus();
        return true;      
        }else{
            System.out.println("Passenger not found in the confirmed list.");
            return false;
        }
    }
       
    public void setConfirmedSeats(int confirmedSeats) {
        if (confirmedSeats < 0 || confirmedSeats > maxSeats) {
            System.err.println("Invalid confirmed seats value. Adjusting to default.");
            confirmedSeats = Math.max(0, Math.min(confirmedSeats, maxSeats));
        }
        this.confirmedSeats = confirmedSeats;
        this.emptySeats = maxSeats - confirmedSeats; 
    }

    public void setEmptySeats(int emptySeats) {
        if (emptySeats < 0 || emptySeats > maxSeats) {
            throw new IllegalArgumentException("Invalid empty seats value.");
        }
        this.emptySeats = emptySeats;
        this.confirmedSeats = maxSeats - emptySeats; 
    }


    public int getConfirmedSeats() {
        return confirmedSeats;
    }

    public int getEmptySeats() {
        return emptySeats;
    }

    public int getWaitlistCount() {
        return waitingList.size();
    }

    @Override
    public String toString() {
        return "Flight ID: " + this.flightID +
                " Total seat available: " + this.maxSeats +
                " Seat booked: " + confirmedSeats +
                " Seat available: " + emptySeats +
                " Vacancy Status: " + vacancyStatus;
    }
}
