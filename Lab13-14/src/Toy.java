import java.io.IOException;
import java.io.Writer;
import java.util.Vector;


public class Toy {
    private String name;
    private int cost;
    private int lowerAgeBound;
    private int upperAgeBound;
    public Toy(String name, int cost, int lowerAgeBound, int upperAgeBound) {
        this.name = name;
        this.cost = cost;
        this.lowerAgeBound = lowerAgeBound;
        this.upperAgeBound = upperAgeBound;
    }

    @Override
    public String toString() {
        return "Name: " + getName() + ", cost: " + getCost() + ", for children " + getLowerAgeBound() + "-" + getUpperAgeBound() + " y.o.";
    }

    public String getName() {
        return name;
    }
    public int getCost() {
        return cost;
    }
    public int getLowerAgeBound() {
        return lowerAgeBound;
    }
    public int getUpperAgeBound() {
        return upperAgeBound;
    }
    public Vector<String> getData() {
        Vector<String> vector = new Vector<>(3);
        vector.add(getName());
        vector.add(String.valueOf(getCost()));
        vector.add(String.valueOf(getLowerAgeBound())+"-"+String.valueOf(getUpperAgeBound()));
        return vector;
    }
    public void writeSelf(Writer writer) throws IOException {
        writer.append("<toy>");
        writer.append("<name>");
        writer.append(getName());
        writer.append("</name>");
        writer.append("<cost>");
        writer.append(String.valueOf(getCost()));
        writer.append("</cost>");
        writer.append("<upper>");
        writer.append(String.valueOf(getUpperAgeBound()));
        writer.append("</upper>");
        writer.append("<lower>");
        writer.append(String.valueOf(getLowerAgeBound()));
        writer.append("</lower>");
        writer.append("</toy>");
    }
}
class X extends Toy {
    X(String name, int cost, int lowerAgeBound, int upperAgeBound) {
        super(name, cost, lowerAgeBound, upperAgeBound);
    }
    public void writeSelf(Writer writer) {

    }
}