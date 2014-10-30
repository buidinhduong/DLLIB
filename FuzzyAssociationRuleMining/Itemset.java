package LIRMM;
/*
* Author: BUI DINH DUONG
* Email: dinhduong.bui@gmail.com
* Website: http://buidinhduong.com
*/
import java.util.ArrayList;
import java.util.Collections;
import org.junit.Assert;

public class Itemset{
    /**
     * Itemset
     */
    public ArrayList<String> getNames() {
        return names;
    }
    
    public void setNames(ArrayList<String> names) {
        this.names = names;
    }
    public void addNames(ArrayList<String> newnames ) {
        if(names==null)names=new ArrayList<>();
        names.addAll(newnames);
    }
    public void addNames(String[] newnames ) {
        if(names==null)names=new ArrayList<>();
        for(String s: newnames)
            names.add(s);
    }
    public void addName(String name) {
       if(names==null)names=new ArrayList<>();
       names.add(name);
    }
    public float getCardinality() {
        if(cardinality==0){
            System.out.println("Itemset.cardinality=0");
            cardinality=AprioriTid.membershipValueOf(this);
        }
        return cardinality;
    }
    public void setCardinality(float cardinality) {
        this.cardinality = cardinality;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String itemsname="(";
        int start=0;
        for(String name : names){
            if(start==0){ itemsname+=name;}
            else if(start==names.size()-1){ itemsname+=","+name;}
            else itemsname+=","+name;
            start++;
        }
         itemsname+=")";
        return itemsname;
    }
    public String toSpaceString() {
        // TODO Auto-generated method stub
        int start=0;
        String itemsname="";
        for(String name : names){
            if(start==0){ itemsname+=name;}
            else if(start==names.size()-1){ itemsname+=" "+name;}
            else itemsname+=" "+name;
            start++;
        }
        return itemsname;
    }
    public Itemset minusSubset(Itemset subset)
    {
        int expectedSubsetSize=this.size()-subset.size();
        Itemset smallset=new Itemset();
        ArrayList<String> all=new ArrayList<>(this.getNames());//copy
        for(String s : subset.getNames())
        {
            all.remove(s);
        }    
        Assert.assertEquals("WRONG MINUS SUBSET", expectedSubsetSize, all.size());
        smallset.setNames(all);
        return smallset;
        
    }
    public ArrayList<Itemset> subsets() {
        //System.out.println("Subset of["+toSpaceString()+"]");
        ArrayList<Itemset> subsets=new ArrayList<>();
        ArrayList<Itemset> candidates=new ArrayList<>();
        for(String s : this.getNames())
        {
            Itemset itemset1=new Itemset();
            itemset1.addName(s);
            subsets.add(itemset1);
            candidates.add(itemset1);
        }
        int k_itemset = 2;
        //convert to vector string
        do
        {
            if(k_itemset==this.size()) return subsets;
            //generate the candidates
            candidates= AprioriTid.generateCandidates(k_itemset,candidates);
            //increase the itemset that is being looked at
            k_itemset++;
            //add to subset
            subsets.addAll(candidates);
        }while(candidates.size()>1);
        long explectedCount=(long) (Math.pow(2, this.size())-2);
        Assert.assertEquals("WRONG SUB-ITEMSET GENERATION",explectedCount, subsets.size());
        return subsets;
        
    }
    public ArrayList<Rule> generateRules(CalculateItemsetSupport rot) {
        this.root=rot;
        ArrayList<Rule> rules = new ArrayList<>();
        ArrayList<Itemset> sub=this.subsets();
        for(Itemset ss : sub)
        {
            Itemset end=this.minusSubset(ss);
            end.setCardinality(this.root.calculateSupport(end));
            ss.setCardinality(this.root.calculateSupport(ss));
            Rule newrul=new Rule(ss, end);
            if(!checkIn(newrul, rules)){
               newrul.setSupport(this.getCardinality());
               if(newrul.getConfidence()>=Config.minconfidence)
               rules.add(newrul);
            }
            //add reverse
            Rule rrever=newrul.reverse();
            if(!checkIn(rrever, rules)){
                rrever.setSupport(this.getCardinality());
                if(rrever.getConfidence()>=Config.minconfidence)
                rules.add(rrever);
            }
        }
       
    return rules;
    }
    public ArrayList<Rule> generateRules() {
        ArrayList<Rule> rules = new ArrayList<>();
            ArrayList<Itemset> sub=this.subsets();
            for(Itemset ss : sub)
            {
                Itemset end=this.minusSubset(ss);
                Rule newrul=new Rule(ss, end);
                if(!checkIn(newrul, rules)){
                   newrul.setSupport(this.getCardinality());
                   if(newrul.getConfidence()>=Config.minconfidence)
                   rules.add(newrul);
                }
                //add reverse
                Rule rrever=newrul.reverse();
                if(!checkIn(rrever, rules)){
                    rrever.setSupport(this.getCardinality());
                    if(rrever.getConfidence()>=Config.minconfidence)
                    rules.add(rrever);
                }
            }
           
        return rules;
    }
    public boolean checkIn(Rule rul, ArrayList<Rule> rules)
    {
        //check if this rule added
        boolean isAdded=false;
        for(Rule r : rules)
        {
            if(r.description().equals(rul.description())){
                    isAdded=true;
                    break;
            }
        }
        return isAdded;
    }
    public int size()
    {
        return this.getNames().size();
    }
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        
        Itemset item=(Itemset)obj;
        if(this.getNames().size()!=item.getNames().size()) return false;
        ArrayList<String> myItems=new ArrayList<>(this.getNames());
        ArrayList<String> yourItems=new ArrayList<>(item.getNames());
        Collections.sort(myItems);
        Collections.sort(yourItems);
        for(int i=0;i<myItems.size();i++)
        {
            if(!myItems.get(i).equals(yourItems.get(i)))
            {
                return false;
            }
        }
        return true;
    }
    private ArrayList<String>names;
    float cardinality;
    CalculateItemsetSupport root;
}
