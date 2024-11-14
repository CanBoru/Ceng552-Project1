package student;

import group.Group;
import java.time.LocalDate;
import java.util.List;
public interface StudentRepository {
    void saveStudent(Integer id,String fullName, LocalDate dateOfBirth, Group group);
    void deleteStudent(Integer idStudent);
    Student findById(Integer id);
    void updateStudent(Integer id,String fullName, LocalDate dateOfBirth, Group group);
    List<Student> allStudents();
}
