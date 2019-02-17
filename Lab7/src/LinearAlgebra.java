import java.io.IOException;
import java.util.*;
import java.nio.file.*;

class SquareMatrix {
    private Vector<Double> body;
    private int size;
    SquareMatrix() {
        size = 0;
        body = new Vector<>(1);
    }
    SquareMatrix(int n, boolean isUnit) {
        size = n;
        body = new Vector<>(n*n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j && isUnit) {
                    body.add(1.0);
                }
                else {
                    body.add(0.0);
                }
            }
        }
    }
    SquareMatrix(Double[][] array) {
        size = array.length;
        body = new Vector<>(size*size);
        for (Double[] r : array) {
            for (Double el : r) {
                body.add(el);
            }
        }
    }
    SquareMatrix(double[][] array) {
        size = array.length;
        body = new Vector<>(size*size);
        for (double[] r : array) {
            for (Double el : r) {
                body.add(el);
            }
        }
    }
    SquareMatrix(Vector<Double> v) {
        size = (int)Math.sqrt(v.size());
        body = new Vector<>(v);
    }
    SquareMatrix(SquareMatrix m) {
        size = m.size;
        body = new Vector<>(m.body);
    }
    SquareMatrix add(SquareMatrix m) {
        if (size == m.size) {
            Vector<Double> sum = new Vector<>(body);
            for (int i = 0; i < sum.size(); i++) {
                Double s = sum.get(i);
                s += m.body.get(i);
                sum.set(i, s);
            }
            return new SquareMatrix(sum);
        }
        else throw new ArithmeticException();
    }
    SquareMatrix mul(SquareMatrix m) {
        if (size == m.size) {
            SquareMatrix mlt = new SquareMatrix(body);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    Double s = 0.0;
                    for (int k = 0; k < size; k++) {
                        s += get(i, k)*m.get(k, j);
                    }
                    mlt.set(i, j, s);
                }
            }
            return mlt;
        }
        else throw new ArithmeticException();
    }
    SquareMatrix pow(int n) {
        if (n == 1) {
            return new SquareMatrix(this);
        }
        if (n == 0) {
            return new SquareMatrix(size, true);
        }
        return mul(pow(n-1));
    }
    SquareMatrix inverseMatrix() {
        SquareMatrix invertedMatrix = new SquareMatrix(body);
        Double det = determinant();
        if (det.equals(0.0)) throw new ArithmeticException("No inverted matrix exist! (determinant = 0)");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Double aij = algCompl(j, i) / det;
                invertedMatrix.set(i, j, aij);
            }
        }
        return new SquareMatrix(invertedMatrix);
    }
    SquareMatrix replaceColumnAt(Vector<Double> b, int j) {
        SquareMatrix D = new SquareMatrix(this);
        for (int i = 0; i < size; i++) {
            D.set(i, j, b.get(i));
        }
        return D;
    }
    Double algCompl(int i0, int j0) {
        SquareMatrix submatrix = new SquareMatrix();
        for (int i = 0; i < size; i++) {
            if (i != i0) {
                for (int j = 0; j < size; j++) {
                    if (j != j0) {
                        Double aij = get(i, j);
                        submatrix.body.add(aij);
                    }
                }
            }
        }
        submatrix.size = size-1;
        return submatrix.determinant()*((i0+j0)%2==0? 1: -1);
    }
    Double determinant() {
        Double det = 0.0;
        if (size == 1) {
            return get(0, 0);
        }
        for (int j = 0; j < size; j++) {
            det += get(0, j)*algCompl(0, j);
        }
        return det;
    }
    void transpose() {
        Vector<Double> transposedMatrix = new Vector<>(size*size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Double aij = get(j, i);
                transposedMatrix.add(aij);
            }
        }
        body = transposedMatrix;
    }
    void set(int i, int j, Double val) {
        body.set(i*size + j, val);
    }
    Double get(int i, int j) {
        return body.get(i*size + j);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sb.append(get(i, j));
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    public static SquareMatrix parseSquareMatrix(String str) throws IOException {
        StringTokenizer st = new StringTokenizer(str, "\n");
        String rowStr0 = st.nextToken();
        double[] row0 = Arrays.stream(rowStr0.split(" ")).map(String::trim).mapToDouble(Double::parseDouble).toArray();
        int sz = row0.length;
        double[][] src = new double[sz][];
        src[0] = row0;
        for (int i = 1; st.hasMoreTokens(); i++) {
            String rowStr = st.nextToken();
            double[] row = Arrays.stream(rowStr.split(" ")).map(String::trim).mapToDouble(Double::parseDouble).toArray();
            if (row.length != sz) throw new IOException("Incorrect input!");
            src[i] = row;
        }
        return new SquareMatrix(src);
    }
    int getDim() {
        return size;
    }
    Set<Integer> minInRows() {
        Double min = get(0,0);
        Set<Integer> v = new HashSet<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Double aij = get(i, j);
                if (aij < min) {
                    v.clear();
                    v.add(i);
                    min = aij;
                }
                else if (aij.equals(min)) {
                    v.add(i);
                }
            }
        }
        return v;
    }
    Set<Integer> maxInCols() {
        Double max = get(0,0);
        Set<Integer> v = new HashSet<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Double aij = get(i, j);
                if (aij > max) {
                    v.clear();
                    v.add(j);
                    max = aij;
                }
                else if (aij.equals(max)) {
                    v.add(j);
                }
            }
        }
        return v;
    }
    Vector<Double> getRow(int i) {
        Vector<Double> v = new Vector<>();
        for (int j = 0; j < size; j++) {
            Double aj = get(i, j);
            v.add(aj);
        }
        return v;
    }
    Vector<Double> getCol(int j) {
        Vector<Double> v = new Vector<>();
        for (int i = 0; i < size; i++) {
            Double aj = get(i, j);
            v.add(aj);
        }
        return v;
    }
    Double getScalarProd(int rowNum, int colNum) {
        Vector<Double> row = getRow(rowNum);
        Vector<Double> col = getCol(colNum);
        Double prod = 0.0;
        for (int i = 0; i < row.size(); i++) {
            prod += row.get(i)*col.get(i);
        }
        return prod;
    }
    boolean isUpperTriangle() {
        for (int i = 1; i < size; i++) {
            for (int j = 0; j < i; j++) {
                if (!get(i, j).equals(0.0)) {
                    return false;
                }
            }
        }
        return true;
    }
}

