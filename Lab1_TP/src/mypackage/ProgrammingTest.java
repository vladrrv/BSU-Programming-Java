package mypackage;

import java.util.HashSet;
import java.util.Set;

public class ProgrammingTest {
    public static void main(String[] args) {
        Set<Student> students = new HashSet<>();
        Academic academic1 = new Academic("acad1");
        Academic academic2 = new Academic("acad2");
        students.add(new Undergraduate("A", "alogin", "aemail", academic1));
        students.add(new Undergraduate("B", "blogin", "bemail", academic1));
        students.add(new Postgraduate("C", "clogin", "cemail", academic2));
        students.add(new Postgraduate("D", "dlogin", "demail", academic1));
        students.add(new Postgraduate("E", "elogin", "eemail", academic1));
        students.add(new Postgraduate("E", "elogin", "eemail", academic1));

        Course course = new Course("FPMI", students);
        Notifier notifier = new Notifier(course.getPostgraduates("acad1"));
        notifier.doNotifyAll("not");
    }
}
