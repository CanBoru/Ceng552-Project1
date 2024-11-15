package MarkUnitTest;

import group.Group;
import group.GroupName;
import mark.Mark;
import module.ModuleName;
import org.junit.Test;
import student.Student;
import module.Module;
import static org.junit.Assert.*;
import mark.exception.MarkException;
import org.junit.Before;
import java.time.LocalDate;
import java.util.List;
import mark.MarkService;

public class MarkServiceTest {
    private MarkService markService;
    private Student student1;
    private Student student2;
    private Module module1;
    private Module module2;

    // Constants for test values
    private static final int VALID_MARK = 15;
    private static final int MIN_MARK = 5;
    private static final int MAX_MARK = 20;
    private static final int INVALID_LOW_MARK = 4;
    private static final int INVALID_HIGH_MARK = 21;
    private static final int MIDDLE_MARK = 12;

    @Before
    public void setUp() {
        markService = new MarkService();
        Group msirGroup = new Group(GroupName.MSIR);
        LocalDate date = LocalDate.of(1999, 1, 1);
        LocalDate date2 = LocalDate.of(1999, 1, 2);

        student1 = new Student(1, "Berat", date, msirGroup);
        student2 = new Student(2, "Siddik", date2, msirGroup);
        module1 = new Module(ModuleName.BDA, "TestModule", 5);
        module2 = new Module(ModuleName.RI, "TestModule2", 2);
    }

    // Helper methods
    private void createMultipleMarks(int count, Module module) {
        for (int i = 0; i < count; i++) {
            markService.createMark(student1, VALID_MARK, module);
        }
    }

    // ============= CreateMark Tests (Decision Table Testing) =============

    @Test
    public void testCreateMark_ValidMark() {
        markService.createMark(student1, VALID_MARK, module1);

        assertEquals(1, markService.allMarks().size());
        Mark createdMark = markService.allMarks().get(0);
        assertEquals(student1, createdMark.getStudent());
        assertEquals(Integer.valueOf(VALID_MARK), createdMark.getMark());
        assertEquals(module1, createdMark.getModule());
    }

    @Test(expected = MarkException.class)
    public void testCreateMark_InvalidMarkTooLow() {
        markService.createMark(student1, INVALID_LOW_MARK, module1);
    }

    @Test(expected = MarkException.class)
    public void testCreateMark_InvalidMarkTooHigh() {
        markService.createMark(student1, INVALID_HIGH_MARK, module1);
    }

    @Test
    public void testCreateMark_BoundaryValues() {
        // Test lower bound
        markService.createMark(student1, MIN_MARK, module1);
        // Test upper bound
        markService.createMark(student2, MAX_MARK, module2);

        assertEquals(2, markService.allMarks().size());
    }

    @Test
    public void testCreateMark_MultipleDifferentMarks() {
        markService.createMark(student1, MIN_MARK, module1);   // minimum
        markService.createMark(student1, MIDDLE_MARK, module1); // middle
        markService.createMark(student1, MAX_MARK, module1);   // maximum

        assertEquals(3, markService.allMarks().size());
    }

    @Test
    public void testCreateMark_SameMarkDifferentStudents() {
        markService.createMark(student1, VALID_MARK, module1);
        markService.createMark(student2, VALID_MARK, module1);

        assertEquals(2, markService.allMarks().size());
    }

    @Test(expected = MarkException.class)
    public void testCreateMark_NullStudent() {
        markService.createMark(null, VALID_MARK, module1);
    }

    @Test(expected = MarkException.class)
    public void testCreateMark_NullModule() {
        markService.createMark(student1, VALID_MARK, null);
    }

    @Test(expected = MarkException.class)
    public void testCreateMark_NullMark() {
        markService.createMark(student1, null, module1);
    }

    @Test(expected = MarkException.class)
    public void testCreateMark_AllNullParameters() {
        markService.createMark(null, null, null);
    }

    // ============= AllMarks Tests (Equivalence Class Testing) =============

    @Test
    public void testAllMarks_EmptyList() {
        assertTrue(markService.allMarks().isEmpty());
    }

    @Test
    public void testAllMarks_SingleMark() {
        markService.createMark(student1, VALID_MARK, module1);
        assertEquals(1, markService.allMarks().size());
    }

