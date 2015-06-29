package ObjectModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ModelEvaluator {
 /**
     * a: the number of document objects belong to the classifying class and they are assigned to
the class by classifier model (correct assignment)
• b: the number of object does not belong to the classifying class but they are assigned to the
class by classifier model (incorrect assignment).
• c: the number of document objects belong to the classifying class but they are removed from
the class (incorrect remove).
• d: the number of object does not belong to the classifying class and they are removed from
the class (correct remove).
     */
   public double a,b,c,d;
   ClassifierModel model;
   public Map<String, Integer> classAndNumOfDocuments = new HashMap<>();
   public Map<String, Integer> classAndNumOfClassifiedDocuments = new HashMap<>();
   public ModelEvaluator(ClassifierModel model)
    {
     this.model=model;  
    }
   public void printDescription()
   {
       System.out.println("======Model Evaluation======");
       System.out.println("======Class and Number of Documents======");
       for(Entry<String, Integer> info : classAndNumOfDocuments.entrySet()) {   
           System.out.println(info.getKey()+":"+info.getValue());
       }
       System.out.println("======Class and Number of Classifed Documents======");

       for(Entry<String, Integer> info : classAndNumOfClassifiedDocuments.entrySet()) {   
           System.out.println(info.getKey()+":"+info.getValue());
       }
       System.out.println("Precision:"+getPrecision());
       System.out.println("Fallout:"+getFallout());

       System.out.println("======End Model Evaluation======");
   }
   public double getPrecision()
   {
       //is defined as a measure of the proportion of selected items that the system got right:
       return a/(a+b);
   }
   public double getFallout()
   {
       //the proportion of no targeted items that were mistakenly selected:
       return b/(b+a);
   }
   
   public double getAccuracy()
   {
       //the proportion of correctly classified objects.
       return (a+b)/(a+b+c+d);
   }
   public double getRecall()
   {
       //is defined as the proportion of the target items that the system selected
       return a/(a+c);
   }
   public double getError()
   {
       return (c+b)/(a+b+c+d);
   }
   public double getFMeasure()
   {
       //F1=2*Precision*Recall/(Precision+ Recall)
       return -1;
   }
   
   
}
