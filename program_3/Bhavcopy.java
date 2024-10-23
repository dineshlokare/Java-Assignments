package program_3;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

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
    Double gain;

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

    List<BhavcopyData> readCSV(String filepath) {
        List<BhavcopyData> dataList = new ArrayList<>();
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
                dataList.add(data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
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

    public double calculateMean(List<Double> values) {
        double sum = 0.0;
        for (Double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    public double calculateStdDeviation(List<Double> values, double mean) {
        double sum = 0.0;
        for (Double value : values) {
            sum += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sum / values.size());
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

    public void takeChoice(int choice, List<BhavcopyData> dataList, Scanner scan) {
        switch (choice) {
            case 1:
                System.out.println("Enter the Symbol: ");
                String symbol = scan.next().trim();
                boolean found = false;
                for (BhavcopyData data : dataList) {
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
                for (BhavcopyData data : dataList) {
                    if (data.series != null && data.series.equalsIgnoreCase(series)) {
                        count++;
                    }
                }
                System.out.println("Number of symbol for Series " + series + " : " + count);
                break;

            case 3:
                System.out.println("Enter the percentage (Gain) N%: ");
                double N = scan.nextDouble();

                for (BhavcopyData data : dataList) {
                    if (data.open != null && data.close != null && data.open != 0) {
                        double percentageGain = ((data.close - data.open) / data.open) * 100;
                        if (percentageGain > N) {
                            System.out.println(data.symbol + " has a gain greater than " + N + "%");
                        }
                    }
                }
                break;

            case 4:
                System.out.println("Enter the percentage N (N could be negative): ");
                double percentageN = scan.nextDouble();
                boolean foundData = false;
                for (BhavcopyData data : dataList) {
                    if (data.low != null && data.low != 0 && data.high != null) {
                        double percentageChange = ((data.high - data.low) / data.low) * 100;
                        if (percentageChange > percentageN) {
                            System.out.println(data.symbol);
                            foundData = true;
                        }

                    }
                }
                if (!foundData) {
                    System.out.println("No symbols found for the required percentage change");
                }
                break;
            case 5:
                System.out.println("Enter the SERIES to get Standar Deviation: ");
                String seriesInput = scan.next().trim();

                List<Double> closePrices = new ArrayList<>();
                boolean foundSeries = false;

                for (BhavcopyData data : dataList) {
                    if (data.series.endsWith(seriesInput) && data.close != null) {
                        closePrices.add(data.close);
                        foundSeries = true;
                    }
                }
                if (!foundSeries) {
                    System.out.println("No data found for the series" + seriesInput);
                    break;
                }
                BhavcopyData load = new BhavcopyData();
                double mean = load.calculateMean(closePrices);
                double standardDeviation = load.calculateStdDeviation(closePrices, mean);

                System.out.printf("Standard deviation for the series %s: %.2f\n", seriesInput, standardDeviation);
                break;
            case 6:
                System.out.println("Enter the number of TOPGAINERS (N): ");
                int Number = scan.nextInt();

                List<BhavcopyData> gainerList = new ArrayList<>();

                for (BhavcopyData data : dataList) {
                    if (data.open != null && data.close != null && data.open != 0) {
                        double gain = ((data.close - data.open) / data.open) * 100;

                        data.gain = gain;
                        gainerList.add(data);

                    }
                }
                gainerList.sort((d1, d2) -> Double.compare(d2.gain, d1.gain));

                System.out.println("Top " + Number + " gainers:");
                for (int i = 0; i < Number && i < gainerList.size(); i++) {
                    BhavcopyData topgainer = gainerList.get(i);
                    System.out.println(topgainer.symbol + " with gain:" + String.format("%.2f", topgainer.gain) + "%");
                }
                break;
            case 7:
                System.out.println("Enter the number of laggards (N):");
                int bottomNumber = scan.nextInt();

                List<BhavcopyData> bottomLaggards = new ArrayList<>();

                for (BhavcopyData data : dataList) {
                    if (data.open != null && data.close != null && data.open != 0) {
                        double gain = ((data.close - data.open) / data.open) * 100;

                        data.gain = gain;
                        bottomLaggards.add(data);

                    }

                }
                bottomLaggards.sort((d1, d2) -> Double.compare(d1.gain, d2.gain));
                System.out.println("Bottom " + bottomNumber + " Laggards");
                for (int i = 0; i < Math.min(bottomNumber, bottomLaggards.size()); i++) {
                    System.out
                            .println(bottomLaggards.get(i).symbol + " with gain: " + bottomLaggards.get(i).gain + "%");

                }
                break;
            case 8:
                System.out.println("Enter the number of top traded symbols: ");
                int topNtraded = scan.nextInt();
                dataList.sort((data1, data2) -> Long.compare(data2.totTrdQty, data1.totTrdQty));

                System.out.println("Top " + topNtraded + " most traded symbols: ");
                for (int i = 0; i < topNtraded && i < dataList.size(); i++) {
                    System.out.println(dataList.get(i).symbol + " ");
                }
                System.out.println();

                break;
            case 9:
                System.out.println("Enter the number of least traded symbols: ");
                int leastNtraded = scan.nextInt();
                dataList.sort((data1, data2) -> Long.compare(data1.totTrdQty, data2.totTrdQty));

                System.out.println("Number of " + leastNtraded + " least traded symbols: ");
                for (int i = 0; i < leastNtraded && i < dataList.size(); i++) {
                    System.out.println(dataList.get(i).symbol + " ");
                }
                System.out.println();

                break;
            case 10:
                System.out.println("Enter the SERIES to get highest & lowest trades: ");
                String getSeries = scan.next().trim();

                BhavcopyData highTraded = null;
                BhavcopyData lowTraded = null;

                for (BhavcopyData data : dataList) {
                    if (data.series.equalsIgnoreCase(getSeries)) {
                        if (highTraded == null || data.totTrdVal > highTraded.totTrdVal) {
                            highTraded = data;
                        }
                        if (lowTraded == null || data.totTrdVal < lowTraded.totTrdVal) {
                            lowTraded = data;
                        }
                    }
                }
                if (highTraded == null || lowTraded == null) {
                    System.out.println("No data found for the given series " + getSeries);
                } else {
                    System.out.printf("Highest traded symbol for TOTRDVAL for series %s: %s\n", getSeries,
                            highTraded.symbol);
                    System.out.printf("Lowest traded symbol for TOTRDVAL for series %s: %s\n", getSeries,
                            lowTraded.symbol);
                }

                break;
            default:
                System.out.println("Invalid Choice");
        }
    }

}

public class Bhavcopy {

    public static void main(String[] args) {
        BhavcopyData bh = new BhavcopyData();
        List<BhavcopyData> dataList = bh.readCSV("/home/dineshl/Desktop/Java-Assignments/program_3/Bhavcopy.csv");

        Scanner scan = new Scanner(System.in);
        Data data = new Data();
        int choice;

        while (true) {
            data.displayData();
            System.out.println("Enter you choice: ");
            choice = scan.nextInt();
            data.takeChoice(choice, dataList, scan);
            System.out.println();
        }

    }

}
