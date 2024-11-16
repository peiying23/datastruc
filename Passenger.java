import java.util.ArrayList;
import java.util.HashMap;

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

    public void addTicketToBucket(Ticket ticket){
        ticketsBucket.add(ticket);
    }

    public void viewTicketStatus(){
        for (int i = 0; i < ticketsBucket.size(); i++) {
            String flightID=ticketsBucket.get(i).getFlight().flightID;
            String ticketStatus=ticketsBucket.get(i).getStatus();
            System.out.println("Flight ID: "+flightID
                    +"\n Ticket Status: "+ticketStatus);
        }
    }

    @Override
    public String toString(){
        return "Passenger Information: "+
                "\nName: "+this.name+
                "\nPassport Number: "+this.passportNum;
    }


}