abstract class Solver {
    protected SquareMatrix A;
    protected Vector<Double> b;
    protected Vector<Double> x;
    abstract Vector<Double> solve();
    void printSolution() {
        for (int i = 0; i < x.size(); i++)
            System.out.println("x"+(i+1)+" = "+x.get(i));
    }
    Solver(Double[][] coeffs, Double[] b) {
        A = new SquareMatrix(coeffs);
        this.b = new Vector<>(Arrays.asList(b));
        x = new Vector<>(b.length);
    }
}
class TriangleSolver extends Solver {
    TriangleSolver(Double[][] coeffs, Double[] b) {
        super(coeffs, b);
    }
    Vector<Double> solve() {
        if (!A.isUpperTriangle()) throw new ArithmeticException("Matrix is not upper-triangle!");
        int n = b.size();
        for (int i = 0; i < n; i++) {
            x.add(0.0);
        }
        x.set(n-1, b.lastElement()/A.get(n-1,n-1));
        for (int i = n-2; i >= 0; i--) {
            Double xi = b.elementAt(i);
            for (int j = i+1; j < n; j++) {
                xi -= x.get(j)*A.get(i, j);
            }
            xi = xi/A.get(i, i);
            x.set(i, xi);
        }
        return x;
    }
}
class NondegenSolver extends Solver {
    NondegenSolver(Double[][] coeffs, Double[] b) {
        super(coeffs, b);
    }
    Vector<Double> solve() {
        int n = b.size();
        Double det = A.determinant();
        for (int i = 0; i < n; i++) {
            Double xi = A.replaceColumnAt(b, i).determinant()/det;
            x.add(xi);
        }
        return x;
    }
}

public class LinearAlgebra {
    public static void main(String[] args) {
        try {
    /*-----------------------------------------------------------------------*/
            TriangleSolver task1 = new TriangleSolver(new Double[][]{
                    {1.0, 2.0, 3.0},
                    {0.0, 2.0, 4.0},
                    {0.0, 0.0, 1.0}
            }, new Double[]{
                    0.0,
                    1.0,
                    2.0
            });
            task1.solve();
            task1.printSolution();
    /*-----------------------------------------------------------------------*/
            SquareMatrix B = new SquareMatrix(new Double[][]{
                    {1.0, 1.0, 2.0},
                    {1.0, 0.0, 4.0},
                    {2.0, 4.0, 1.0}
            });
            System.out.println("\nB = \n" + B);
            System.out.println("B^(-1) = \n" + B.inverseMatrix());
    /*-----------------------------------------------------------------------*/
            NondegenSolver task3 = new NondegenSolver(new Double[][]{
                    {1.0, 2.0, 3.0},
                    {4.0, 2.0, 4.0},
                    {4.0, 4.0, 1.0}
            }, new Double[]{
                    5.0,
                    1.0,
                    2.0
            });
            task3.solve();
            task3.printSolution();
    /*-----------------------------------------------------------------------*/
            SquareMatrix C = B.pow(3);
            System.out.println("\nB^3 = \n" + C);
    /*-----------------------------------------------------------------------*/
            String str = new String(Files.readAllBytes(Paths.get("input.txt")));
            SquareMatrix D = SquareMatrix.parseSquareMatrix(str);
            System.out.println(D);
            Set<Integer> r = D.minInRows();
            Set<Integer> c = D.maxInCols();
            for (int i : r) {
                for (int j : c) {
                    System.out.println("Row " + i + " * column " + j + " = " + D.getScalarProd(i, j));
                }
            }
    /*-----------------------------------------------------------------------*/
        }
        catch (NumberFormatException e) {
            System.out.println("Incorrect input format!");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
