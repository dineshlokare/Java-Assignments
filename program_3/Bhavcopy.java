package program_3;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class BhavcopyData {
    String symbol;
    String series;
    Double open;
    Double high;
    Double low;
    Double close;
    Double last;
    Double prevClose;
    Long totTrdQty;
    Double totTrdVal;
    String timestamp;
    Integer totalTrades;
    String isin;

    public BhavcopyData(String symbol, String series, Double open, Double high, Double low, Double close, Double last,
            Double prevClose, Long totTrdQty, Double totTrdVal, String timestamp, Integer totalTrades, String isin) {
        this.symbol = symbol;
        this.series = series;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.last = last;
        this.prevClose = prevClose;
        this.totTrdQty = totTrdQty;
        this.totTrdVal = totTrdVal;
        this.timestamp = timestamp;
        this.totalTrades = totalTrades;
        this.isin = isin;
    }

    BhavcopyData() {
    }

    // public String getSymbol() {
    // return symbol;
    // }
    List<BhavcopyData> readCSV(String filepath) {
        List<BhavcopyData> datalList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 13) {
                    continue;
                }
                BhavcopyData data = new BhavcopyData(
                        values[0].trim(),
                        values[1].trim(),
                        parseDouble(values[2].trim()),
                        parseDouble(values[3].trim()),
                        parseDouble(values[4].trim()),
                        parseDouble(values[5].trim()),
                        parseDouble(values[6].trim()),
                        parseDouble(values[7].trim()),
                        parseLong(values[8].trim()),
                        parseDouble(values[9].trim()),
                        values[10].trim(),
                        parseInt(values[11].trim()),
                        values[12].trim());
                datalList.add(data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return datalList;
    }

    Double parseDouble(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }

    }

    private Integer parseInt(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long parseLong(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }

    }

    @Override
    public String toString() {
        return String.join(", ", symbol, series, String.valueOf(open), String.valueOf(low), String.valueOf(high),
                String.valueOf(close), String.valueOf(last),
                String.valueOf(prevClose), String.valueOf(totTrdQty), String.valueOf(totTrdVal), timestamp,
                String.valueOf(totalTrades), isin);
    }

}

class Data {

    public void displayData() {

        System.out.println("Select Operations to Perform:");
        System.out.println("1. Get information about a particular symbol");
        System.out.println("2. Count the number of symbols for a given SERIES");
        System.out.println("3. List all symbols where ((CLOSE - OPEN) / OPEN) > N%");
        System.out.println("4. List all symbols where ((HIGH - LOW) / LOW) > N%");
        System.out.println("5. Standard deviation for all symbols of a given series");
        System.out.println("6. Top N symbols having maximum gain");
        System.out.println("7. Bottom N symbols having lowest gain");
        System.out.println("8. Top N most traded (by volume) symbols");
        System.out.println("9. Bottom N least traded (by volume) symbols");
        System.out.println("10. Highest and lowest traded (by TOTRDVAL) for a given series");
    }

    public void takeChoice(int choice, List<BhavcopyData> datalList, Scanner scan) {
        switch (choice) {
            case 1:
                System.out.println("Enter the Symbol: ");
                String symbol = scan.next().trim();
                System.out.println(symbol);
                boolean found = false;
                for (BhavcopyData data : datalList) {
                    if (data.symbol.equalsIgnoreCase(symbol)) {
                        System.out.println(data);
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("Symbol not found");
                }
                break;

            case 2:
                System.out.println("Enter the SERIES: ");
                String series = scan.next().trim();
                int count = 0;
                for (BhavcopyData data : datalList) {
                    if (data.series != null && data.series.equalsIgnoreCase(series)) {
                        count++;
                    }
                }
                System.out.println("Number of symbol for Series " + series + " : " + count);
                break;

            case 3:
                System.out.println("Enter the percentage (Gain) N%: ");
                double N = scan.nextDouble();

                for (BhavcopyData data : datalList) {
                    if (data.open != null && data.close != null && data.open != 0) {
                        double percentageGain = ((data.close - data.open) / data.open) * 100;
                        if (percentageGain > N) {
                            System.out.println(data.symbol + " has a gain greater than " + N + "%");
                        }
                    }
                }
                break;

            // case 4:
            // System.out.println("Performing task for option 1");
            // break;
            // case 5:
            // System.out.println("Performing task for option 1");
            // break;
            // case 6:
            // System.out.println("Performing task for option 1");
            // break;
            // case 7:
            // System.out.println("Performing task for option 1");
            // break;
            // case 8:
            // System.out.println("Performing task for option 1");
            // break;
            // case 9:
            // System.out.println("Performing task for option 1");
            // break;
            // case 10:
            // System.out.println("Performing task for option 1");
            // break;
            default:
                System.out.println("Invalid Choice");
        }
    }

}

public class Bhavcopy {

    public static void main(String[] args) {
        BhavcopyData bh = new BhavcopyData();
        List<BhavcopyData> datalList = bh.readCSV("/home/dineshl/Desktop/Java-Assignments/program_3/Bhavcopy.csv");

        Scanner scan = new Scanner(System.in);
        Data data = new Data();
        int choice;

        while (true) {
            data.displayData();
            System.out.println("Enter you choice: ");
            choice = scan.nextInt();
            data.takeChoice(choice, datalList, scan);
            System.out.println();
        }

    }

}
