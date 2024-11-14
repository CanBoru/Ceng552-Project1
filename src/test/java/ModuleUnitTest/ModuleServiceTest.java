package ModuleUnitTest;

import module.ModuleName;
import module.ModuleService;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;
import module.Module;

public class ModuleServiceTest {
    private ModuleService moduleService;
    private static final String VALID_NAME = "Test Module";
    private static final Integer VALID_HOURS = 30;
    private static final String LONG_NAME = "This is a very long module name that might exceed any reasonable length limit";

    @Before
    public void setUp() {
        moduleService = new ModuleService();
    }

    // ============= SaveModule Tests (Decision Table Testing) =============

    @Test
    public void testSaveModule_SingleModule() {
        moduleService.saveModule(ModuleName.BDA, "Big Data Analytics", 30);

        assertEquals(1, moduleService.allModules().size());
        Module savedModule = moduleService.allModules().get(0);
        assertEquals(ModuleName.BDA, savedModule.getReference());
        assertEquals("Big Data Analytics", savedModule.getName());
        assertEquals(Integer.valueOf(30), savedModule.getNumberHours());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveModule_NullReference() {
        moduleService.saveModule(null, VALID_NAME, VALID_HOURS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveModule_NullName() {
        moduleService.saveModule(ModuleName.BDA, null, VALID_HOURS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveModule_EmptyName() {
        moduleService.saveModule(ModuleName.BDA, "", VALID_HOURS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveModule_WhitespaceName() {
        moduleService.saveModule(ModuleName.BDA, "   ", VALID_HOURS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveModule_NullHours() {
        moduleService.saveModule(ModuleName.BDA, VALID_NAME, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveModule_NegativeHours() {
        moduleService.saveModule(ModuleName.BDA, VALID_NAME, -1);
    }

    @Test
    public void testSaveModule_MultipleModules() {
        moduleService.saveModule(ModuleName.BDA, "Big Data Analytics", 30);
        moduleService.saveModule(ModuleName.CRY, "Cryptography", 24);
        moduleService.saveModule(ModuleName.RI, "Information Retrieval", 20);

        assertEquals(3, moduleService.allModules().size());
    }

    @Test
    public void testSaveModule_AllEnumValues() {
        moduleService.saveModule(ModuleName.BDA, "Big Data Analytics", 30);
        moduleService.saveModule(ModuleName.CRY, "Cryptography", 24);
        moduleService.saveModule(ModuleName.RI, "Information Retrieval", 20);
        moduleService.saveModule(ModuleName.DEV_OPS, "DevOps", 25);
        moduleService.saveModule(ModuleName.GL, "Software Engineering", 28);

        assertEquals(5, moduleService.allModules().size());
        assertNotNull(moduleService.findByReference("BDA"));
        assertNotNull(moduleService.findByReference("CRY"));
        assertNotNull(moduleService.findByReference("RI"));
        assertNotNull(moduleService.findByReference("DEV_OPS"));
        assertNotNull(moduleService.findByReference("GL"));
    }

    @Test
    public void testSaveModule_DuplicateReference() {
        moduleService.saveModule(ModuleName.BDA, "Big Data Analytics", 30);
        moduleService.saveModule(ModuleName.BDA, "Big Data Analytics 2", 35);

        List<Module> modules = moduleService.allModules();
        assertEquals(2, modules.size());
        long bdaCount = modules.stream()
                .filter(m -> m.getReference() == ModuleName.BDA)
                .count();
        assertEquals(2, bdaCount);
    }

    @Test
    public void testSaveModule_ExtensiveHoursTesting() {
        moduleService.saveModule(ModuleName.BDA, "Test Module 1", 0);
        moduleService.saveModule(ModuleName.CRY, "Test Module 2", 1);
        moduleService.saveModule(ModuleName.RI, "Test Module 3", 100);
        moduleService.saveModule(ModuleName.GL, "Test Module 4", Integer.MAX_VALUE);

        List<Module> modules = moduleService.allModules();
        assertEquals(4, modules.size());
        assertTrue(modules.stream().anyMatch(m -> m.getNumberHours() == 0));
        assertTrue(modules.stream().anyMatch(m -> m.getNumberHours() == 1));
        assertTrue(modules.stream().anyMatch(m -> m.getNumberHours() == 100));
        assertTrue(modules.stream().anyMatch(m -> m.getNumberHours() == Integer.MAX_VALUE));
    }

    // ============= AllModules Tests (Equivalence Class Testing) =============

    @Test
    public void testAllModules_EmptyList() {
        List<Module> modules = moduleService.allModules();
        assertNotNull(modules);
        assertTrue(modules.isEmpty());
    }

    @Test
    public void testAllModules_SingleModule() {
        moduleService.saveModule(ModuleName.BDA, VALID_NAME, VALID_HOURS);
        assertEquals(1, moduleService.allModules().size());
    }

    @Test
    public void testAllModules_MultipleModules() {
        moduleService.saveModule(ModuleName.BDA, "Big Data Analytics", 30);
        moduleService.saveModule(ModuleName.CRY, "Cryptography", 24);
        assertEquals(2, moduleService.allModules().size());
    }

    @Test
    public void testAllModules_ModificationAttempt() {
        moduleService.saveModule(ModuleName.BDA, VALID_NAME, VALID_HOURS);
        List<Module> modules = moduleService.allModules();
        modules.clear(); // Should not affect internal list
        assertEquals(1, moduleService.allModules().size());
    }

    // ============= FindByReference Tests (Equivalence Class Testing) =============

    @Test
    public void testFindByReference_ExistingModule() {
        moduleService.saveModule(ModuleName.BDA, "Big Data Analytics", 30);

        Module found = moduleService.findByReference("BDA");

        assertNotNull(found);
        assertEquals(ModuleName.BDA, found.getReference());
        assertEquals("Big Data Analytics", found.getName());
        assertEquals(Integer.valueOf(30), found.getNumberHours());
    }

    @Test
    public void testFindByReference_NonExistingModule() {
        Module found = moduleService.findByReference("UNKNOWN");
        assertNull(found);
    }

    @Test
    public void testFindByReference_CaseInsensitive() {
        moduleService.saveModule(ModuleName.BDA, "Big Data Analytics", 30);

        assertNotNull(moduleService.findByReference("bda"));
        assertNotNull(moduleService.findByReference("BdA"));
        assertNotNull(moduleService.findByReference("BDA"));
    }

    @Test
    public void testFindByReference_NullReference() {
        Module found = moduleService.findByReference(null);
        assertNull(found);
    }

    @Test
    public void testFindByReference_EmptyString() {
        Module found = moduleService.findByReference("");
        assertNull(found);
    }

    @Test
    public void testFindByReference_WhitespaceOnly() {
        Module found = moduleService.findByReference("   ");
        assertNull(found);
    }

    @Test
    public void testFindByReference_FirstElement() {
        moduleService.saveModule(ModuleName.BDA, VALID_NAME, VALID_HOURS);
        moduleService.saveModule(ModuleName.CRY, VALID_NAME, VALID_HOURS);
        assertNotNull(moduleService.findByReference("BDA"));
    }

    @Test
    public void testFindByReference_LastElement() {
        moduleService.saveModule(ModuleName.BDA, VALID_NAME, VALID_HOURS);
        moduleService.saveModule(ModuleName.CRY, VALID_NAME, VALID_HOURS);
        assertNotNull(moduleService.findByReference("CRY"));
    }

    // ============= Additional Edge Cases =============

    @Test
    public void testSaveModule_LongName() {
        moduleService.saveModule(ModuleName.BDA, LONG_NAME, VALID_HOURS);
        Module saved = moduleService.findByReference("BDA");
        assertEquals(LONG_NAME, saved.getName());
    }

    @Test
    public void testSaveModule_DuplicateNamesWithDifferentReferences() {
        String commonName = "Common Module Name";
        moduleService.saveModule(ModuleName.BDA, commonName, 30);
        moduleService.saveModule(ModuleName.CRY, commonName, 25);
        moduleService.saveModule(ModuleName.RI, commonName, 20);

        List<Module> modules = moduleService.allModules();
        assertEquals(3, modules.size());

        long countWithCommonName = modules.stream()
                .filter(m -> m.getName().equals(commonName))
                .count();
        assertEquals(3, countWithCommonName);

        long uniqueReferences = modules.stream()
                .map(Module::getReference)
                .distinct()
                .count();
        assertEquals(3, uniqueReferences);
    }
}