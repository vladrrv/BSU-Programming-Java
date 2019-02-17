import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AutoArray<Car> carArray = new AutoArray<>();
        AutoArray<Bus> busArray = new AutoArray<>();
        try {
            Scanner scanner1 = new Scanner(new File("input1.txt"));
            while (scanner1.hasNextLine()) {
                String name = scanner1.next();
                String color = scanner1.next();
                String type = scanner1.next();
                String material = scanner1.next();
                carArray.add(new Car(name, color, type, material));
            }
            System.out.println("Car array: " + carArray);
            System.out.println("How many cars 'ford, black, oil, polyester': " +
                    carArray.getNumberOfAutos(new Car("ford", "black", "oil", "polyester")));
            System.out.println("Sorted car array: " + carArray.toStringSorted());
            System.out.println("Max in car array: " + carArray.max());
            /*-------------------------------------------------------------*/
            Scanner scanner2 = new Scanner(new File("input2.txt"));
            while (scanner2.hasNextLine()) {
                String name = scanner2.next();
                String color = scanner2.next();
                String type = scanner2.next();
                int numSeats = scanner2.nextInt();
                int numDoors = scanner2.nextInt();
                busArray.add(new Bus(name, color, type, numSeats, numDoors));
            }
            System.out.println("Bus array: " + busArray);
            System.out.println("How many buses 'mercedes, black, oil, 10 seats, 2 doors': " +
                    busArray.getNumberOfAutos(new Bus("mercedes", "black", "oil", 10, 2)));
            System.out.println("Sorted bus array: " + busArray.toStringSorted());
            System.out.println("Max in bus array: " + busArray.max());
        }
        catch (IOException | InputMismatchException | Auto.TypeException | Car.MaterialException | Bus.SeatsException | Bus.DoorsException ex ) {
            System.out.println(ex.getMessage());
        }
    }
}
