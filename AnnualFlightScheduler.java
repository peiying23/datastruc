import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnualFlightScheduler {

    static HashMap<LocalDate, ArrayList<Flight>> flightsByDate;
    static DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AnnualFlightScheduler(){
        flightsByDate=new HashMap<>();
        initializeFlightsForYear(2025);
    }

    public void initializeFlightsForYear(int year){
        LocalDate startDate= LocalDate.of(2025,1,1);
        LocalDate endDate= LocalDate.of(2025,12,31);

        while(!startDate.isAfter(endDate)){
            ArrayList<Flight>dailyFlights=new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                dailyFlights.add(new Flight("Flight-"+i+"-"+startDate));

            }
            flightsByDate.put(startDate,dailyFlights);
            startDate=startDate.plusDays(1);
        }
    }

    //I am thinking where to locate this method.
    public static void searchFlights(LocalDate date1, LocalDate date2) {
        System.out.println("\nFlight for the Weeks: ");
        System.out.println();
        while (!date1.isAfter(date2)) {
            ArrayList<Flight> temp = flightsByDate.get(date1);
            for (int i = 0; i < temp.size(); i++) {
                System.out.println(temp.get(i));
            }
            System.out.println("-------------------------------------------------------------------------");
            date1=date1.plusDays(1);
        }
    }

    public static  Flight processFlightID(String flightID){
        //format of a flightID
        //Flight-0-2025-01-03
        String[]parts=flightID.split("-");
        int flightIndex=Integer.parseInt(parts[1]);
        String dateString=parts[2]+"-"+parts[3]+"-"+parts[4];
        LocalDate date=LocalDate.parse(dateString,formatter);

        return flightsByDate.get(date).get(flightIndex);

    }

}
