package ObjectModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The Document Object represents the texts that we use for training or 
 * prediction as a bag of words.
 * 
 * @author  BUI DUONG
 */
public class Document {
    
    /**
     * List of features and its counts (word weighting)
     */
    public Map<String, Integer> features;
    
    /**
     * The class of the document
     */
    public String category;
    
    /**
     * Document constructor
     */
    public Document() {
        features = new HashMap<>();
    }
    public void printFeatures() {
       // StringBuilder sb = new StringBuilder();
        Iterator<Entry<String, Integer>> iter = features.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, Integer> entry = iter.next();
            //sb.append(entry.getKey());
            //sb.append('=').append('"');
            //sb.append(entry.getValue());
           // sb.append('"');
            if (iter.hasNext()) {
                //sb.append(',').append(' ');
                //System.out.print(entry.getKey()+"="+entry.getValue()+"");
            }
            //sb.append("\n");

        }
        //return sb.toString();

    }
}
