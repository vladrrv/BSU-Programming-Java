package mypackage;

import java.util.HashSet;
import java.util.Set;

public class Course {
    private String name;
    private Set<Student> students;

    public Course(String name, Set<Student> students) {
        this.name = name;
        this.students = students;
    }

    Set<Postgraduate> getPostgraduates(String nameOfSupervisor) {
        Set<Postgraduate> postgraduates = new HashSet<>();
        for (Student student : students) {
            if (student instanceof Postgraduate && ((Postgraduate) student).getSupervisor().getName().equals(nameOfSupervisor)) {
                postgraduates.add((Postgraduate) student);
            }
        }
        return postgraduates;
    }
}
