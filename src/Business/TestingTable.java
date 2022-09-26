package Business;

public class TestingTable {
    private int studentId;
    private int variantId;

    public TestingTable(int studentId, int variantId) {
        this.studentId = studentId;
        this.variantId = variantId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }
}
