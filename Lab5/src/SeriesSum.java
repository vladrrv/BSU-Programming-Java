class ArgsException extends Exception {
    ArgsException(String m) {
        super(m);
    }
}
public class SeriesSum {
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                String m1 = "Too many arguments passed!\nRequired arguments: double x, double epsilon";
                String m2 = "Too few arguments passed!\nRequired arguments: double x, double epsilon";
                throw new ArgsException(args.length > 2? m1 : m2);
            }
            System.out.println("Series Sum: " + getSum(Double.parseDouble(args[0]), Double.parseDouble(args[1])));
        }
        catch (ArgsException e) {
            System.out.println(e.getMessage());
        }
        catch (NumberFormatException e) {
            System.out.println("Incorrect number format!\nRequired arguments: double x, double epsilon");
        }
    }
    static double getSum(double x, double epsilon) {
        int k = 1;
        double sum = 0;
        double numerator = (x + 1/Math.sqrt(k));
        double member = numerator/2;
        while (Math.abs(member) > epsilon/10) {
            sum += member;
            member /= numerator;
            numerator += (-1/Math.sqrt(k++)) + 1/Math.sqrt(k);
            member *= (numerator/(k+1));
        }
        return round(sum, (int)Math.log10(1/epsilon));
    }
    static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
