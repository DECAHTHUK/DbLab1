package Business;

public class Variant {
    private int id;
    private String pathToFile;

    public Variant(int id, String pathToFile) {
        this.id = id;
        this.pathToFile = pathToFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }
}
