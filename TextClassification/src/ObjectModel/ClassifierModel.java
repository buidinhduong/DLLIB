package ObjectModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The ClassifierModel is the classifier model after training phase ! 
 * @author BUI DUONG
 */
public class ClassifierModel {
    /**
     * number of training observations
     */
    public int numOfObservations=0;
    
    /**
     * number of categories
     */
    public int numOfCategories=0;
    
    /**
     * number of features
     */
    public int numOfFeatures=0;
    
    /**
     * log priors for log( P(c) )
     */
    public Map<String, Double> logPriors = new HashMap<>();
    
    /**
     * log likelihood for log( P(x|c) ) 
     */
    public Map<String, Map<String, Double>> logLikelihoods = new HashMap<>();
    public void printDesciption(boolean isFullDesctiption)
    {
        System.out.println("======Model Information=====");
        System.out.println("numOfFeatures:"+numOfFeatures);
        System.out.println("numOfCategories:"+numOfCategories);
        System.out.println("\tlogPriors:");
        for(Map.Entry<String, Double> info : logPriors.entrySet()) {   
            System.out.println(info.getKey()+":"+info.getValue());
        }
        if(isFullDesctiption){
        System.out.println("\tlikelihood");
        for(Entry<String, Map<String, Double>> info : logLikelihoods.entrySet()) {   
            System.out.println(info.getKey()+":"+info.getValue());
            for(Entry<String, Double> info2 : info.getValue().entrySet()) {   
                System.out.println("\t"+info2.getKey()+":"+info2.getValue());
            }
        }
        }
        System.out.println("======End Model Information=====");

    }
}