    @Test
    public void testAllMarks_MultipleMarks() {
        markService.createMark(student1, VALID_MARK, module1);
        markService.createMark(student2, VALID_MARK, module2);

        List<Mark> marks = markService.allMarks();
        assertEquals(2, marks.size());
    }

    @Test
    public void testAllMarks_ModificationAttempt() {
        markService.createMark(student1, VALID_MARK, module1);
        List<Mark> marks = markService.allMarks();
        marks.clear(); // Should not affect internal list

        assertEquals(1, markService.allMarks().size());
    }

    // ============= FindMarkByModule Tests (Equivalence Class Testing) =============

    @Test
    public void testFindMarkByModule_ExistingModule() {
        markService.createMark(student1, VALID_MARK, module1);
        markService.createMark(student2, VALID_MARK, module1);
        markService.createMark(student1, VALID_MARK, module2);

        List<Mark> moduleMarks = markService.findMarkByModule(module1);

        assertEquals(2, moduleMarks.size());
        assertTrue(moduleMarks.stream()
                .allMatch(m -> m.getModule().getReference()
                        .toString().equalsIgnoreCase(module1.getReference().toString())));
    }

    @Test
    public void testFindMarkByModule_NonExistingModule() {
        markService.createMark(student1, VALID_MARK, module1);
        List<Mark> moduleMarks = markService.findMarkByModule(module2);
        assertTrue(moduleMarks.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindMarkByModule_NullModule() {
        markService.findMarkByModule(null);
    }

    @Test
    public void testFindMarkByModule_CaseInsensitivity() {
        markService.createMark(student1, VALID_MARK, module1);
        Module sameModuleDifferentCase = new Module(ModuleName.BDA, "TestModule", 5);

        List<Mark> marks = markService.findMarkByModule(sameModuleDifferentCase);
        assertEquals(1, marks.size());
    }

    @Test
    public void testFindMarkByModule_LargeDataset() {
        createMultipleMarks(100, module1);
        markService.createMark(student2, VALID_MARK, module2);

        List<Mark> marks = markService.findMarkByModule(module1);
        assertEquals(100, marks.size());
    }

    // ============= BestMarkByModule Tests (Equivalence Class & Boundary Testing) =============

    @Test
    public void testBestMarkByModule_SingleStudent() {
        markService.createMark(student1, VALID_MARK, module1);
        Student bestStudent = markService.bestMarkByModule(module1);
        assertEquals(student1, bestStudent);
    }

    @Test
    public void testBestMarkByModule_MultipleStudents() {
        markService.createMark(student1, VALID_MARK, module1);
        markService.createMark(student2, MAX_MARK, module1); // Best mark
        markService.createMark(student1, MIDDLE_MARK, module1);

        Student bestStudent = markService.bestMarkByModule(module1);
        assertEquals(student2, bestStudent);
    }

    @Test
    public void testBestMarkByModule_NoMarksForModule() {
        markService.createMark(student1, VALID_MARK, module1);
        Student bestStudent = markService.bestMarkByModule(module2);
        assertNull(bestStudent);
    }

    @Test
    public void testBestMarkByModule_EqualMarks() {
        markService.createMark(student1, MAX_MARK, module1);
        markService.createMark(student2, MAX_MARK, module1);

        Student bestStudent = markService.bestMarkByModule(module1);
        assertNotNull(bestStudent);
        assertEquals(student1, bestStudent); // Should return first student with highest mark
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBestMarkByModule_NullModule() {
        markService.bestMarkByModule(null);
    }

    @Test
    public void testBestMarkByModule_BoundaryValues() {
        markService.createMark(student1, MIN_MARK, module1);  // minimum
        markService.createMark(student2, MAX_MARK, module1); // maximum

        Student best = markService.bestMarkByModule(module1);
        assertEquals(student2, best);
    }

    @Test
    public void testBestMarkByModule_AllSameMarks() {
        // Test when all students have the same mark
        markService.createMark(student1, VALID_MARK, module1);
        markService.createMark(student2, VALID_MARK, module1);

        Student best = markService.bestMarkByModule(module1);
        assertNotNull(best);
        assertEquals(student1, best); // Should return first student with that mark
    }
    
    @Test
    public void testMarkToString() {
        Mark mark = new Mark(student1, VALID_MARK, module1);
        String markString = mark.toString();
        assertTrue(markString.contains("student=" + student1));
        assertTrue(markString.contains("mark=" + VALID_MARK));
        assertTrue(markString.contains("module=" + module1));
    }
    
}