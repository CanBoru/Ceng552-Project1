package module;
import java.util.List;
public interface ModuleRepository {
void saveModule(ModuleName reference,String name,Integer numberHours);
List<Module> allModules();
Module findByReference(String reference);
}
