import java.util.ArrayList;
import java.util.Collections;

public class XArrayList<T extends Comparable<T>> extends ArrayList<T> {
    T min() {
        return Collections.min(this);
    }
    T max() {
        return Collections.max(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[\n");
        for (T el : this) {
            sb.append(el);
            sb.append("\n");
        }
        sb.append("]");
        return super.toString();
    }
}
