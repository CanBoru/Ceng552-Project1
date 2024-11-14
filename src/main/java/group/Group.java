package group;
public class Group {
    private final GroupName reference;
    private Integer numberStudent;
    public Group(GroupName reference) {
        this.reference = reference;
        numberStudent=0;
    }

    public void setNumberStudent(Integer numberStudent) {

        this.numberStudent = numberStudent;
    }

    @Override
    public String toString() {
        return "Group{" +
                "reference=" + reference +
                '}';
    }
    public String showGroup() {
        return "Group{" +
                "reference=" + reference +" , number student="+numberStudent+
                '}';
    }

    public GroupName getReference() {
        return reference;
    }

    public Integer getNumberStudent() {
        return numberStudent;
    }
}
