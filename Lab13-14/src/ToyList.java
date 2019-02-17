import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ToyList extends ArrayList<Toy> {
    public void sort() {
        /*
        Comparator<Toy> toyComparator = (t1, t2) -> {
            return t1.getCost()-t2.getCost();
        };
        Collections.sort(this, toyComparator);*/
        Collections.sort(this, Comparator.comparingInt(Toy::getCost));
    }
    public ToyList selectToysInAgeBounds(int lower, int upper) {
        ToyList selected = new ToyList();
        for (Toy toy : this) {
            if (toy.getLowerAgeBound() <= lower && toy.getUpperAgeBound() >= upper) {
                selected.add(toy);
            }
        }
        selected.sort();
        return selected;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[\n");
        for (Toy toy : this) {
            sb.append(toy);
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }
}
