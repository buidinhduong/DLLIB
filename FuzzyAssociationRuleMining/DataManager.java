package LIRMM;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;
public class DataManager {
    private static final String INPUTFOLDER=Config.INPUTFOLDER;
    //final String INPUTFILE =INPUTFOLDER+"FuzzyAge.txt";
    //final String INPUTFILE =INPUTFOLDER+"FuzzyCourseData.txt";
    //final String INPUTFILE =INPUTFOLDER+"FuzzyBENHTIM.txt";
    final String INPUTFILE =INPUTFOLDER+"Fuzzy_Schema_Age.txt";
    final String SEPARATOR=" ";
    @Test
    public void test()
    {
        convertToFuzzyData(INPUTFOLDER+"Schema_Age.txt", "");
        //fuzzyAgeCount();
    }
    public DataTable readInputData()
    {
        DataTable data=new DataTable();
        try {
            String input=readInput(INPUTFILE);
            String[] lines= input.split("\n");
            data.setAtributes(lines[0].split(SEPARATOR));
            data.setData(new float[lines.length-1][data.getAttributeCount()]);
            for(int i=1;i<lines.length;i++)
            {
                System.out.print("\nLine["+i+"]:"+lines[i]);
                float iLines[]=lineToNumberArray(lines[i]);
                data.data[i-1]=iLines;
            }
            return  data;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return data;
       
    }
    public float[] lineToNumberArray(String line)
    {
        String values[]=line.split(SEPARATOR);
        float nums[]=new float[values.length];
        for(int i=0;i<values.length;i++)
        {
            nums[i]=Float.valueOf(values[i]);
        }
        return nums;
    }
    public static String readInput(String INPUTFILE) throws FileNotFoundException, IOException
    {
    try(BufferedReader br = new BufferedReader(new FileReader(INPUTFILE))) {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        System.out.print("=======Input File Content:==========\n"+sb.toString());
        return sb.toString();
    }
    }
    public static ArrayList<Region> getRegions()
    {
        ArrayList<Region> regions=new ArrayList<>();
        //Course DB
//        regions.add(convertRegion("ST", "ST.M,ST.H,ST.L"));
//        regions.add(convertRegion("DB", "DB.L,DB.M,DB.H"));
//        regions.add(convertRegion("OOP", "OOP.L,OOP.M,OOP.H"));
//        regions.add(convertRegion("DS","DS.L,DS.M,DS.H"));
//        regions.add(convertRegion("MIS", "MIS.L,MIS.M,MIS.H"));
        
        //Heart DB
//        regions.add(convertRegion("TUOI","TUOI.TRE,TUOI.TRUNGNIEN,TUOI.GIA" ));
//        regions.add(convertRegion("CHOLESTEROL","CHOLESTEROL.THAP,CHOLESTEROL.CAO" ));
//        regions.add(convertRegion("DUONG","DUONG.KO DUONG.CO" ));
//        regions.add(convertRegion("BENHTIM"," BENHTIM.KO BENHTIM.CO"));
        //Schema DB
        regions.add(convertRegion("Schema","Employee-OWN-House Employee-Friend-Student Employee-RENT-Apartment Employee-Friend-Employee Student-RENT-Apartment Student-Friend-Student Employee-Probationary-Company"));
        regions.add(convertRegion("Age","Age.Young Age.Middle Age.Old" ));
        regions.add(convertRegion("Count","Count.VeryFew Count.Few Count.Most" ));
        
        return regions;
    }
    public static Region convertRegion(String main, String subs)
    {
        //format : Main Sub,Sub,Sub
        Region r=new Region();
        r.setName(main);
        if(subs.contains(","))
            r.setSubregion(subs.split(","));
        else if(subs.contains(" "))
            r.setSubregion(subs.split(","));
        return r;
    }
   public static String arrayToString(int[] array)
   {
       String data="";
       for(int value :array)
       {
           if(data=="") data+=value;
           else data+=" "+value;
       }
       return data;
   }
   public static float[][]  fuzzyAgeCount()
   {
       String inputcontent[];
       
    try {
        ArrayList<String> cols=new ArrayList<>();
        cols.add("Young");
        cols.add("MiddleAge");
        cols.add("Old");
        cols.add("VeryFew");
        cols.add("Few");
        cols.add("Most");
        inputcontent = readInput(INPUTFOLDER+"Schema_Age.txt").split("\n");
        float[] age= new float[inputcontent.length-1];
        float[] count= new float[inputcontent.length-1];

        for(int i=1;i<inputcontent.length;i++)
        {
            String col[]=inputcontent[i].split(" ");
            age[i-1]=Float.valueOf(col[1]);
            count[i-1]=Float.valueOf(col[2]);
        }
        
        float all[][]=new float[age.length][3+3];
        
        for(int index=0;index<age.length;index++)
        {
            float[] young={10,25,30,40};
            float[] middleAge={30,35,48,55};
            float[] old={50,55,90,120};
            float y=MembershipFunction.TrapezoidalMFs(age[index], young);
            all[index][0]=y;
            float md=MembershipFunction.TrapezoidalMFs(age[index], middleAge);
            all[index][1]=md;
            float o= MembershipFunction.TrapezoidalMFs(age[index], old);
            all[index][2]=o;
        }
       
        for(int index=0;index<count.length;index++)
        {
            float[] veryfew={0,5,10,15};
            float[] few={20,25,30,45};
            float[] most={40,65,80,100};
            float vf=MembershipFunction.TrapezoidalMFs(count[index], veryfew);
            all[index][3]=vf;
            float f=MembershipFunction.TrapezoidalMFs(count[index], few);
            all[index][4]=f;
            float m=MembershipFunction.TrapezoidalMFs(count[index], most);
            all[index][5]=m;
        }
        displayMaxtriData(cols, all);
        return all;
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return null;
       
       
   }
   public static void convertToFuzzyData(String inputfile,String outputFile)
   {
       //convert schema
       /**
        * Step 1: collect all distinct attributes , count
        * init maxtrix [data.lengt][count];
        * for each row, get index of atttibut at step 1
        * if yes, set matrix[row][index]=1, else = 0  
        */
       try {
        String inputcontent[]=readInput(inputfile).split("\n");
       //collect all distict attributes
        ArrayList<String> attributes=new ArrayList<>();
        int rowCount=0;
        for(int i=1;i<inputcontent.length;i++)
        {
            rowCount++;
            String schema=inputcontent[i].split(" ")[0];
            if(!stringExistIn(schema, attributes))
                attributes.add(schema);
            
        }
        int[][] binaryMaxtrix=new int[rowCount][attributes.size()];
        //update matrix
        for(int index=1;index<inputcontent.length;index++)//ingore first line
        {
            String schema=inputcontent[index].split(" ")[0];
            //this must be true
            binaryMaxtrix[index-1][attributes.indexOf(schema)]=1;
        }
        //displayMaxtriData(attributes, binaryMaxtrix);
        attributes.add("Young");
        attributes.add("MiddleAge");
        attributes.add("Old");
        attributes.add("VeryFew");
        attributes.add("Few");
        attributes.add("Most");
        displayMaxtriData(attributes,mergeMatrix(binaryMaxtrix, fuzzyAgeCount()) );
        //append to matrix
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
       
   }
   public static float[][] mergeMatrix(int[][] binaryMaxtrix,float[][] m2)
   {
       float[][] matrix=new float[binaryMaxtrix.length][binaryMaxtrix[0].length+m2[0].length];
       System.out.println("M1:row: "+binaryMaxtrix.length+" col size+"+binaryMaxtrix[0].length);
       System.out.println("M2:row: "+m2.length+" col size+"+m2[0].length);
       System.out.println("row: "+matrix.length+" col size+"+matrix[0].length);
       for(int row=0;row<matrix.length;row++)
       {
           for(int col=0;col<matrix[0].length;col++)
           {
               if(col<binaryMaxtrix[0].length)
               {
                   //fill all m1
                   matrix[row][col]=binaryMaxtrix[row][col];
               }
               else{//fill all m2 to
                   matrix[row][col]=m2[row][col-binaryMaxtrix[0].length];
               }
           }
           
       }
       return  matrix;
   }
   public static void displayMaxtriData(ArrayList<String> cols, float[][]data)
   {
       System.out.println("=====Matrix Data Col:"+cols.size()+"====");

       for(String col: cols)
       {
           System.out.print(" "+col);
       }
       System.out.println("\n");
       for(int row=0;row<data.length;row++)
       {
           System.out.print("\n");
           for(int col=0;col<cols.size();col++)
           {
               if(col==0)
               System.out.print(data[row][col]);
               else System.out.print(" "+data[row][col]);
           }
       }
           
   }
   public static void displayMaxtriData(ArrayList<String> cols, int[][]data)
   {
       System.out.println("=====Matrix Data Col:"+cols.size()+"====");

       for(String col: cols)
       {
           System.out.print(" "+col);
       }
       System.out.println("\n");
       for(int row=0;row<data.length;row++)
       {
           System.out.print("\n");
           for(int col=0;col<cols.size();col++)
           {
               if(col==0)
               System.out.print(data[row][col]);
               else System.out.print(" "+data[row][col]);
           }
       }
           
   }
   public static boolean stringExistIn(String a, ArrayList<String> arr)
   {
       for(String s : arr)
       {
           if(s.equals(a))
               return true;
       }
       return false;
   }
}
