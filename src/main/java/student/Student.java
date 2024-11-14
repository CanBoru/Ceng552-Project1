package student;
import group.Group;
import java.time.LocalDate;


public class Student {
    private Integer id;
    private String fullName;
    private LocalDate dateBirth;
    private Group group;


    public Student(Integer id,String fullName, LocalDate dateBirth, Group group) {
        this.id=id;
        this.fullName = fullName;
        this.dateBirth = dateBirth;
        this.group = group;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDateBirth(LocalDate dateBirth) {
        this.dateBirth = dateBirth;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getDateBirth() {
        return dateBirth;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", dateBirth=" + dateBirth +
                ", group=" + group +
                '}';
    }

}
