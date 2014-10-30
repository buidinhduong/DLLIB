package LIRMM;

public class DataTable {
    public String[] getAtributes() {
        return atributes;
    }
    public void setAtributes(String[] atributes) {
        this.atributes = atributes;
    }
    public float[][] getData() {
        return data;
    }
    public void setData(float[][] data) {
        this.data = data;
    }
    String [] atributes;
    float [][] data;
    public int getAttributeCount()
    {
        if(atributes!=null)
     return atributes.length;
        return 0;
    }
    
}
