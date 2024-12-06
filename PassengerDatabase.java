import java.util.HashMap;

public class PassengerDatabase {

        HashMap<String,Passenger> passengerDatabase;
        public PassengerDatabase(){
            passengerDatabase=new HashMap<>();
        }

        public void addPassenger(Passenger passenger) {
            passengerDatabase.put(passenger.getPassportNum(), passenger);
        }

        public Passenger getPassenger(String passportNum) {
            return passengerDatabase.get(passportNum);
        }

        public boolean removePassenger(String passportNum) {
            return passengerDatabase.remove(passportNum) != null;
        }

    }
