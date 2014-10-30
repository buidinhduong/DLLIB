package LIRMM;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class TestFPGrowth {
    public static void main(String arg[]) {
        try {
            String root = Config.INPUTFOLDER;
            FuzzyFPGrowth al = new FuzzyFPGrowth();
            double minsup = Config.minsupport;
            al.runAlgorithmForFuzzyDatabase(root + "Fuzzy_Schema_Age.txt", root
                    + "output3.txt", minsup);
            ArrayList<Rule> allRules = new ArrayList<>();
            AprioriTid.displayItemset(al.allfreequentItemset);
            for (Itemset s : al.allfreequentItemset) {
                if (s.size() > 1) {
                    ArrayList<Rule> rules = s.generateRules(al);
                    allRules.addAll(rules);
                }
            }
            System.out.println("======Display all rules====");
            for (Rule r : allRules) {
                System.out.println(r.toString());
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
