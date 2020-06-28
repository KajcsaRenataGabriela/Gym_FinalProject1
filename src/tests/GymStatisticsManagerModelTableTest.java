package tests;

import controllers.ModelTables.GymStatisticsManagerModelTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GymStatisticsManagerModelTableTest {

    private final GymStatisticsManagerModelTable modelTable = new GymStatisticsManagerModelTable("Reni", "Fitness","Trainer", "Jumping","Rope","Water");

    @Test
    void getClient() {
        assertEquals("Reni", modelTable.getClient());
    }

    @Test
    void getGym() {
        assertEquals("Fitness", modelTable.getGym());
    }

    @Test
    void getTrainer() {
        assertEquals("Trainer", modelTable.getTrainer());
    }

    @Test
    void getExercise() {
        assertEquals("Jumping", modelTable.getExercise());
    }

    @Test
    void getEquipment() {
        assertEquals("Rope", modelTable.getEquipment());
    }

    @Test
    void getDiet() {
        assertEquals("Water", modelTable.getDiet());
    }

    @Test
    void trueDoIHaveAnExercise() {
        assertTrue(modelTable.doIHaveAnExercise());
    }

    @Test
    void falseDoIHaveAnEquipment() {
        modelTable.setExercise(null);
        assertFalse(modelTable.doIHaveAnExercise());
    }

    @Test
    void equipmentAndExercise() {
        if(modelTable.getExercise()==null) {
            assertNull(modelTable.getEquipment());
        }
        else {
            assertEquals("Rope", modelTable.getEquipment());
        }
    }
}