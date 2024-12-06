import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static AnnualFlightScheduler afs;
    public static void main(String[] args) {
        afs = new AnnualFlightScheduler();
        Scanner sc = new Scanner(System.in);
        int choice; 

        do { 
            System.out.println("Welcome to Flight Booking System"); //maybe here can be name
            System.out.println("""
                    MENU:
                    (1): Search Flight
                    (2): Book Ticket
                    (3): Edit Ticket Information
                    (4): View Ticket Status
                    (5): Cancel a Ticket
                    (6): Exit
                                        
                    Enter your choice here:
                    """);
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: {
                    Main.SearchFlightForWeek();
                    break;
                }
                case 2: {
                    Main.BookTicket(); 
                    break;
                }
                case 3:
                    Main.EditTicketInformation();
                    Main.afs.saveFlightsToCsv();
                    break;
                case 4: {
                    System.out.println("Please enter your passport number:");
                    String passportNum = sc.nextLine();
                
                    Passenger passenger = afs.getPassengerInfo(passportNum); 
                
                    if (passenger != null) {
                        passenger.viewTicketStatus();
                    } else {
                        System.out.println("The passenger information could not be found.");
                    }
                    break;
                }
                case 5:{
                    Main.CancelTicket();
                    break;
                }                
            }
        } while (choice != 6); 

        System.out.println("Thanks for using!");
    }

    public static void EditTicketInformation(){
    Scanner s = new Scanner(System.in);
        System.out.println("Enter your current passport number");
        String passportNum = s.nextLine();
    
    Passenger passenger = afs.getPassengerInfo(passportNum);
    
    if(passenger != null){
        System.out.println("Enter new name (press Enter to skip):");
        String newName = s.nextLine();
        System.out.println("Enter new passport number (press Enter to skip):");
        String newPassportNum = s.nextLine();
        
        passenger.editPassengerDetails(newName, newPassportNum);
        System.out.println("Passenger details updated successfully.");
    }else{
        System.out.println("Passenger not found");
    }
}
    
    
    
    public static void BookTicket() { 
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the flight ID (format: Flight-<number>-<yyyy-MM-dd>):");
        String flightID = sc.nextLine();
        System.out.println("Please enter your name:");
        String name = sc.nextLine();
        System.out.println("Please enter your passport number:");
        String passportNum = sc.nextLine();

        Flight flight = AnnualFlightScheduler.processFlightID(flightID);

        Passenger passenger = new Passenger(passportNum, name); 

        flight.bookFlight(passenger, flight); 

    }

    public static void CancelTicket(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the flight ID:");
        String flightID = sc.nextLine();
        System.out.println("Enter your passport number:");
        String passportNum = sc.nextLine();
        
        Flight flight = AnnualFlightScheduler.processFlightID(flightID);
        Passenger passenger = afs.getPassengerInfo(passportNum);
        
        if(passenger != null && flight != null){
            boolean result = flight.cancelTicket(passenger);
            if(result){
                System.out.println("Ticket successfully canceled.");
                afs.saveFlightsToCsv();
            }else{
                System.out.println("Failed to cancel the ticket. Please check your information.");
            }
        }else{
            System.out.println("Flight or passenger not found.");
    }
    }
    
    
    
    public static void SearchFlightForWeek(){
        Scanner sc=new Scanner(System.in);
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("Enter a starting date:");
        LocalDate date1=LocalDate.parse(sc.nextLine(),formatter);
        System.out.println("Enter an ending date:");
        LocalDate date2=LocalDate.parse(sc.nextLine(),formatter);

        AnnualFlightScheduler.searchFlights(date1,date2);
    }

}