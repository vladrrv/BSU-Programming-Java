import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

public class AutoArray<T extends Auto> extends ArrayList<T> {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[\n");
        for (T el : this) {
            sb.append(el);
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }
    public String toStringSorted() {
        AutoArray<T> sorted = (AutoArray<T>)this.clone();
        Collections.sort(sorted);
        StringBuilder sb = new StringBuilder("[\n");
        for (T el : sorted) {
            sb.append(el);
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }
    int getNumberOfAutos(T auto) {
        int count = 0;
        for (T el : this) {
            if (el.compareTo(auto) == 0) {
                count++;
            }
        }
        //int frequency = Collections.frequency(this, auto);
        return count;
    }

    T find(T auto) {
        int idx = Collections.binarySearch(this, auto);
        if (idx < 0) throw new NoSuchElementException();
        return this.get(idx);
    }
    T max() {
        return Collections.max(this);
    }
}
