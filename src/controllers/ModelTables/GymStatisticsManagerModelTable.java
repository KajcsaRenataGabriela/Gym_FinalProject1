package controllers.ModelTables;

public class GymStatisticsManagerModelTable {

    String client, gym, trainer, exercise, equipment, diet;

    public GymStatisticsManagerModelTable(String client, String gym, String trainer,String exercise, String equipment, String diet){
        this.client=client;
        this.gym=gym;
        this.trainer=trainer;
        this.exercise=exercise;
        this.equipment=equipment;
        this.diet=diet;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getGym() {
        return gym;
    }

    public void setGym(String gym) {
        this.gym = gym;
    }

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getEquipment() {
        if(!doIHaveAnExercise())
            equipment=null;
        return equipment;
    }

    public void setEquipment(String equipment) {
        if(doIHaveAnExercise())
            this.equipment = equipment;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public boolean doIHaveAnExercise(){
        return exercise != null;
    }
}
