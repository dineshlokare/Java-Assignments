package program_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MyfixedFiletoCsv {
    public static void main(String[] args) throws IOException {

        // taking the filename, number of columns and length for each columns from
        // commandline
        String filename = args[0];
        int numberColumns = Integer.parseInt(args[1]);
        int[] lengthColumns = new int[numberColumns];

        // checking for arguments if provided or not
        if (args.length == 0) {
            System.out.println("File name is not provided");
            return;
        }

        // parsing length of columns from commandline
        for (int i = 0; i < numberColumns; i++) {
            lengthColumns[i] = Integer.parseInt(args[i + 2]);
        }

        // showing given filename, number of columns and length of columns
        System.out.println("filename: " + filename);
        System.out.println("Number of Columns: " + numberColumns);

        for (int i = 0; i < lengthColumns.length; i++) {
            System.out.println("length of columns: " + lengthColumns[i]);
        }

        // csv file in which converted data will be written
        String outputFile = "newfile.csv";
        // reading the file and converting it into csv file
        readFile(filename, numberColumns, lengthColumns, outputFile);
    }
    
    // this methods reads the fixed-width file and writes the processed data into csv file
    public static void readFile(String filename, int numberColumns, int[] lengthColumns, String outputFile)
            throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename)); //reading the input file
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));// writing in the output file
        String line; 
        System.out.println("File has been created successfully!");

        // reading each line from the given file
        while ((line = reader.readLine()) != null) {
            // processing the line from the file and converting in the csv format
            String csvLine = fileProcess(line, numberColumns, lengthColumns);

            //writing the csv formatted line to the output file
            writer.write(csvLine);
            // moving to the next line in the output file
            writer.newLine();
        }
        //closing the reader and writer 
        reader.close();
        writer.close();
    }

    // method process the single line of fixed-width file and converts it into csv format
    public static String fileProcess(String line, int numberColumns, int[] lengthColumns) {
        StringBuilder csvLine = new StringBuilder(); // to build the csv line
        int position = 0; //keeping track of the current postion in the line


        for (int i = 0; i < numberColumns; i++) {
            // extract the field for the current column based on the position and column length
            String field = extractLine(line, position, lengthColumns[i]);

            //formatting the extracted fields
            String formattedLine = formatLine(field);
            
            // appending the formatted field into csv line
            csvLine.append(formattedLine);

            //adding comma after every field except the last one
            if (i < numberColumns - 1) {
                csvLine.append(",");
            }

            //moving the position forward by the length of the current column
            position += lengthColumns[i];

        }
        return csvLine.toString();
        //return the formatted csv line
    }

    // this method extract the substring(field) from the fixed-width line based on start position and length
    public static String extractLine(String line, int startposition, int length) {
        //extracting the substring from start position with given length,not exceeding the line length
        return line.substring(startposition, Math.min(startposition + length, line.length()));
    }
    // this method formats the fields by trimming spaces, removing leading zeros and enclosing the field in quotes 
    public static String formatLine(String field) {
        
        // leading and trailing spaces are trimmed from field
        field = field.trim();
        //removing leading zeros 
        field = field.replaceFirst("^0+(?!$)", "");

        return "\"" + field + "\"";
        //enclosing fields in double quotes
    }

}
