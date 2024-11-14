package TeacherUnitTest;

import group.Group;
import group.GroupName;
import module.Module;
import module.ModuleName;
import org.junit.Before;
import org.junit.Test;
import teacher.TeacherService;
import teacher.Grade;
import teacher.Teacher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class TeacherServiceTest {
    private TeacherService teacherService;
    private List<Module> modules1;
    private List<Module> modules2;
    private List<Group> groups1;
    private List<Group> groups2;

    // Constants for testing
    private static final String VALID_NAME = "John Doe";
    private static final String LONG_NAME = "This is a very long name that might exceed any reasonable length limit";
    private static final Integer VALID_ID = 1;

    @Before
    public void setUp() {
        teacherService = new TeacherService();

        // Setup test modules
        Module module1 = new Module(ModuleName.BDA, "Big Data Analytics", 30);
        Module module2 = new Module(ModuleName.CRY, "Cryptography", 24);
        modules1 = Arrays.asList(module1);
        modules2 = Arrays.asList(module1, module2);

        // Setup test groups
        Group group1 = new Group(GroupName.MSIR);
        Group group2 = new Group(GroupName.MIAD);
        groups1 = Arrays.asList(group1);
        groups2 = Arrays.asList(group1, group2);
    }

    // ============= SaveTeacher Tests (Decision Table Testing) =============

    @Test
    public void testSaveTeacher_SingleTeacher() {
        teacherService.saveTeacher(VALID_ID, VALID_NAME, Grade.MCA, modules1, groups1);

        assertEquals(1, teacherService.allTeachers().size());
        Teacher saved = teacherService.allTeachers().get(0);
        assertEquals(VALID_ID, saved.getId());
        assertEquals(VALID_NAME, saved.getFullName());
        assertEquals(Grade.MCA, saved.getGrade());
        assertEquals(modules1, saved.getListModules());
        assertEquals(groups1, saved.getListGroup());
    }

    @Test
    public void testSaveTeacher_MultipleTeachers() {
        teacherService.saveTeacher(1, "John Doe", Grade.MCA, modules1, groups1);
        teacherService.saveTeacher(2, "Jane Doe", Grade.MCB, modules2, groups2);
        assertEquals(2, teacherService.allTeachers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveTeacher_NullId() {
        teacherService.saveTeacher(null, VALID_NAME, Grade.MCA, modules1, groups1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveTeacher_NegativeId() {
        teacherService.saveTeacher(-1, VALID_NAME, Grade.MCA, modules1, groups1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveTeacher_NullName() {
        teacherService.saveTeacher(VALID_ID, null, Grade.MCA, modules1, groups1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveTeacher_EmptyName() {
        teacherService.saveTeacher(VALID_ID, "", Grade.MCA, modules1, groups1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveTeacher_WhitespaceName() {
        teacherService.saveTeacher(VALID_ID, "   ", Grade.MCA, modules1, groups1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveTeacher_NullGrade() {
        teacherService.saveTeacher(VALID_ID, VALID_NAME, null, modules1, groups1);
    }

    @Test
    public void testSaveTeacher_EmptyLists() {
        teacherService.saveTeacher(VALID_ID, VALID_NAME, Grade.MCA,
                new ArrayList<>(), new ArrayList<>());

        assertEquals(1, teacherService.allTeachers().size());
        Teacher saved = teacherService.allTeachers().get(0);
        assertTrue(saved.getListModules().isEmpty());
        assertTrue(saved.getListGroup().isEmpty());
    }

    @Test
    public void testSaveTeacher_NullLists() {
        teacherService.saveTeacher(VALID_ID, VALID_NAME, Grade.MCA, null, null);
        assertEquals(1, teacherService.allTeachers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveTeacher_DuplicateId() {
        teacherService.saveTeacher(VALID_ID, "John Doe", Grade.MCA, modules1, groups1);
        teacherService.saveTeacher(VALID_ID, "Jane Doe", Grade.MCB, modules2, groups2);
    }

    @Test
    public void testSaveTeacher_LongName() {
        teacherService.saveTeacher(VALID_ID, LONG_NAME, Grade.MCA, modules1, groups1);
        assertEquals(LONG_NAME, teacherService.allTeachers().get(0).getFullName());
    }

    // ============= AllTeachers Tests (Equivalence Class Testing) =============

    @Test
    public void testAllTeachers_EmptyList() {
        List<Teacher> teachers = teacherService.allTeachers();
        assertNotNull(teachers);
        assertTrue(teachers.isEmpty());
    }

    @Test
    public void testAllTeachers_SingleTeacher() {
        teacherService.saveTeacher(VALID_ID, VALID_NAME, Grade.MCA, modules1, groups1);
        assertEquals(1, teacherService.allTeachers().size());
    }

    @Test
    public void testAllTeachers_MultipleTeachers() {
        teacherService.saveTeacher(1, "John Doe", Grade.MCA, modules1, groups1);
        teacherService.saveTeacher(2, "Jane Doe", Grade.MCB, modules2, groups2);
        assertEquals(2, teacherService.allTeachers().size());
    }

    @Test
    public void testAllTeachers_ModificationAttempt() {
        teacherService.saveTeacher(VALID_ID, VALID_NAME, Grade.MCA, modules1, groups1);
        List<Teacher> originalSize = teacherService.allTeachers();

        List<Teacher> teachers = teacherService.allTeachers();
        teachers.clear(); // Try to modify the returned list

        assertEquals(originalSize.size(), teacherService.allTeachers().size());
    }

    // ============= DeleteTeacher Tests (Equivalence Class Testing) =============

    @Test
    public void testDeleteTeacher_ExistingTeacher() {
        teacherService.saveTeacher(VALID_ID, VALID_NAME, Grade.MCA, modules1, groups1);
        teacherService.deleteTeacher(VALID_ID);
        assertEquals(0, teacherService.allTeachers().size());
    }

    @Test
    public void testDeleteTeacher_FirstTeacher() {
        teacherService.saveTeacher(1, "First", Grade.MCA, modules1, groups1);
        teacherService.saveTeacher(2, "Second", Grade.MCB, modules1, groups1);
        teacherService.deleteTeacher(1);
        assertEquals(1, teacherService.allTeachers().size());
        assertEquals("Second", teacherService.allTeachers().get(0).getFullName());
    }

    @Test
    public void testDeleteTeacher_LastTeacher() {
        teacherService.saveTeacher(1, "First", Grade.MCA, modules1, groups1);
        teacherService.saveTeacher(2, "Last", Grade.MCB, modules1, groups1);
        teacherService.deleteTeacher(2);
        assertEquals(1, teacherService.allTeachers().size());
        assertEquals("First", teacherService.allTeachers().get(0).getFullName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteTeacher_NonExistingTeacher() {
        teacherService.deleteTeacher(999);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteTeacher_NullId() {
        teacherService.deleteTeacher(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteTeacher_NegativeId() {
        teacherService.deleteTeacher(-1);
    }

    // ============= Additional Test Cases =============

    @Test
    public void testTeacherGrades_AllValues() {
        teacherService.saveTeacher(1, "John Doe", Grade.MCA, modules1, groups1);
        teacherService.saveTeacher(2, "Jane Doe", Grade.MCB, modules2, groups2);

        List<Teacher> teachers = teacherService.allTeachers();
        assertEquals(2, teachers.size());
        assertEquals(Grade.MCA, teachers.get(0).getGrade());
        assertEquals(Grade.MCB, teachers.get(1).getGrade());
    }

    @Test
    public void testLargeDataset() {
        for (int i = 0; i < 100; i++) {
            teacherService.saveTeacher(i, "Teacher " + i, Grade.MCA, modules1, groups1);
        }
        assertEquals(100, teacherService.allTeachers().size());
    }
}