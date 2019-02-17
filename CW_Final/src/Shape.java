import java.awt.*;
import java.util.Comparator;

public abstract class Shape {
    Point p1;
    Point p2;

    public Shape(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
    public abstract double area();

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }
    public int getX1() {
        return p1.x;
    }
    public int getX2() {
        return p2.x;
    }
    public int getY1() {
        return p1.y;
    }
    public int getY2() {
        return p2.y;
    }
    @Override
    public String toString() {
        return "p1=" + p1 +
                ", p2=" + p2;
    }
}
class Circle extends  Shape implements Comparable<Circle> {
    private Point center;
    private double radius;
    public Circle(Point p1, Point p2) {
        super(p1, p2);
        double dx = (p1.x-p2.x);
        double dy = (p1.y-p2.y);
        center = p1;
        radius = Math.sqrt(dx*dx+dy*dy);
    }

    @Override
    public int compareTo(Circle o) {

        return Comparator.comparingInt(Circle::getCenterX).
                thenComparing(Circle::getCenterY)
                .reversed()
                .compare(this, o);
    }
    public int getCenterX() {
        return center.x;
    }
    public int getCenterY() {
        return center.y;
    }
    @Override
    public double area() {
        return Math.PI*radius*radius;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", center=" + center +
                ", radius=" + radius +
                '}';
    }
}