import java.util.Date;

public class GatorReservation {
    public int patronID;
    public int priorityNumber;
    public Date timeOfReservation;

    GatorReservation(int patronID, int priorityNumber) {
        this.patronID = patronID;
        this.priorityNumber = priorityNumber;
        this.timeOfReservation = new Date();
    }
}
