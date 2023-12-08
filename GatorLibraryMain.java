import java.util.*;

public class GatorLibraryMain {
    private GatorRedBlackTree bookTree;
    private String newLine = System.getProperty("line.separator");

    GatorLibraryMain() {
        this.bookTree = new GatorRedBlackTree();
    }

    public String printBook(int bookID) {
        StringBuffer sb = new StringBuffer();
        GatorTreeNode bookNode = bookTree.searchBook(bookID);
        if (bookNode == bookTree.getNull()) {
            sb.append("BookID not found in the Library");
        }
        else {
            GatorBook book = bookNode.book;
            int[] temp = new int[book.ReservationHeap.size()];
            int i = 0;
            for (GatorReservation reservation : book.ReservationHeap) {
                temp[i++] = reservation.patronID;
            }

            sb.append("BookID = ").append(book.BookID).append(newLine)
                    .append("Title = ").append(book.BookName).append(newLine)
                    .append("Author = ").append(book.AuthorName).append(newLine)
                    .append("Availability = ").append(book.AvailabilityStatus).append(newLine)
                    .append("BorrowedBy = ").append(book.BorrowedBy == -1 ? "None" : book.BorrowedBy).append(newLine)
                    .append("Reservations = ").append(Arrays.toString(temp)).append(newLine).append(newLine);
        }
        return sb.toString();
    }

    public String printBooks(int bookID1, int bookID2) {
        StringBuffer sb = new StringBuffer();
        List<GatorBook> books = bookTree.getBooksInInterval(bookID1, bookID2);
        for (GatorBook book : books) {
            sb.append(printBook(book.BookID));
        }
        return sb.toString();
    }

    public void insertBook(int bookID, String bookName, String authorName, String availabilityStatus) {
        GatorBook book = new GatorBook(bookID, bookName, authorName, availabilityStatus);
        GatorTreeNode bookNode = new GatorTreeNode(book);
        bookTree.insert(bookNode);
    }

    /*
    # If the book is available
    Book <bookID> Borrowed by Patron <patronID>
    # If the book is already Borrowed by another Patron
    Book <bookID> Reserved by Patron <patronID>
    */
    public String borrowBook(int patronID, int bookID, int patronPriority) {
        GatorBook book = bookTree.searchBook(bookID).book;
        StringBuffer sb = new StringBuffer();
        if (book.BorrowedBy == -1) {
            book.AvailabilityStatus = "No";
            book.BorrowedBy = patronID;
            sb.append(String.format("Book %s Borrowed by Patron %s", bookID, patronID)).append(newLine);
        }
        else {
            book.ReservationHeap.add(new GatorReservation(patronID, patronPriority));
            sb.append(String.format("Book %s Reserved by Patron %s", bookID, patronID)).append(newLine);
        }
        return sb.toString();
    }

    /*
    Book <bookID> Returned by Patron <patronID>
    # Additionally if there are reservations made for this book and the top patron in heap is patronID2
    Book <bookID> Allotted to Patron <patronID2>
    */
    public String returnBook(int patronID, int bookID) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Book %s Returned by Patron %s", bookID, patronID)).append(newLine).append(newLine);
        GatorBook book = bookTree.searchBook(bookID).book;
        if (!book.ReservationHeap.isEmpty()) {
            GatorReservation nextReservation = book.ReservationHeap.poll();
            book.BorrowedBy = nextReservation.patronID;
            sb.append(String.format("Book %s Allotted to Patron %s", bookID, nextReservation.patronID)).append(newLine);
        }
        else {
            book.AvailabilityStatus = "Yes";
            book.BorrowedBy = -1;
        }
        return sb.toString();
    }

    /*
    # If there are no reservations for this book.
    Book 2 is no longer available.
    # If the book is reserved by patronID1, patronID2, patronID3
    Book 2 is no longer available. Reservations made by Patrons patronID1,
    patronID2, patronID3 have been cancelled!
    */
    public String deleteBook(int bookID) {
        StringBuffer sb = new StringBuffer();
        GatorBook book = bookTree.searchBook(bookID).book;
        bookTree.deleteNode(bookID);
        if (!book.ReservationHeap.isEmpty()) {
            sb.append(String.format("Book %s is no longer available.", bookID));
            sb.append(" Reservations made by Patrons ");
            while (!book.ReservationHeap.isEmpty()) {
                sb.append(book.ReservationHeap.poll().patronID).append(" ");
            }
            sb.append(" have been cancelled!").append(newLine);
        }
        else {
            sb.append(String.format("Book %s is no longer available.", bookID)).append(newLine);
        }
        return sb.toString();
    }

    public String findClosestBook(int bookID) {
        StringBuffer sb = new StringBuffer();
        List<Integer> bookClosestIDs = bookTree.findClosestNode(bookID);
        Collections.sort(bookClosestIDs);
        sb.append(String.format("Book %s is the closest book to book %s.", bookClosestIDs.toString(), bookID)).append(newLine).append(newLine);
        for (Integer id : bookClosestIDs) {
            sb.append(printBook(id));
        }
        return sb.toString();
    }

    public int colorFlip(){
        return bookTree.getFlipCount();
    }
}

