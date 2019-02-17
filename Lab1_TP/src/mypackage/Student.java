package mypackage;

public class Student extends Person implements Notifiable {
    private String login;
    private String email;

    public Student(String name, String login, String email) {
        super(name);
        this.login = login;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }
    public String getEmail() {
        return email;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void notify(String message) {
        System.out.println("Student "+getName()+" was notified with message '"+message+"'.");
    }
}

class Academic extends Person {
    public Academic(String name) {
        super(name);
    }
}

class Postgraduate extends Student {
    private Academic supervisor;

    public Postgraduate(String name, String login, String email) {
        super(name, login, email);
    }
    public Postgraduate(String name, String login, String email, Academic supervisor) {
        super(name, login, email);
        this.supervisor = supervisor;
    }

    public Academic getSupervisor() {
        return supervisor;
    }
    public void setSupervisor(Academic supervisor) {
        this.supervisor = supervisor;
    }
}

class Undergraduate extends Student {
    private Academic tutor;

    public Undergraduate(String name, String login, String email) {
        super(name, login, email);
    }
    public Undergraduate(String name, String login, String email, Academic tutor) {
        super(name, login, email);
        this.tutor = tutor;
    }

    public Academic getTutor() {
        return tutor;
    }
    public void setTutor(Academic tutor) {
        this.tutor = tutor;
    }
}