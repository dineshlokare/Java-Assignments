package program_1;

public class MySqrootA{
    public static void main(String[] args) {
        if(args.length == 0){
            System.out.print("Number not Provided!!");
            return;
        }
        double number;
        try {
            number = Double.parseDouble(args[0]);
        } catch (Exception e) {
            System.out.println("Number is Invalid!!");
            return;
        }
        if(number < 0){
            System.out.println(number + "number is negative!!");
            return;
        }
        calculation(number);

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