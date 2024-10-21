package program_1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MySqrootB{
    public static void main(String[] args) throws FileNotFoundException, IOException {

       
        String filename = "/home/dineshl/Desktop/Java-Assignments/program_1/myfile.txt";

            try (BufferedReader read = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = read.readLine()) != null) {

                    try 
                    {
                      double number = Double.parseDouble(line);
                      calculation(number);

                    }
                    catch (Exception e) {
                        System.out.println("Number is not present");
                        return;
                    }
                } 
                read.close();
            }catch (Exception e)
            {
                System.out.println("Error while reading...");
            }           
        
        

}
public static void calculation(double number){
        double z = 1.0;
        for(int i = 0; i < 25; i++){
            double previousZ = z;
            z = z - (z * z - number) / (2 * z);

            if(Math.abs(z - previousZ) <= 0.001){
                break;
            }
        }
        System.out.printf("%.2f %.4f%n",number,z);

    }
}