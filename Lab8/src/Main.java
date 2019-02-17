import java.io.IOException;
import java.nio.file.*;

class MyInt extends Number implements Comparable<MyInt> {
    int value;
    public MyInt(int value) {
        this.value = value;
    }
    public int compareTo(MyInt c) {
        return value-c.value;
    }
    public int intValue() {
        return value;
    }
    public long longValue() {
        return (long)value;
    }
    public float floatValue() {
        return (float)value;
    }
    public double doubleValue() {
        return (double)value;
    }
    public String toString() {
        return String.valueOf(value);
    }
}

public class Main {
    public static void main(String[] args) {
        String str = "";
        try {
            str = new String(Files.readAllBytes(Paths.get("input.txt")));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        MyTree<Integer> tree = MyTree.parseMyTreeInt(str);
        System.out.print("Inorder: ");
        tree.printInorder();
        System.out.print("\nPreorder: ");
        tree.printPreorder();
        System.out.print("\nPostorder: ");
        tree.printPostorder();
        System.out.println("\nIs 2 present: " + tree.isPresent(2));
        //tree.removeElement(-2);
        tree.removeElement(5);
        tree.removeElement(-2);
        System.out.print("After removing some elements, preorder: ");
        tree.printPreorder();

        System.out.print("\nCustom class tree: ");
        MyTree<MyInt> tree1 = new MyTree<>();
        tree1.addElement(new MyInt(4));
        tree1.addElement(new MyInt(6));
        tree1.addElement(new MyInt(-5));
        tree1.printInorder();
    }
}
