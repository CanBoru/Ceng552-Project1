package student;

import group.Group;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentService implements StudentRepository {
    private List<Student> listStudent;

    public StudentService() {
        listStudent = new ArrayList<>();
    }

    @Override
    public void saveStudent(Integer id, String fullName, LocalDate dateOfBirth, Group group) {
        // Validate ID
        if (id == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        if (id < 0) {
            throw new IllegalArgumentException("Student ID cannot be negative");
        }

        // Validate name
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or empty");
        }

        // Validate date of birth
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }

        // Check if date is in the future
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }

        // Check if date is too far in the past (more than 100 years)
        if (dateOfBirth.isBefore(LocalDate.now().minusYears(80))) {
            throw new IllegalArgumentException("Date of birth cannot be more than 80 years ago");
        }

        // Validate group
        if (group == null) {
            throw new IllegalArgumentException("Student group cannot be null");
        }

        Student student = new Student(id, fullName, dateOfBirth, group);
        listStudent.add(student);
    }

    @Override
    public void deleteStudent(Integer idStudent) {
        if (idStudent == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        if (idStudent < 0) {
            throw new IllegalArgumentException("Student ID cannot be negative");
        }

        Integer index = findIndexById(idStudent);
        if (index == null) {
            throw new IndexOutOfBoundsException("Student with ID " + idStudent + " not found");
        }
        listStudent.remove(index.intValue());
    }

    @Override
    public Student findById(Integer id) {
    	if (id == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
    	
    	if (id < 0) {
            throw new IllegalArgumentException("Student ID cannot be negative");
        }
        for (Student student : listStudent) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        throw new IndexOutOfBoundsException("Student with ID " + id + " not found");
    }

    private Integer findIndexById(Integer id) {
        for (int i = 0; i < listStudent.size(); i++) {
            if (listStudent.get(i).getId().equals(id)) {
                return i;
            }
        }
        return null;
    }

    @Override
    public void updateStudent(Integer id, String fullName, LocalDate dateOfBirth, Group group) {
        // Validate ID
        if (id == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }

        // Validate name
        if (fullName == null) {
            throw new IllegalArgumentException("Student name cannot be null");
        }
        
        // Validate name
        if (fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }

        // Validate date of birth
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }

        // Check if date is in the future
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }

        // Check if date is too far in the past (more than 99 years)
        if (dateOfBirth.isBefore(LocalDate.now().minusYears(99))) {
            throw new IllegalArgumentException("Date of birth cannot be more than 99 years ago");
        }

        // Validate group
        if (group == null) {
            throw new IllegalArgumentException("Student group cannot be null");
        }

        Student student = findById(id);
        student.setGroup(group);
        student.setDateBirth(dateOfBirth);
        student.setFullName(fullName);

        Integer index = findIndexById(id);
        if (index != null) {
            listStudent.set(index, student);
        }
    }

    @Override
    public List<Student> allStudents() {
        return new ArrayList<>(listStudent);
    }
}