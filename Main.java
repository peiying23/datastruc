//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {

        AnnualFlightScheduler afs=new AnnualFlightScheduler(); // to initiate all the flights
        Passenger user=new Passenger("123456","MARRY");

        //assuming now I got user login
        Scanner sc=new Scanner(System.in);
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
        int choice=sc.nextInt();
        sc.nextLine();

        switch(choice){
            case 1:{
                Main.SearchFlightForWeek();
                break;
            }
            case 2:{
                Main.BookTicket(user);
                break;
            }
            case 3:break;
            case 4:break;
            case 5:break;
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

    public static void BookTicket(Passenger user){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the flight ID:");
        String flightID=sc.nextLine();
        Flight flight=AnnualFlightScheduler.processFlightID(flightID);
        flight.bookFlight(user,flight);
    }




}