package group;

import student.Student;

import java.util.ArrayList;
import java.util.List;

public class GroupService implements GroupRepository {
    List<Group> listGroups;

    public GroupService() {
        this.listGroups = new ArrayList<>();
    }

    @Override
    public void saveGroup(GroupName reference) {
        if (reference == null) {
            throw new IllegalArgumentException("Group reference cannot be null");
        }
        Group group = new Group(reference);
        listGroups.add(group);
    }

    @Override
    public List<Group> allGroups() {
        return new ArrayList<>(listGroups); // Return a copy of the list
    }

    @Override
    public Group findByReference(String reference) {
        Integer index = findIndex(reference);
        if (index != null)
            return listGroups.get(index);
        else return null;
    }

    @Override
    public void updateNumberOfStudent(List<Student> listStudents) {
        // If listStudents is null, set all groups' student count to 0
        if (listStudents == null) {
            for (Group group : listGroups) {
                group.setNumberStudent(0);
            }
            return;
        }

        // Original logic for non-null list
        for (int i = 0; i < listGroups.size(); i++) {
            int count = 0;
            String ref = listGroups.get(i).getReference().toString();
            Integer index = findIndex(ref);
            for (Student student : listStudents) {
                // Check if student's group is not null before accessing
                if (student.getGroup() != null &&
                        ref.equalsIgnoreCase(student.getGroup().getReference().toString())) {
                    count++;
                }
            }
            listGroups.get(index).setNumberStudent(count);
        }
    }

    private Integer findIndex(String reference) {
        for (int i = 0; i < listGroups.size(); i++) {
            if (listGroups.get(i).getReference().toString().equalsIgnoreCase(reference))
                return i;
        }
        return null;
    }

}
