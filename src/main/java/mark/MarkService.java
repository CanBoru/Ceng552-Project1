package mark;

import mark.exception.MarkException;
import student.Student;
import module.Module;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MarkService implements MarkRepository {
    List<Mark> listMarks;

    public MarkService() {
        this.listMarks = new ArrayList();
    }

    @Override
    public void createMark(Student student, Integer mark, Module module) {
        // Check for null parameters first
        if (student == null || mark == null || module == null) {
            throw new MarkException("All parameters are required. Student, mark, and module cannot be null");
        }

        // Then check if mark is valid
        if (!isValid(mark)) {
            throw new MarkException("invalid mark , it should be between 5 and 20");
        }

        Mark markObj = new Mark(student, mark, module);
        listMarks.add(markObj);
    }

    private boolean isValid(Integer mark) {
        return mark >= 5 && mark <= 20;
    }

    @Override
    public List<Mark> allMarks() {
        return new ArrayList<>(listMarks); // Return a copy instead of the original list
    }

    @Override
    public List<Mark> findMarkByModule(Module module) {
        if (module == null) {
            throw new IllegalArgumentException("Module cannot be null");
        }
        return listMarks.stream()
                .filter(m -> m.getModule().getReference()
                        .toString().equalsIgnoreCase(module.getReference().toString()))
                .collect(Collectors.toList());
    }
    @Override
    public Student bestMarkByModule(Module module) {
        if (module == null) {
            throw new IllegalArgumentException("Module cannot be null");
        }

        Integer val = 0;
        Integer index = null;

        for (int i = 0; i < listMarks.size(); i++) {
            if (listMarks.get(i).getMark() > val
                    && listMarks.get(i).getModule()
                    .getReference().toString()
                    .equalsIgnoreCase(module.getReference().toString())) {
                val = listMarks.get(i).getMark();
                index = i;
            }
        }

        return index != null ? listMarks.get(index).getStudent() : null;
    }
}
