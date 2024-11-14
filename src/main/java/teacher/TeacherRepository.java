package teacher;

import group.Group;
import module.Module;
import java.util.List;

public interface TeacherRepository {
    void saveTeacher(Integer id, String fullName,
                     Grade grade, List<Module> listModules, List<Group> listGroup);
    List<Teacher>allTeachers();
    void  deleteTeacher(Integer id);
}
