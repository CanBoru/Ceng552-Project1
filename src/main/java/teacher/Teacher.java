package teacher;

import group.Group;
import module.Module;

import java.util.List;

public class Teacher {
    private Integer id;
    private String fullName;
    private Grade grade;
    private List<Module> listModules;
    private List<Group> listGroup;

    public Teacher(Integer id, String fullName,
                   Grade grade, List<Module> listModules, List<Group> listGroups) {
        this.id = id;
        this.fullName = fullName;
        this.grade = grade;
        this.listModules = listModules;
        this.listGroup = listGroups;
    }



    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", grade=" + grade +
                ", listModules=" + listModules +
                ", listGroup=" + listGroup +
                '}';
    }

    public String getFullName() {
        return fullName;
    }

    public Grade getGrade() {
        return grade;
    }

    public List<Module> getListModules() {
        return listModules;
    }

    public List<Group> getListGroup() {
        return listGroup;
    }

    public Integer getId() {
        return id;
    }
}
