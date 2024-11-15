
import group.Group;
import group.GroupService;
import group.GroupName;
import mark.Mark;
import mark.MarkService;
import module.Module;
import module.ModuleName;
import module.ModuleService;
import student.Student;
import student.StudentService;
import teacher.Grade;
import teacher.Teacher;
import teacher.TeacherService;
import org.junit.Before;
import org.junit.Test;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MainTest {
    private GroupService groupService;
    private ModuleService moduleService;
    private StudentService studentService;
    private TeacherService teacherService;
    private MarkService markService;
    
    public Main callMain() {
    	Main main = new Main();
    	return main;
    }

    @Before
    public void setUp() {
        groupService = new GroupService();
        moduleService = new ModuleService();
        studentService = new StudentService();
        teacherService = new TeacherService();
        markService = new MarkService();
    }

    // ============= Group Integration Tests =============

    @Test
    public void testCreateGroups_InitialState() {
        // When
        List<Group> groups = createGroups();

        // Then
        assertEquals(3, groups.size());
        // Verify initial state matches Table 1
        for (Group group : groups) {
            assertEquals(Integer.valueOf(0), group.getNumberStudent());
        }
        assertTrue(groups.stream().anyMatch(g -> g.getReference() == GroupName.MIAD));
        assertTrue(groups.stream().anyMatch(g -> g.getReference() == GroupName.MSIA));
        assertTrue(groups.stream().anyMatch(g -> g.getReference() == GroupName.MSIR));
    }

    // ============= Module Integration Tests =============

    @Test
    public void testCreateModules_MatchesTable2() {
        // When
        List<Module> modules = createModules();

        // Then
        assertEquals(4, modules.size());
        // Verify matches Table 2
        assertTrue(moduleExists(modules, ModuleName.BDA, "base de donnée avancé", 40));
        assertTrue(moduleExists(modules, ModuleName.CRY, "cryptographie", 35));
        assertTrue(moduleExists(modules, ModuleName.RI, "réseaux Informatique", 28));
        assertTrue(moduleExists(modules, ModuleName.GL, "génie logiciel", 30));
    }

    // ============= Student Integration Tests =============

    @Test
    public void testCreateStudents_MatchesTable3() {
        // Given
        createGroups();

        // When
        List<Student> students = createStudents();

        // Then
        assertEquals(4, students.size());
        // Verify matches Table 3
        verifyStudent(students.get(0), 1, "sofian gasb", "2000-01-08", GroupName.MIAD);
        verifyStudent(students.get(1), 2, "amine kaci", "1999-05-11", GroupName.MSIA);
        verifyStudent(students.get(2), 3, "hamid jebri", "1997-10-26", GroupName.MIAD);
        verifyStudent(students.get(3), 4, "hanane safi", "1995-10-26", GroupName.MSIR);
    }

    // ============= Group Update Integration Tests =============

    @Test
    public void testGroupUpdate_MatchesTable4() {
        // Given
        createGroups();
        List<Student> students = createStudents();

        // When
        groupService.updateNumberOfStudent(students);

        // Then
        // Verify matches Table 4
        assertEquals(Integer.valueOf(2), groupService.findByReference("MIAD").getNumberStudent());
        assertEquals(Integer.valueOf(1), groupService.findByReference("MSIA").getNumberStudent());
        assertEquals(Integer.valueOf(1), groupService.findByReference("MSIR").getNumberStudent());
    }

    // ============= Teacher Integration Tests =============

    @Test
    public void testCreateTeachers_MatchesTable5() {
        // Given
        createGroups();
        createModules();

        // When
        List<Teacher> teachers = createTeachers();

        // Then
        assertEquals(2, teachers.size());
        // Verify matches Table 5
        verifyTeacher(teachers.get(0), 1, "khalifa ahmed", Grade.MCA,
                new ModuleName[]{ModuleName.BDA},
                new GroupName[]{GroupName.MIAD, GroupName.MSIA});
        verifyTeacher(teachers.get(1), 2, "brahim gasbi", Grade.MCB,
                new ModuleName[]{ModuleName.GL, ModuleName.RI},
                new GroupName[]{GroupName.MSIR});
    }

    // ============= Mark Integration Tests =============

    @Test
    public void testCreateMarks_MatchesTable6() {
        // Given
        createGroups();
        createModules();
        createStudents();

        // When
        List<Mark> marks = createMarks();

        // Then
        assertEquals(3, marks.size());
        // Verify matches Table 6
        verifyMark(marks.get(0), 1, 15, ModuleName.BDA);
        verifyMark(marks.get(1), 1, 11, ModuleName.CRY);
        verifyMark(marks.get(2), 2, 10, ModuleName.CRY);
    }

    @Test
    public void testBestMarkByModule_CRY() {
        // Given
        createGroups();
        createModules();
        createStudents();
        createMarks();

        // When
        Module cryptoModule = moduleService.findByReference("CRY");
        Student bestStudent = markService.bestMarkByModule(cryptoModule);
        List<Mark> cryptoMarks = markService.findMarkByModule(cryptoModule);

        // Then
        assertNotNull(bestStudent);
        assertEquals(Integer.valueOf(1), bestStudent.getId());
        assertEquals(2, cryptoMarks.size());
        assertEquals(11, cryptoMarks.get(0).getMark().intValue()); // Best mark for CRY
    }

    // Helper Methods

    private List<Group> createGroups() {
        GroupName[] nameGroup = {GroupName.MIAD, GroupName.MSIA, GroupName.MSIR};
        for (GroupName groupName : nameGroup) {
            groupService.saveGroup(groupName);
        }
        return groupService.allGroups();
    }

    private List<Module> createModules() {
        ModuleName[] nameRefModules = {ModuleName.BDA, ModuleName.CRY, ModuleName.RI, ModuleName.GL};
        String[] nameModules = {"base de donnée avancé", "cryptographie", "réseaux Informatique", "génie logiciel"};
        Integer[] numberHours = {40, 35, 28, 30};

        for (int i = 0; i < nameRefModules.length; i++) {
            moduleService.saveModule(nameRefModules[i], nameModules[i], numberHours[i]);
        }
        return moduleService.allModules();
    }

    private List<Student> createStudents() {
        String[] fullNames = {"sofian gasb", "amine kaci", "hamid jebri", "hanane safi"};
        LocalDate[] dateOfBirth = {
                LocalDate.of(2000, 1, 8),
                LocalDate.of(1999, 5, 11),
                LocalDate.of(1997, 10, 26),
                LocalDate.of(1995, 10, 26)
        };
        GroupName[] nameGroup = {GroupName.MIAD, GroupName.MSIA, GroupName.MIAD, GroupName.MSIR};

        for (int i = 0; i < fullNames.length; i++) {
            Group group = groupService.findByReference(nameGroup[i].toString());
            studentService.saveStudent(i+1, fullNames[i], dateOfBirth[i], group);
        }
        return studentService.allStudents();
    }

    private List<Teacher> createTeachers() {
        String[] fullNames = {"khalifa ahmed", "brahim gasbi"};
        GroupName[][] nameGroup = {{GroupName.MIAD, GroupName.MSIA}, {GroupName.MSIR}};
        ModuleName[][] moduleName = {{ModuleName.BDA}, {ModuleName.GL, ModuleName.RI}};
        Grade[] grade = {Grade.MCA, Grade.MCB};

        for (int i = 0; i < fullNames.length; i++) {
            List<Group> listGroups = new ArrayList<>();
            for (GroupName gName : nameGroup[i]) {
                Group group = groupService.findByReference(gName.toString());
                listGroups.add(group);
            }

            List<Module> listModules = new ArrayList<>();
            for (ModuleName mName : moduleName[i]) {
                Module module = moduleService.findByReference(mName.toString());
                listModules.add(module);
            }

            teacherService.saveTeacher(i+1, fullNames[i], grade[i], listModules, listGroups);
        }
        return teacherService.allTeachers();
    }

    private List<Mark> createMarks() {
        Integer[] idStudent = {1, 1, 2};
        ModuleName[] refModule = {ModuleName.BDA, ModuleName.CRY, ModuleName.CRY};
        Integer[] notes = {15, 11, 10};

        for (int i = 0; i < idStudent.length; i++) {
            Student student = studentService.findById(idStudent[i]);
            Module module = moduleService.findByReference(refModule[i].toString());
            markService.createMark(student, notes[i], module);
        }
        return markService.allMarks();
    }

    private boolean moduleExists(List<Module> modules, ModuleName name, String moduleName, Integer hours) {
        return modules.stream()
                .anyMatch(m -> m.getReference() == name
                        && m.getName().equals(moduleName)
                        && m.getNumberHours().equals(hours));
    }

    private void verifyStudent(Student student, int id, String name, String birthDate, GroupName groupName) {
        assertEquals(Integer.valueOf(id), student.getId());
        assertEquals(name, student.getFullName());
        assertEquals(LocalDate.parse(birthDate), student.getDateBirth());
        assertEquals(groupName, student.getGroup().getReference());
    }

    private void verifyTeacher(Teacher teacher, int id, String name, Grade grade,
                               ModuleName[] modules, GroupName[] groups) {
        assertEquals(Integer.valueOf(id), teacher.getId());
        assertEquals(name, teacher.getFullName());
        assertEquals(grade, teacher.getGrade());
        assertEquals(modules.length, teacher.getListModules().size());
        assertEquals(groups.length, teacher.getListGroup().size());
    }

    private void verifyMark(Mark mark, int studentId, int value, ModuleName moduleName) {
        assertEquals(Integer.valueOf(studentId), mark.getStudent().getId());
        assertEquals(Integer.valueOf(value), mark.getMark());
        assertEquals(moduleName, mark.getModule().getReference());
    }
    
    @Test
    public void testShowGroup() {
    	Main main = callMain();
        List<Group> groups = main.createGroups();

        // Redirect System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call showGroup
        Main.showGroups(groups);

        // Verify the output contains the expected group references
        String output = outContent.toString();
        assertTrue(output.contains("MIAD"));
        assertTrue(output.contains("MSIA"));
        assertTrue(output.contains("MSIR"));

        // Reset System.out
        System.setOut(System.out);
    }
    
    @Test
    public void testMain() {
        // Given: Setup any necessary data
        Main main = callMain();
        createGroups();
        createModules();
        createStudents();
        createTeachers();
        createMarks();

        // Redirect System.out to capture printed output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // When: Call the main method
        main.main(new String[0]);

        // Then: Verify that the output contains expected data
        String output = outContent.toString();

        // Verify the output contains the expected group names
        assertTrue(output.contains("MIAD"));
        assertTrue(output.contains("MSIA"));
        assertTrue(output.contains("MSIR"));

        // Verify the output contains the expected module names
        assertTrue(output.contains("BDA"));
        assertTrue(output.contains("CRY"));
        assertTrue(output.contains("RI"));
        assertTrue(output.contains("GL"));

        // Verify the output contains the expected student names
        assertTrue(output.contains("sofian gasb"));
        assertTrue(output.contains("amine kaci"));
        assertTrue(output.contains("hamid jebri"));
        assertTrue(output.contains("hanane safi"));

        // Verify the output contains the expected teacher names
        assertTrue(output.contains("khalifa ahmed"));
        assertTrue(output.contains("brahim gasbi"));

        // Verify the output contains the expected marks
        assertTrue(output.contains("15"));
        assertTrue(output.contains("11"));
        assertTrue(output.contains("10"));

        // Reset System.out
        System.setOut(System.out);
    }

    
    
}