package StudentUnitTest;

import group.Group;
import group.GroupName;
import org.junit.Before;
import org.junit.Test;
import student.Student;
import student.StudentService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class StudentServiceTest {
    private StudentService studentService;
    private Group group1;
    private Group group2;
    private LocalDate date1;
    private LocalDate date2;

    // Constants for testing
    private static final String VALID_NAME = "John Doe";
    private static final String LONG_NAME = "This is a very long name that might exceed any reasonable length limit for a student name";
    private static final Integer VALID_ID = 1;
    private static final LocalDate FUTURE_DATE = LocalDate.now().plusYears(1);
    private static final LocalDate BEFORE_DATE = LocalDate.now().minusYears(100);

    @Before
    public void setUp() {
        studentService = new StudentService();
        group1 = new Group(GroupName.MSIR);
        group2 = new Group(GroupName.MIAD);
        date1 = LocalDate.of(2000, 1, 1);
        date2 = LocalDate.of(2001, 2, 2);
        
    }

    // ============= SaveStudent Tests (Decision Table Testing) =============

    @Test
    public void testSaveStudent_SingleStudent() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, group1);

        assertEquals(1, studentService.allStudents().size());
        Student saved = studentService.findById(VALID_ID);
        assertNotNull(saved);
        assertEquals(VALID_NAME, saved.getFullName());
        assertEquals(date1, saved.getDateBirth());
        assertEquals(group1, saved.getGroup());
    }

    @Test
    public void testSaveStudent_MultipleStudents() {
        studentService.saveStudent(1, "John Doe", date1, group1);
        studentService.saveStudent(2, "Jane Doe", date2, group2);
        assertEquals(2, studentService.allStudents().size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSaveStudent_NegativeId() {
    	studentService.saveStudent(-1, VALID_NAME, date1, group1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStudent_NullId() {
        studentService.saveStudent(null, VALID_NAME, date1, group1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStudent_EmptyName() {
        studentService.saveStudent(VALID_ID, "", date1, group1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStudent_WhitespaceName() {
        studentService.saveStudent(VALID_ID, "   ", date1, group1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStudent_NullName() {
        studentService.saveStudent(VALID_ID, null, date1, group1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStudent_FutureDate() {
        studentService.saveStudent(VALID_ID, VALID_NAME, FUTURE_DATE, group1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStudent_NullDate() {
        studentService.saveStudent(VALID_ID, VALID_NAME, null, group1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveStudent_NullGroup() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, null);
    }

    // ============= DeleteStudent Tests (Equivalence Class Testing) =============

    @Test
    public void testDeleteStudent_ExistingStudent() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, group1);
        studentService.deleteStudent(VALID_ID);
        assertTrue(studentService.allStudents().isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDeleteStudent_NonExistingStudent() {
        studentService.deleteStudent(999);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteStudent_NullId() {
        studentService.deleteStudent(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteStudent_NegativeId() {
        studentService.deleteStudent(-1);
    }

    // ============= FindById Tests (Equivalence Class & Boundary Testing) =============

    @Test
    public void testFindById_ExistingStudent() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, group1);
        Student found = studentService.findById(VALID_ID);
        assertNotNull(found);
        assertEquals(VALID_NAME, found.getFullName());
    }

    @Test
    public void testFindById_FirstElement() {
        studentService.saveStudent(1, "First", date1, group1);
        studentService.saveStudent(2, "Second", date1, group1);
        assertNotNull(studentService.findById(1));
    }

    @Test
    public void testFindById_LastElement() {
        studentService.saveStudent(1, "First", date1, group1);
        studentService.saveStudent(2, "Last", date1, group1);
        assertNotNull(studentService.findById(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindById_NonExistingStudent() {
        studentService.findById(999);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindById_NullId() {
        studentService.findById(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindById_NegativeId() {
        studentService.findById(-5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindIndexById_ExistingStudent() {
        studentService.saveStudent(1, "John Doe", date1, group1); // Adds student
        Integer index = studentService.findIndexById(1);
        assertNotNull(index); // Checks that index is returned
        assertEquals(0, index.intValue()); // Index should be 0
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindIndexById_NonExistingStudent() {
        studentService.saveStudent(1, "John Doe", date1, group1); // Adds student
        Integer index = studentService.findIndexById(999); // Non-existing ID
        assertNull(index); // Should return null for non-existing ID
    }
    
    // ============= UpdateStudent Tests (Decision Table Testing) =============

    @Test
    public void testUpdateStudent_AllFields() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, group1);
        studentService.updateStudent(VALID_ID, "Jane Doe", date2, group2);

        Student updated = studentService.findById(VALID_ID);
        assertEquals("Jane Doe", updated.getFullName());
        assertEquals(date2, updated.getDateBirth());
        assertEquals(group2, updated.getGroup());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStudent_EmptyName() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, group1);
        studentService.updateStudent(VALID_ID, "", date1, group1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStudent_WhitespaceName() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, group1);
        studentService.updateStudent(VALID_ID, "  ", date1, group1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStudent_NullName() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, group1);
        studentService.updateStudent(VALID_ID, null, date1, group1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStudent_NullID() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, group1);
        studentService.updateStudent(null, VALID_NAME, date1, group1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStudent_NullDate() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, group1);
        studentService.updateStudent(VALID_ID, VALID_NAME, null, group1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStudent_FutureDate() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, group1);
        studentService.updateStudent(VALID_ID, VALID_NAME, FUTURE_DATE, group1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStudent_BeforeDate() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, group1);
        studentService.updateStudent(VALID_ID, VALID_NAME, BEFORE_DATE, group1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStudent_NullGroup() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, group1);
        studentService.updateStudent(VALID_ID, VALID_NAME, date1, null);
    }

    // ============= AllStudents Tests (Equivalence Class Testing) =============

    @Test
    public void testAllStudents_EmptyList() {
        List<Student> students = studentService.allStudents();
        assertNotNull(students);
        assertTrue(students.isEmpty());
    }

    @Test
    public void testAllStudents_SingleStudent() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, group1);
        assertEquals(1, studentService.allStudents().size());
    }

    @Test
    public void testAllStudents_MultipleStudents() {
        studentService.saveStudent(1, "John", date1, group1);
        studentService.saveStudent(2, "Jane", date2, group2);
        assertEquals(2, studentService.allStudents().size());
    }

    @Test
    public void testAllStudents_ModificationAttempt() {
        studentService.saveStudent(VALID_ID, VALID_NAME, date1, group1);
        List<Student> students = studentService.allStudents();
        students.clear(); // Should not affect internal list
        assertEquals(1, studentService.allStudents().size());
    }

    // ============= Additional Edge Cases =============

    @Test
    public void testSaveStudent_LongName() {
        studentService.saveStudent(VALID_ID, LONG_NAME, date1, group1);
        Student saved = studentService.findById(VALID_ID);
        assertEquals(LONG_NAME, saved.getFullName());
    }

    @Test
    public void testAgeValidation() {
        LocalDate currentDate = LocalDate.now();
        LocalDate validAge = currentDate.minusYears(20);
        studentService.saveStudent(VALID_ID, VALID_NAME, validAge, group1);

        Student saved = studentService.findById(VALID_ID);
        assertEquals(validAge, saved.getDateBirth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAgeValidation_NegativeValue() {
        LocalDate tooYoung = LocalDate.now().minusYears(100);
        studentService.saveStudent(VALID_ID, VALID_NAME, tooYoung, group1);
    }

    @Test
    public void testIdManagement_LargeDataset() {
        for (int i = 0; i < 100; i++) {
            studentService.saveStudent(i, "Student " + i, date1, group1);
        }
        assertEquals(100, studentService.allStudents().size());
    }
    
    @Test
    public void testStudentToString() {
        // Create test data
        Integer id = 1;
        String fullName = "John Doe";
        LocalDate dateBirth = LocalDate.of(2000, 1, 1);
        Group group = new Group(GroupName.MSIR);
        
        // Create student
        Student student = new Student(id, fullName, dateBirth, group);
        
        // Expected string format based on Student.java toString() implementation
        String expected = "Student{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", dateBirth=" + dateBirth +
                ", group=" + group +
                '}';
                
        assertEquals(expected, student.toString());
    }

}