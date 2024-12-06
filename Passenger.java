import java.util.ArrayList;
import java.util.HashMap;
import java.lang.String;

public class Passenger {

    private String passportNum;
    private String name;
    private ArrayList<Ticket>ticketsBucket;

    public Passenger(String passportNum, String name){
        this.passportNum=passportNum;
        this.name=name;
        ticketsBucket=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPassportNum() {
        return passportNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassportNum(String passportNum) {
        this.passportNum = passportNum;
    }

    public void addTicketToBucket(Ticket ticket) {
        if (ticketsBucket.size() < ticket.getFlight().maxSeats) { 
            ticketsBucket.add(ticket);
        } else {
            System.err.println("Exceeded the maximum number of tickets!");
        }
    }

    public void viewTicketStatus(){

        if (ticketsBucket.isEmpty()) {
            System.out.println("You haven't booked any flights yet.");
        } else {
            for (int i = 0; i < ticketsBucket.size(); i++) {
                String flightID = ticketsBucket.get(i).getFlight().flightID;
                String ticketStatus = ticketsBucket.get(i).getStatus();
                System.out.println("Flight ID: " + flightID
                        + "\n Ticket Status: " + ticketStatus);
            }
        }
    }

    public void editPassengerDetails(String newName, String newPassportNumber){
    if(newName != null && !newName.isEmpty()){
        this.name = newName;
    }
    if(newPassportNumber != null && !newPassportNumber.isEmpty()){
        this.passportNum = newPassportNumber; 
    }    
        System.out.println("Passenger details updated successfully.");
    }  
    
    @Override
    public String toString(){
        return "Passenger Information: "+
                "\nName: "+this.name+
                "\nPassport Number: "+this.passportNum;
    }


}



