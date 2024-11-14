package group;

import student.Student;
import java.util.List;

public interface GroupRepository {
    void saveGroup(GroupName group);
    List<Group> allGroups();
    Group findByReference(String reference);
    void updateNumberOfStudent (List<Student>listStudents);
}
