package tests;

import controllers.ModelTables.ModelTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelTableTest {

    private final ModelTable modelTable = new ModelTable("test");
    @Test
    void getSomething() {
        assertEquals("test", modelTable.getSomething());
    }

}