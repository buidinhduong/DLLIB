package LIRMM;

public class Region {
    String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String[] getSubregion() {
        return subregion;
    }
    public void setSubregion(String[] subregion) {
        this.subregion = subregion;
    }
    String subregion[];
    boolean hasSubregion(String rname)
    {
        for(String sub : subregion)
        {
            if(sub.contains(rname))
                return true;
        }
        return false;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Region Name:"+name+" lengt:"+subregion.length;
    }
}
