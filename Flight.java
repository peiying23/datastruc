import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Date;
public class Flight {

    String flightID;
    final int maxSeats=5;
    ArrayList<Passenger>confirmedTicketList;
    Queue<Passenger>waitingList;
    boolean vacancyStatus;

    public Flight(String fightID){
        this.flightID=fightID;
        this.confirmedTicketList=new ArrayList<>();
        this.waitingList=new LinkedList<>();
        vacancyStatus=true;
    }

    public boolean isFull(){
        if(confirmedTicketList.size()<maxSeats){
            return false;
        }else{
            vacancyStatus=false;
            return  true;
        }
    }

    public void bookFlight(Passenger passenger, Flight flight){
        Ticket bookingTicket=new Ticket(passenger,flight);
        passenger.addTicketToBucket(bookingTicket);

        if(!isFull()){
            bookingTicket.setStatus("confirmed");
            confirmedTicketList.add(passenger);
            System.out.println("Your have successfully booked the flight.");
            isFull();//to update the status everytime after a seat is booked.
        }else{
            bookingTicket.setStatus("waiting list");
            System.out.println("The flight has been fully booked. You are in the waiting list now.");
            waitingList.add(passenger);
        }
     }

     @Override
     public String toString(){
        return "Flight ID: "+this.flightID
                +" Total seat available: "+this.maxSeats
                +" Seat booked: "+this.confirmedTicketList.size()
                +" Seat available: "+(this.maxSeats-this.confirmedTicketList.size())
                +" Vacancy Status: "+this.vacancyStatus;

     }



}
