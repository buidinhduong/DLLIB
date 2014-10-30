package LIRMM;

public class TransactionItem {
    public TransactionItem(String name, float mfvalue) {
        super();
        this.name = name;
        this.mfvalue = mfvalue;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getMfvalue() {
        return mfvalue;
    }
    public void setMfvalue(float mfvalue) {
        this.mfvalue = mfvalue;
    }
    private String name;
    private float mfvalue;
}
