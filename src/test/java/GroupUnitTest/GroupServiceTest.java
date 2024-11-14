package GroupUnitTest;

import group.GroupName;
import group.Group;
import group.GroupService;
import org.junit.Before;
import org.junit.Test;
import student.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class GroupServiceTest {
    private GroupService groupService;
    private static final LocalDate TEST_DATE = LocalDate.of(1999, 1, 1);
    private static final LocalDate TEST_DATE_2 = LocalDate.of(1999, 1, 2);
    private static final LocalDate TEST_DATE_3 = LocalDate.of(1999, 1, 3);

    @Before
    public void setUp() {
        groupService = new GroupService();
    }

    // Helper methods
    private Student createTestStudent(int id, String name, Group group) {
        return new Student(id, name, TEST_DATE, group);
    }

    private List<Student> createTestStudents(Group group, int count) {
        List<Student> students = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            students.add(createTestStudent(i, "Student" + i, group));
        }
        return students;
    }

    // ============= SaveGroup Tests (Equivalence Class Testing) =============

    @Test
    public void testSaveGroup() {
        groupService.saveGroup(GroupName.MSIR);
        assertEquals(1, groupService.allGroups().size());
        assertEquals(GroupName.MSIR, groupService.allGroups().get(0).getReference());
    }

    @Test
    public void testSaveMultipleGroups() {
        groupService.saveGroup(GroupName.MSIR);
        groupService.saveGroup(GroupName.MIAD);
        groupService.saveGroup(GroupName.MSIA);
        assertEquals(3, groupService.allGroups().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveGroup_NullReference() {
        groupService.saveGroup(null);
    }

    @Test
    public void testSaveGroup_DuplicateGroup() {
        groupService.saveGroup(GroupName.MSIR);
        groupService.saveGroup(GroupName.MSIR);
        assertEquals(2, groupService.allGroups().size());
    }

    // ============= AllGroups Tests (Equivalence Class Testing) =============

    @Test
    public void testAllGroups_Empty() {
        assertTrue(groupService.allGroups().isEmpty());
    }

    @Test
    public void testAllGroups_NonEmpty() {
        groupService.saveGroup(GroupName.MSIR);
        assertFalse(groupService.allGroups().isEmpty());
        assertEquals(1, groupService.allGroups().size());
    }

    @Test
    public void testAllGroups_ModificationAttempt() {
        groupService.saveGroup(GroupName.MSIR);
        List<Group> groups = groupService.allGroups();
        groups.clear(); // Should not affect internal list
        assertEquals(1, groupService.allGroups().size());
    }

    // ============= FindByReference Tests (Equivalence Class Testing) =============

    @Test
    public void testFindByReference_Existing() {
        groupService.saveGroup(GroupName.MSIR);
        Group found = groupService.findByReference("MSIR");
        assertNotNull(found);
        assertEquals(GroupName.MSIR, found.getReference());
    }

    @Test
    public void testFindByReference_NonExisting() {
        assertNull(groupService.findByReference("UNKNOWN"));
    }

    @Test
    public void testFindByReference_CaseInsensitive() {
        groupService.saveGroup(GroupName.MSIR);
        assertNotNull(groupService.findByReference("msir"));
        assertNotNull(groupService.findByReference("Msir"));
        assertNotNull(groupService.findByReference("MSIR"));
    }

    @Test
    public void testFindByReference_NullInput() {
        assertNull(groupService.findByReference(null));
    }

    @Test
    public void testFindByReference_EmptyString() {
        assertNull(groupService.findByReference(""));
    }

    @Test
    public void testFindByReference_WhitespaceOnly() {
        assertNull(groupService.findByReference("   "));
    }

    // ============= UpdateNumberOfStudent Tests (Decision Table Testing) =============

    @Test
    public void testUpdateNumberOfStudent_SingleGroup() {
        // Setup
        Group msirGroup = new Group(GroupName.MSIR);
        groupService.saveGroup(GroupName.MSIR);

        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Berat", TEST_DATE, msirGroup));
        students.add(new Student(2, "Siddik", TEST_DATE_2, msirGroup));
        students.add(new Student(3, "Nurlan", TEST_DATE_3, msirGroup));

        // Test
        groupService.updateNumberOfStudent(students);

        // Verify
        assertEquals(Integer.valueOf(3),
                groupService.findByReference("MSIR").getNumberStudent());
    }

    @Test
    public void testUpdateNumberOfStudent_MultipleGroups() {
        // Setup
        Group msirGroup = new Group(GroupName.MSIR);
        Group miadGroup = new Group(GroupName.MIAD);
        groupService.saveGroup(GroupName.MSIR);
        groupService.saveGroup(GroupName.MIAD);

        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Berat", TEST_DATE, msirGroup));
        students.add(new Student(2, "Siddik", TEST_DATE_2, miadGroup));
        students.add(new Student(3, "Nurlan", TEST_DATE_3, msirGroup));

        // Test
        groupService.updateNumberOfStudent(students);

        // Verify
        assertEquals(Integer.valueOf(2),
                groupService.findByReference("MSIR").getNumberStudent());
        assertEquals(Integer.valueOf(1),
                groupService.findByReference("MIAD").getNumberStudent());
    }

    @Test
    public void testUpdateNumberOfStudent_EmptyStudentList() {
        groupService.saveGroup(GroupName.MSIR);
        groupService.updateNumberOfStudent(new ArrayList<>());
        assertEquals(Integer.valueOf(0),
                groupService.findByReference("MSIR").getNumberStudent());
    }

    @Test
    public void testUpdateNumberOfStudent_NullList() {
        groupService.saveGroup(GroupName.MSIR);
        groupService.updateNumberOfStudent(null);
        assertEquals(Integer.valueOf(0),
                groupService.findByReference("MSIR").getNumberStudent());
    }

    @Test
    public void testUpdateNumberOfStudent_EmptyGroupList() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Test", TEST_DATE, new Group(GroupName.MSIR)));
        groupService.updateNumberOfStudent(students); // Should handle case when no groups exist
    }

    @Test
    public void testUpdateNumberOfStudent_StudentWithNonExistentGroup() {
        groupService.saveGroup(GroupName.MSIR);
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Test", TEST_DATE, new Group(GroupName.MIAD)));
        groupService.updateNumberOfStudent(students);
        assertEquals(Integer.valueOf(0),
                groupService.findByReference("MSIR").getNumberStudent());
    }

    @Test
    public void testUpdateNumberOfStudent_StudentWithNullGroup() {
        groupService.saveGroup(GroupName.MSIR);
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Test", TEST_DATE, null));
        groupService.updateNumberOfStudent(students);
        assertEquals(Integer.valueOf(0),
                groupService.findByReference("MSIR").getNumberStudent());
    }

    @Test
    public void testUpdateNumberOfStudent_LargeStudentList() {
        Group msirGroup = new Group(GroupName.MSIR);
        groupService.saveGroup(GroupName.MSIR);
        List<Student> students = createTestStudents(msirGroup, 1000);

        groupService.updateNumberOfStudent(students);

        assertEquals(Integer.valueOf(1000),
                groupService.findByReference("MSIR").getNumberStudent());
    }

    // ============= Group Display Tests =============

    @Test
    public void testGroupDisplay() {
        Group group = new Group(GroupName.MSIR);
        groupService.saveGroup(GroupName.MSIR);
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Berat", TEST_DATE, group));
        groupService.updateNumberOfStudent(students);

        String expected = "Group{reference=MSIR , number student=1}";
        assertEquals(expected, groupService.findByReference("MSIR").showGroup());
    }

    @Test
    public void testGroupDisplay_NoStudents() {
        groupService.saveGroup(GroupName.MSIR);
        String expected = "Group{reference=MSIR , number student=0}";
        assertEquals(expected, groupService.findByReference("MSIR").showGroup());
    }
    
    @Test
    public void testGroupToString() {
        Group group = new Group(GroupName.MSIR);
        String expected = "Group{reference=MSIR}";
        assertEquals(expected, group.toString());
    }
}