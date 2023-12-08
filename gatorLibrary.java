import java.io.*;

public class gatorLibrary {
    public static void main (String[] args) {
        GatorLibraryMain library = new GatorLibraryMain();

        // File name will be read
        String inputFileName = args[0];

        // Read input file and execute corresponding functions
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            // Define output format
            BufferedWriter writer = new BufferedWriter(new FileWriter(inputFileName.substring(0,inputFileName.length()-4) + "_output_file.txt"));
            String line;
            outer:
            while ((line = reader.readLine()) != null) {
                // Split by '('
                String[] tokens = line.split("\\(");
                // Get operation name
                String command = tokens[0].trim();
                // Get operation parameters
                String[] arguments = tokens[1].substring(0, tokens[1].length() - 1).split(",");
                switch (command) {
                    case "InsertBook":
                        int bookIDInsert = Integer.parseInt(arguments[0].trim());
                        String bookNameInsert = arguments[1].trim();
                        String authorNameInsert = arguments[2].trim();
                        String availabilityStatusInsert = arguments[3].trim();
                        library.insertBook(bookIDInsert, bookNameInsert, authorNameInsert, availabilityStatusInsert);
                        break;
                    case "PrintBook":
                        int bookID = Integer.parseInt(arguments[0].trim());
                        writer.write(library.printBook(bookID));
                        break;
                    case "PrintBooks":
                        int bookID1 = Integer.parseInt(arguments[0].trim());
                        int bookID2 = Integer.parseInt(arguments[1].trim());
                        writer.write(library.printBooks(bookID1, bookID2));
                        break;
                    case "BorrowBook":
                        int patronIDBorrow = Integer.parseInt(arguments[0].trim());
                        int bookIDBorrow = Integer.parseInt(arguments[1].trim());
                        int patronPriorityBorrow = Integer.parseInt(arguments[2].trim());
                        writer.write(library.borrowBook(patronIDBorrow, bookIDBorrow, patronPriorityBorrow));
                        writer.newLine();
                        break;
                    case "ReturnBook":
                        int patronIDReturn = Integer.parseInt(arguments[0].trim());
                        int bookIDReturn = Integer.parseInt(arguments[1].trim());
                        writer.write(library.returnBook(patronIDReturn, bookIDReturn));
                        writer.newLine();
                        break;
                    case "DeleteBook":
                        int bookIDDelete = Integer.parseInt(arguments[0].trim());
                        writer.write(library.deleteBook(bookIDDelete));
                        writer.newLine();
                        break;
                    case "FindClosestBook":
                        int bookIDClosest = Integer.parseInt(arguments[0].trim());
                        writer.write(library.findClosestBook(bookIDClosest));
                        writer.newLine();
                        break;
                    case "ColorFlipCount":
                        writer.write("Colour Flip Count: " + library.colorFlip());
                        writer.newLine();
                        writer.newLine();
                        break;
                    case "Quit":
                        writer.write("Program Terminated!!");
                        break outer;
                    default:
                        System.out.println("Unknown command: " + command);
                        break;
                }
            }
            writer.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}