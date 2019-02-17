package sample;
import java.util.Arrays;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;

public abstract class Series {
    protected double a0;
    protected double d;
    protected int n;
    public Series(double a0, double d) {
        this.a0 = a0;
        this.d = d;
        this.n = 5;
    }
    public Series(double a0, double d, int n) {
        this.a0 = a0;
        this.d = d;
        this.n = n;
    }
    public abstract double getMemberAt(int j);
    public double getSum() {
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += getMemberAt(i);
        }
        return sum;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(getMemberAt(i));
            sb.append(", ");
        }
        sb.replace(sb.length()-2, sb.length()-1, ".");
        return sb.toString();
    }
    public void toFile(String name) throws IOException {
        Files.write(Paths.get(name), Arrays.asList(toString()), Charset.forName("UTF-8"));
    }
}

class Linear extends Series {
    public Linear(double a0, double d) {
        super(a0, d);
    }
    public Linear(double a0, double d, int n) {
        super(a0, d, n);
    }
    public double getMemberAt(int j) {
        return a0+d*j;
    }
}
class Exponential extends Series {
    public Exponential(double a0, double d) {
        super(a0, d);
    }
    public Exponential(double a0, double d, int n) {
        super(a0, d, n);
    }
    public double getMemberAt(int j) {
        return a0*Math.pow(d,j);
    }
}