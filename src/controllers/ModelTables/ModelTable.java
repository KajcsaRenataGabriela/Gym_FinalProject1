package controllers.ModelTables;

public class ModelTable {

    String Something;

    public ModelTable(String Something){
        this.Something = Something;
    }

    public String getSomething() {
        return Something;
    }

    public void setSomething(String something) {
        Something = something;
    }
}
