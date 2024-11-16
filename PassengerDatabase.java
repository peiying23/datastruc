import java.util.HashMap;

public class PassengerDatabase {

        HashMap<String,Passenger> passengerDatabase;
        public PassengerDatabase(){
            passengerDatabase=new HashMap<>();
        }

        // Add passenger to database
        public void addPassenger(Passenger passenger) {
            passengerDatabase.put(passenger.getPassportNum(), passenger);
        }

        // Retrieve passenger from database
        public Passenger getPassenger(String passportNum) {
            return passengerDatabase.get(passportNum);
        }

        // Remove passenger from database
        public boolean removePassenger(String passportNum) {
            return passengerDatabase.remove(passportNum) != null;
        }

    }
