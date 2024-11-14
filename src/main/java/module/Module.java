package module;

public class Module {
private ModuleName reference;
private String name;
private Integer numberHours;

    public Module(ModuleName reference, String name, Integer numberHours) {
        this.reference = reference;
        this.name = name;
        this.numberHours = numberHours;
    }

    @Override
    public String toString() {
        return "Module{" +
                "reference=" + reference +
                ", name='" + name + '\'' +
                ", numberHours=" + numberHours +
                '}';
    }

    public ModuleName getReference() {
        return reference;
    }

    public Integer getNumberHours() {
        return numberHours;
    }

    public String getName() {
        return name;
    }

}
