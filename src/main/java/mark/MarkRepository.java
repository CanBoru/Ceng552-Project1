package mark;

import student.Student;
import java.util.List;
import module.Module;
public interface MarkRepository {
    //give a mark for a student in a specific module
    void createMark(Student student,Integer mark,Module module);
    List<Mark> allMarks();
    // get all marks for a specific module
    List<Mark> findMarkByModule(Module module);
    Student bestMarkByModule(Module module);
}
