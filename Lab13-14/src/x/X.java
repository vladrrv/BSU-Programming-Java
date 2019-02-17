package x;

import java.util.ArrayList;
import java.util.List;

interface Inter {
    static void test() {

    }
}
enum E {
    A("A"),B("B"),C("C");
    private String s;
    E(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }
}

public class X {
    public int a;
    protected int b;
    int c;
    private int d;
}
class Y {
    static int sa;
    private int sb;
    static class Z {
        void z() {
            sa = 0;
            Y yo = new Y();
            yo.sb = 0;
        }
    }
    class Zt {
        void z() {
            sa = 0;
            Y yo = new Y();
            yo.sb = 0;
        }
    }
    {
        Long x = 0L;
        System.out.print(0);
    }
    void y() {
        X o = new X();
    }
    public void doSomething(int... a) {
        for (E e : E.values()) {
            e.name();
        }
    }
    public void doSomething(long... b) {
        ArrayList l = new ArrayList();
        l.add("");
        l.add(0);
    }
    Object l = new ArrayList<String>();
    ArrayList<Integer> j = (ArrayList<Integer>)l;
}
