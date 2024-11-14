package teacher;

import group.Group;
import module.Module;

import java.util.ArrayList;
import java.util.List;

public class TeacherService implements TeacherRepository {
    List<Teacher> listTeachers;

    public TeacherService() {
        this.listTeachers = new ArrayList<>();
    }

    @Override
    public void saveTeacher(Integer id, String fullName, Grade grade,
                            List<Module> listModules, List<Group> listGroup) {
        // Validate ID
        if (id == null) {
            throw new IllegalArgumentException("Teacher ID cannot be null");
        }
        if (id < 0) {
            throw new IllegalArgumentException("Teacher ID cannot be negative");
        }

        // Check for duplicate ID
        for (Teacher teacher : listTeachers) {
            if (teacher.getId().equals(id)) {
                throw new IllegalArgumentException("Teacher with ID " + id + " already exists");
            }
        }

        // Validate name
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Teacher name cannot be null or empty");
        }

        // Validate grade
        if (grade == null) {
            throw new IllegalArgumentException("Grade cannot be null");
        }

        // Initialize empty lists if null
        List<Module> modules = listModules != null ? listModules : new ArrayList<>();
        List<Group> groups = listGroup != null ? listGroup : new ArrayList<>();

        Teacher teacher = new Teacher(id, fullName, grade, modules, groups);
        listTeachers.add(teacher);
    }

    @Override
    public List<Teacher> allTeachers() {
        return new ArrayList<>(listTeachers); // Return a copy to prevent modification
    }

    @Override
    public void deleteTeacher(Integer id) {
        // Validate ID
        if (id == null) {
            throw new IllegalArgumentException("Teacher ID cannot be null");
        }
        if (id < 0) {
            throw new IllegalArgumentException("Teacher ID cannot be negative");
        }

        boolean found = false;
        for (int i = 0; i < listTeachers.size(); i++) {
            if (listTeachers.get(i).getId().equals(id)) {
                listTeachers.remove(i);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Teacher with ID " + id + " not found");
        }
    }
}