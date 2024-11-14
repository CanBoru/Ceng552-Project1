package mark;

import student.Student;
import module.Module;
public class Mark {
    private Student student;
    private Integer mark;
    private Module module;

    public Mark(Student student, Integer mark, Module module) {
        this.student = student;
        this.mark = mark;
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public Student getStudent() {
        return student;
    }

    public Integer getMark() {
        return mark;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "student=" + student +
                ", mark=" + mark +
                ", module=" + module +
                '}';
    }
}
