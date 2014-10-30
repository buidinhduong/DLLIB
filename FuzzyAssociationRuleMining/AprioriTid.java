package LIRMM;
/*
* This file is written for mining fuzzy association rules.based on AprioriTiD algorithm
* Author: BUI DINH DUONG
* Email: dinhduong.bui@gmail.com
* Website: http://buidinhduong.com
*/
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

public class AprioriTid {
    static ArrayList<Itemset> allFrequentItemset;
    static ArrayList<Rule> allRules;
    static DataManager dm;
    static DataTable db;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //step 1 
        loadData();
        //step 2
        ArrayList<Itemset> c1=step3(db);
        ArrayList<Itemset> highests=step4(c1, DataManager.getRegions());
        c1=step5_pruning(highests);
        aprioriStart(2,c1, db);//start from 2-itemset
        generateAllRules();
        displayAllRules();
    }
    /**
     * Pre process data
     */
    public static void loadData()
    {
        dm=new DataManager();
        db= dm.readInputData();
    }
   
    public static void generateAllRules()
    {
        for(Itemset s:allFrequentItemset)
        {
            ArrayList<Rule> rules=s.generateRules();
            if(allRules==null) allRules=new ArrayList<>();
            allRules.addAll(rules);
        }
    }
    public static void displayAllRules()
    {
        if(allRules==null||allRules.size()==0){
            System.out.println("No rule found");
            return;
        }
        System.out.println("Display all rules");
        for(Rule r : allRules)
        {
            System.out.println(r.toString());
        }
    }
    public static void aprioriStart(int k,ArrayList<Itemset> candidates,DataTable database)
    {
        Date d; //date object for timing purposes
        long start, end; //start and end time
        int k_itemset=k; //the current itemset being looked at
        //NOTE: MIN K=2 When Candidate  1-itemset
        System.out.println("Apriori algorithm has started.\n");
        //start timer
        d = new Date();
        start = d.getTime();
        //while not complete
        ArrayList<Itemset> ck_ok;
        do
        {
            //generate the candidates
            ArrayList<Itemset> ck= generateCandidates(k_itemset,candidates);
            //compute support
           step9_membershipvalue(ck, database);
           //pruning
           ck_ok= step5_pruning(ck);
            if(ck_ok.size()!=0)
            {
                System.out.println("Frequent " + k_itemset + "-itemsets");
                displayItemset(ck_ok);
            }
            //increase the itemset that is being looked at
            k_itemset++;
            candidates=ck_ok;
            if(allFrequentItemset==null) allFrequentItemset=new ArrayList<>();
            allFrequentItemset.addAll(ck_ok);            
        /*if there are <=1 frequent items, then its the end.
            This prevents reading through the database again.
            When there is only one frequent itemset.*/
        }while(ck_ok.size()>1);
        //end timer
        d = new Date();
        end = d.getTime();

        //display the execution time
        System.out.println("Execution time is: "+((double)(end-start)/1000) + " seconds.");
    }
    public static void displayItemset(ArrayList<Itemset> itemset)
    {
        if(itemset==null||itemset.size()==0){ System.out.println("\nNo Itemset");return;}
 
        System.out.println("\nDisplay Itemset size:"+itemset.size());
        for(Itemset i: itemset)
        {
            
            System.out.println(i.toSpaceString()+","+i.getCardinality());
        }
    }
    public  static float confidenceOf(Itemset items, DataTable db) {
            float confiden=0;
            Itemset fisrt=new Itemset();
            fisrt.addName(items.getNames().get(0));
            float above=membershipValueOf(items, db);
            float below=membershipValueOf(fisrt, db);
            System.out.println("====="+above+"/"+below);
            confiden=(float)above/below;
            return confiden;
    }
    public static  void step9_membershipvalue(ArrayList<Itemset> items, DataTable db) {
        for(Itemset e : items)
        {
           float mvalue=membershipValueOf(e, db);
           e.cardinality=mvalue;
        }
    }
    /**
     * Find the region with the highest count among the three possible regions for each attribute.
     * @param dt
     * @return
     */
    public static  ArrayList<Itemset> step4(ArrayList<Itemset> items,ArrayList<Region> regions) {
        ArrayList<Itemset> higestItems=new ArrayList<>();
        for(Region r : regions)
        {
            //check each region
            Itemset highest=new Itemset();
            for(Itemset i : items)
            {
                if(r.hasSubregion(i.getNames().get(0)))
                {
                    if(i.cardinality>highest.cardinality)
                    {
                        highest.cardinality=i.cardinality;
                        highest.setNames(i.getNames());
                    }
                }
            }
            higestItems.add(highest);
            System.out.println("Higest"+highest.toString()+","+highest.cardinality);
        }
        return higestItems;
    }

    public static  ArrayList<Itemset> step5_pruning(ArrayList<Itemset> items) {
        //prunning
        ArrayList<Itemset> itemsOk=new ArrayList<>();
        for(Itemset i : items)
        {
            if(i.cardinality>=Config.minsupport){
                itemsOk.add(i);
            }
        }
        return itemsOk;
    }
    
    public static  ArrayList<Itemset> step3(DataTable dt) {
        ArrayList<Itemset> items_1=new ArrayList<>();
        for(int col=0;col<dt.getAttributeCount();col++){
            Itemset it=new Itemset();
            for(int row=0;row<dt.data.length;row++)
            {
                //calculate its scalar cardinality for all the transactions from C1
                it.cardinality+=dt.data[row][col];
            }
            it.addName(dt.getAtributes()[col]);
            System.out.println(it.getNames().get(0)+","+it.cardinality);
            items_1.add(it);
        }
        return items_1;
    }
    public static float membershipValueOf(Itemset item)
    {
        return membershipValueOf(item,db);
    }
    public static float membershipValueOf(Itemset item,DataTable database)
    {
        float[] mins=new float[database.data.length];
        //foreach transaction
        for(int row=0;row<mins.length;row++)
        {
            float colvalues[]=new float[item.getNames().size()];
            for(int i=0;i<colvalues.length;i++)
            {
                String at=item.getNames().get(i);
                colvalues[i]=database.data[row][indexOfAttribute(at, database)];
            }
            mins[row]=min(colvalues);
        }
        return sum(mins)/database.data.length;
    }
    public static float sum(float[] array) {
        float sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }
    public static float min(float[] array) {
        // Validates input
       
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        // Finds and returns min
        float min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (Float.isNaN(array[i])) {
                return Float.NaN;
            }
            if (array[i] < min) {
                min = array[i];
            }
        }
    
        return min;
    }
    public static int indexOfAttribute(String at, DataTable inDatatb)
    {
        int index=-1;
        String[] att=inDatatb.getAtributes();
        for(int i=0;i<att.length;i++){
            if(att[i].equals(at)){
                index=i;
            }
        }
        if(index==-1)
            System.out.println("==Not Found Attribute "+at+" in DB");
        return index;
    }
    
    static ArrayList<Itemset> generateCandidates(int n,ArrayList<Itemset> precandidates)
    {
       // System.out.println("Generate "+n+"-Itemset from Precandidates "+precandidates.size());
        ArrayList<Itemset> itemsL=new ArrayList<>();
        //convert to vector string
        Vector<String> candidates=new Vector<>();
        for(Itemset i: precandidates){
            candidates.add(i.toSpaceString());
            //System.out.println("[Pre String Candidates:"+i.toSpaceString()+"]");
        }
       Vector<String> newCan= generateCandidates(n, candidates);
      // System.out.println("Candidate "+n+"-itemset count:"+newCan.size());
       for (String s : newCan) {
        Itemset it=new Itemset();
        it.addNames(s.split(" "));
        if(it.getNames().size()!=0)
        itemsL.add(it);
    }
       return itemsL;
    }
    static Vector<String> generateCandidates(int n,Vector<String> candidates)
    {
       
        Vector<String> tempCandidates = new Vector<String>(); //temporary candidate string vector
        String str1, str2; //strings that will be used for comparisons
        StringTokenizer st1, st2; //string tokenizers for the two itemsets being compared

        //if its the first set, candidates are just the numbers
        if(n==1)
        {
            for(int i=1; i<=5; i++)
            {
                tempCandidates.add(Integer.toString(i));
            }
        }
        else if(n==2) //second itemset is just all combinations of itemset 1
        {
            //add each itemset from the previous frequent itemsets together
            for(int i=0; i<candidates.size(); i++)
            {
                st1 = new StringTokenizer(candidates.get(i));
                str1 = st1.nextToken();
                for(int j=i+1; j<candidates.size(); j++)
                {
                    st2 = new StringTokenizer(candidates.elementAt(j));
                    str2 = st2.nextToken();
                    tempCandidates.add(str1 + " " + str2);
                }
            }
        }
        else
        {
            //for each itemset
            for(int i=0; i<candidates.size(); i++)
            {
                //compare to the next itemset
                for(int j=i+1; j<candidates.size(); j++)
                {
                    //create the strigns
                    str1 = new String();
                    str2 = new String();
                    //create the tokenizers
                    st1 = new StringTokenizer(candidates.get(i));
                    st2 = new StringTokenizer(candidates.get(j));

                    //make a string of the first n-2 tokens of the strings
                    for(int s=0; s<n-2; s++)
                    {
                        str1 = str1 + " " + st1.nextToken();
                        str2 = str2 + " " + st2.nextToken();
                    }

                    //if they have the same n-2 tokens, add them together
                    if(str2.compareToIgnoreCase(str1)==0)
                        tempCandidates.add((str1 + " " + st1.nextToken() + " " + st2.nextToken()).trim());
                }
            }
            
        }
        //clear the old candidates
        candidates.clear();
        //set the new ones
        candidates = new Vector<String>(tempCandidates);
        tempCandidates.clear();
        System.out.println("===Gergerate  "+candidates.size()+" Itemset");
        return candidates;
    }
    public void testAprioriGeneration()
    {
        //test again
        System.out.println("TEST GAIN");
        Vector<String> testcandidates=new Vector<>();
        testcandidates.add("DB.H");
        testcandidates.add("DS.M");
        testcandidates.add("OOP.H");
        Vector<String> items2=generateCandidates(2, testcandidates);
        for(String s :items2)
        {
            System.out.println(s);
        }
        Vector<String> item3=generateCandidates(3, items2);
        for(String s :item3)
        {
            System.out.println(s);
        }
    }
}
