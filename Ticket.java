public class Ticket {

    Flight flight;
    Passenger passenger;
    private String status;

    public Ticket(Passenger passenger, Flight flight){
        this.passenger=passenger;
        this.flight=flight;
    }

    public String getStatus() {
        return status;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}
