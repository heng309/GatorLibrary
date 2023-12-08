import java.util.PriorityQueue;

public class GatorBook {
    public int BookID;
    public String BookName;
    public String AuthorName;
    public String AvailabilityStatus;
    public int BorrowedBy;
    public PriorityQueue<GatorReservation> ReservationHeap;

    GatorBook(int BookID, String BookName, String AuthorName, String AvailabilityStatus) {
        this.BookID = BookID;
        this.BookName = BookName;
        this.AuthorName = AuthorName;
        this.AvailabilityStatus = AvailabilityStatus;
        this.BorrowedBy = -1;
        this.ReservationHeap = new PriorityQueue<GatorReservation>((g1, g2) -> {
            if (g1.priorityNumber < g2.priorityNumber)
                return -1;
            else if (g1.priorityNumber > g2.priorityNumber)
                return 1;
            else
                return g1.timeOfReservation.compareTo(g2.timeOfReservation);
        });
    }
}
