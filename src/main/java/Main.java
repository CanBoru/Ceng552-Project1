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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Main {
    private static GroupService groupService;
    private static ModuleService moduleService;
    private static StudentService studentService;
    private static TeacherService teacherService;
    private static MarkService markService;

    public static void main(String[] args) {
        //create a list of group
        List<Group> listGroups= createGroups();
        System.out.println("listGroups " );
        showGroup(listGroups);

        //create a list of modules
        System.out.println("-------------------------------------------");
        List<Module> listModules= createModules();
        System.out.println("listModules " );
        showModules(listModules);

        //create a list of students
        System.out.println("----------------------------------------");
        List<Student> listStudents= createStudents();
        System.out.println("listStudents " );
        showStudents(listStudents);

        //updating number of students in each group
        groupService.updateNumberOfStudent(listStudents);
        System.out.println("listGroups after adding students" );
        showGroup(groupService.allGroups());

        //create a list of teachers
        System.out.println("------------------------------------------");
        List<Teacher> listTeachers= createTeachers();
        System.out.println("listTeachers " );
        showTeachers(listTeachers);

        //create a list of marks
        System.out.println("-------------------------------------");
        List<Mark> listMarks= createMarks();
        System.out.println("listMarks  ");
        showMarks(listMarks);
        Module moduleCry=moduleService.allModules().get(1);
        Student st=markService.bestMarkByModule(moduleCry);
        System.out.println("-------------------------------------");
        System.out.println("the student which took best mark in CRY is :\n"+st);
        System.out.println("-------------------------------------");
        System.out.println("list of mark filtered by CRY module");
        List<Mark> lm=markService.findMarkByModule(moduleCry);
        showMarks(lm);
        }

    protected static List<Mark> createMarks() {
        Integer[] idStudent ={1,1,2};
        ModuleName[] refModule ={ModuleName.BDA, ModuleName.CRY, ModuleName.CRY};
        Integer[] notes={15,11,10};
        markService =new MarkService();
        for (int i = 0; i < idStudent.length; i++) {
            //-1 because the index list  starts from 0
            Integer idx=idStudent[i];
            Student student=studentService.findById(idx);
            Module module=moduleService.findByReference(refModule[i].toString());
            markService.createMark(student,notes[i],module);

        }
        return markService.allMarks();}

    protected static List<Teacher> createTeachers() {
        String[] fullNames ={"khalifa ahmed","brahim gasbi"};
        GroupName[][] nameGroup ={{GroupName.MIAD, GroupName.MSIA}, {GroupName.MSIR}};
        ModuleName[][] moduleName ={{ModuleName.BDA},{ModuleName.GL,ModuleName.RI}};
        Grade[] grade ={Grade.MCA,Grade.MCB};
        teacherService=new TeacherService();

        for (int i = 0; i <fullNames.length ; i++) {
            List<Group>listGroups=new ArrayList();
            Integer length=nameGroup[i].length;
            for (int j = 0; j <length ; j++) {
                 Group group=groupService.findByReference(nameGroup[i][j].toString());
                 listGroups.add(group);

            }
            List<Module>listModules=new ArrayList();
            length=moduleName[i].length;
            for (int j = 0; j <length ; j++) {

                    Module module=moduleService.findByReference(moduleName[i][j].toString());
                    listModules.add(module);

            }
            teacherService.saveTeacher(i+1,fullNames[i],grade[i],listModules,listGroups );
        }
    return teacherService.allTeachers();}

    protected static void showGroup(List<Group> allGroups) {
        for (Group group:allGroups) {
            System.out.println(group.showGroup());

        }
    }
    protected static void showTeachers(List<Teacher> listTeacher) {
        for (Teacher teacher:listTeacher) {
            System.out.println(teacher);
        }
    }
    protected static void showGroups(List<Group> listGroup) {
        for (Group group:listGroup) {
            System.out.println(group);
        }
    }
    protected static void showModules(List<Module> listModule) {
        for (Module module:listModule) {
            System.out.println(module);
        }
    }
    protected static void showStudents(List<Student> listStudent) {
        for (Student student:listStudent) {
            System.out.println(student);

        }

    }
    protected static void showMarks(List<Mark> listMarks) {
        for (Mark mark:listMarks) {
            System.out.println(mark);

        }
    }

    protected static List<Student> createStudents() {
        String fullNames[]={"sofian gasb","amine kaci","hamid jebri","hanane safi"};
        LocalDate dateOfBirth []={LocalDate.of(2000, 1, 8)
                ,LocalDate.of(1999, 5, 11)
                ,LocalDate.of(1997, 10, 26)
                ,LocalDate.of(1995, 10, 26)};
        Integer[] numberHours ={40,35,28,25};
        GroupName[] nameGroup ={GroupName.MIAD, GroupName.MSIA
                , GroupName.MIAD,GroupName.MSIR};
        studentService=new StudentService();
        for (int i = 0; i < fullNames.length; i++) {
            Group group=groupService.findByReference(nameGroup[i].toString());
            studentService.saveStudent(i+1,fullNames[i],dateOfBirth[i],group);
        }

   return studentService.allStudents(); }

    protected static List<Module> createModules() {
        ModuleName nameRefModules[]={ModuleName.BDA, ModuleName.CRY
                , ModuleName.RI,ModuleName.GL};
        String nameModules[]={"base de donnée avancé","cryptographie"
                ,"réseaux Informatique","génie logiciel"};
        Integer numberHours[]={40,35,28,30};
        moduleService=new ModuleService();
        for (int i = 0; i <nameRefModules.length ; i++) {
            moduleService.saveModule(nameRefModules[i],nameModules[i],numberHours[i] );
        }
    return moduleService.allModules();
    }

    // returns all created groups
    protected static List<Group> createGroups() {
        GroupName nameGroup[]={GroupName.MIAD
                , GroupName.MSIA, GroupName.MSIR};
        groupService=new GroupService();

        for (GroupName groupName : nameGroup) {
            groupService.saveGroup(groupName);
        }
    return groupService.allGroups();}
}
