package LIRMM;

/*
* This file is extended of the SPMF DATA MINING SOFTWARE
* for mining fuzzy association rules.
* Author: BUI DINH DUONG
* Email: dinhduong.bui@gmail.com
* Website: http://buidinhduong.com
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
public class FuzzyFPGrowth implements CalculateItemsetSupport{

	public ArrayList<Itemset> allfreequentItemset;
	// for statistics
	private long startTimestamp; // start time of the latest execution
	private long endTime; // end time of the latest execution
	private int transactionCount = 0; // transaction count in the database
	private int itemsetCount; // number of freq. itemsets found
	
	// minimum support threshold
	public int relativeMinsupp;
	
	// object to write the output file
	BufferedWriter writer = null; 
	

	/**
	 * Default constructor
	 */
	public FuzzyFPGrowth() {
		
	}
	   /**
     * Run the algorithm.
     * @param input the file path of an input transaction database.
     * @param output the path of the desired output file
     * @param minsupp minimum support threshold as a percentage (double)
     * @throws IOException exception if error while writing the file
     */
    
	public void runAlgorithmForFuzzyDatabase(String input, String output, double minsupp) throws IOException
	{
	 // record the start time
        startTimestamp = System.currentTimeMillis();
        // reinitialize the number of itemsets found to 0
        itemsetCount =0;
        // Prepare the output file
        writer = new BufferedWriter(new FileWriter(output)); 
        
        // (1) PREPROCESSING: Initial database scan to determine the frequency of each item
        // The frequency is store in a map where:
        // key: item   value: support count
        final Map<String, Float> mapSupport = new HashMap<String, Float>();
        // call this method  to perform the database scan
        scanFuzzyDatabaseToDetermineFrequencyOfSingleItems(input, mapSupport);
        // convert the absolute minimum support to a relative minimum support
        // by multiplying by the database size.
        this.relativeMinsupp = (int) Math.ceil(minsupp * transactionCount);
        
        // (2) Scan the database again to build the initial FP-Tree
        // Before inserting a transaction in the FPTree, we sort the items
        // by descending order of support.  We ignore items that
        // do not have the minimum support.
        
        // create the FPTree
        FPTree tree = new FPTree();
        
        
        BufferedReader reader = new BufferedReader(new FileReader(input));
        String line;
        String[] items=null;
        // for each line (transaction) in the input file until the end of file
        while( ((line = reader.readLine())!= null)){ 
            // if the line is  a comment, is  empty or is a
            // kind of metadata
            if (line.isEmpty() == true ||
                    line.charAt(0) == '#' || line.charAt(0) == '%'
                            || line.charAt(0) == '@') {
                continue;
            }
            // first line
            if(items==null){
                items= line.split(" ");
                continue;
            }
            
            // split the transaction into MF values
            String[] lineSplited = line.split(" ");
            // create an array list to store the items
            List<TransactionItem> transaction = new ArrayList<TransactionItem>();
            // for each item in the transaction
            for(int col=0;col<lineSplited.length;col++){
                // if it is frequent, add it to the transaction
                String itemString = items[col];
                // otherwise not because it cannot be part of a frequent itemset.
                if(mapSupport.get(itemString) >= relativeMinsupp){
                    float mfvalue=Float.valueOf(lineSplited[col]);
                    transaction.add(new TransactionItem(itemString, mfvalue));    
                }
            }
            // sort item in the transaction by descending order of support
            Collections.sort(transaction, new Comparator<TransactionItem>(){
                public int compare(TransactionItem item1, TransactionItem item2){
                    // compare the support
                    //System.out.print("["+mapSupport.get(item2.getName())+"-"+mapSupport.get(item1.getName())+"]");
                    //float compare =(float)( mapSupport.get(item2.getName()) - mapSupport.get(item1.getName()));
                    float compare =(float)(item2.getMfvalue() - item1.getMfvalue());
                    // if the same support, we check the lexical ordering!
                    if(compare == 0){ 
                        return item1.getName().compareTo(item2.getName());
                    }
                    // otherwise use the support
                    int comparevalue=compare>0?1:-1;
                    return comparevalue;
                }
            });
            // add the sorted transaction to the fptree.
            tree.addFuzzyTransaction(transaction);
        }
        // close the input file
        reader.close();
        
        // We create the header table for the tree
        tree.createHeaderList(mapSupport);
        
        // (5) We start to mine the FP-Tree by calling the recursive method.
        // Initially, the prefix alpha is empty.
        String[] prefixAlpha = new String[0];
        if(tree.headerList.size() > 0) {
            fpgrowth(tree, prefixAlpha, transactionCount, mapSupport);
        }
        //print(tree.root, null);
        printStats();
        // close the output file
        writer.close();
        // record the end time
        endTime= System.currentTimeMillis();
	}

//	private void print(FPNode_Strings node, String indentation) {
//		System.out.println(indentation + "NODE : " + node.itemID + " COUNTER" + node.counter);
//		for(FPNode_Strings child : node.childs) {
//			print(child, indentation += "\t");
//		}
//	}

	/**
	 * This method scans the input database to calculate the support of single items
	 * @param input the path of the input file
	 * @param mapSupport a map for storing the support of each item (key: item, value: support)
	 * @throws IOException  exception if error while writing the file
	 */
	private void scanFuzzyDatabaseToDetermineFrequencyOfSingleItems(String input,
            final Map<String, Float> mapSupport)
            throws FileNotFoundException, IOException {
        //Create object for reading the input file
        BufferedReader reader = new BufferedReader(new FileReader(input));
        String line;
        String[] items=null;
        // for each line (transaction) until the end of file
        while( ((line = reader.readLine())!= null)){ 
            // if the line is  a comment, is  empty or is a
            // kind of metadata
            if (line.isEmpty() == true ||
                    line.charAt(0) == '#' || line.charAt(0) == '%'
                            || line.charAt(0) == '@') {
                continue;
            }
            
            if(mapSupport.isEmpty())//first line to get items
            {
                items=line.split(" ");
                for(String it: items)
                {
                    mapSupport.put(it, new Float(0));
                }
                continue;
            }
            // split the transaction into items
            String[] lineSplited = line.split(" ");
             // for each item in the transaction, update its support
            for(int col=0;col<lineSplited.length;col++){ 
                String itemString=items[col];
                float itemMFValue=Float.valueOf(lineSplited[col]);
                // increase the support count of the item
                Float count = mapSupport.get(itemString);
                if(count == null){
                    mapSupport.put(itemString, itemMFValue);
                }else{
                    mapSupport.put(itemString, count+itemMFValue);
                }
            }
            // increase the transaction count
            transactionCount++;
        }
        // close the input file
        reader.close();
    }

	/**
	 * This method mines pattern from a Prefix-Tree recursively
	 * @param tree  The Prefix Tree
	 * @param prefix  The current prefix "alpha"
	 * @param mapSupport The frequency of each item in the prefix tree.
	 * @throws IOException   exception if error writing the output file
	 */
	private void fpgrowth(FPTree tree, String[] prefixAlpha, float prefixSupport, Map<String, Float> mapSupport) throws IOException {
		// We need to check if there is a single path in the prefix tree or not.
		if(tree.hasMoreThanOnePath == false){
			// That means that there is a single path, so we 
			// add all combinations of this path, concatenated with the prefix "alpha", to the set of patterns found.
			addAllCombinationsForPathAndPrefix(tree.root.childs.get(0), prefixAlpha); // CORRECT?
			
		}else{ // There is more than one path
			fpgrowthMoreThanOnePath(tree, prefixAlpha, prefixSupport, mapSupport);
		}
	}
	
	/**
	 * Mine an FP-Tree having more than one path.
	 * @param tree  the FP-tree
	 * @param prefix  the current prefix, named "alpha"
	 * @param mapSupport the frequency of items in the FP-Tree
	 * @throws IOException   exception if error writing the output file
	 */
	private void fpgrowthMoreThanOnePath(FPTree tree, String [] prefixAlpha, float prefixSupport, Map<String, Float> mapSupport) throws IOException {
		// We process each frequent item in the header table list of the tree in reverse order.
		for(int i= tree.headerList.size()-1; i>=0; i--){
			String item = tree.headerList.get(i);
			
			float support = mapSupport.get(item);
			// if the item is not frequent, we skip it
			if(support <  relativeMinsupp){
				continue;
			}
			// Create Beta by concatening Alpha with the current item
			// and add it to the list of frequent patterns
			String [] beta = new String[prefixAlpha.length+1];
			System.arraycopy(prefixAlpha, 0, beta, 0, prefixAlpha.length);
			beta[prefixAlpha.length] = item;
			
			// calculate the support of beta
			float betaSupport = (prefixSupport < support) ? prefixSupport: support;
			// save beta to the output file
			writeItemsetToFile(beta, betaSupport);
			
			// === Construct beta's conditional pattern base ===
			// It is a subdatabase which consists of the set of prefix paths
			// in the FP-tree co-occuring with the suffix pattern.
			List<List<FPNode>> prefixPaths = new ArrayList<List<FPNode>>();
			FPNode path = tree.mapItemNodes.get(item);
			while(path != null){
				// if the path is not just the root node
				if(path.parent.itemID != null){
					// create the prefixpath
					List<FPNode> prefixPath = new ArrayList<FPNode>();
					// add this node.
					prefixPath.add(path);   // NOTE: we add it just to keep its support,
					// actually it should not be part of the prefixPath
					
					//Recursively add all the parents of this node.
					FPNode parent = path.parent;
					while(parent.itemID != null){
						prefixPath.add(parent);
						parent = parent.parent;
					}
					// add the path to the list of prefixpaths
					prefixPaths.add(prefixPath);
				}
				// We will look for the next prefixpath
				path = path.nodeLink;
			}
			
			// (A) Calculate the frequency of each item in the prefixpath
			Map<String, Float> mapSupportBeta = new HashMap<String, Float>();
			// for each prefixpath
			for(List<FPNode> prefixPath : prefixPaths){
				// the support of the prefixpath is the support of its first node.
			    float pathCount = prefixPath.get(0).counter;  
				 // for each node in the prefixpath,
				// except the first one, we count the frequency
				for(int j=1; j<prefixPath.size(); j++){ 
					FPNode node = prefixPath.get(j);
					// if the first time we see that node id
					if(mapSupportBeta.get(node.itemID) == null){
						// just add the path count
						mapSupportBeta.put(node.itemID, pathCount);
					}else{
						// otherwise, make the sum with the value already stored
						mapSupportBeta.put(node.itemID, mapSupportBeta.get(node.itemID) + pathCount);
					}
				}
			}
			
			// (B) Construct beta's conditional FP-Tree
			FPTree treeBeta = new FPTree();
			// add each prefixpath in the FP-tree
			for(List<FPNode> prefixPath : prefixPaths){
				treeBeta.addPrefixPath(prefixPath, mapSupportBeta, relativeMinsupp); 
			}  
			// Create the header list.
			treeBeta.createHeaderList(mapSupportBeta); 
			
			// Mine recursively the Beta tree if the root as child(s)
			if(treeBeta.root.childs.size() > 0){
				// recursive call
				fpgrowth(treeBeta, beta, betaSupport, mapSupportBeta);
			}
		}
		
	}

	/**
	 * This method is for adding recursively all combinations of nodes in a path, concatenated with a given prefix,
	 * to the set of patterns found.
	 * @param nodeLink the first node of the path
	 * @param prefix  the prefix
	 * @param minsupportForNode the support of this path.
	 * @throws IOException 
	 */
	private void addAllCombinationsForPathAndPrefix(FPNode node, String[] prefix) throws IOException {
		// Concatenate the node item to the current prefix
		String [] itemset = new String[prefix.length+1];
		System.arraycopy(prefix, 0, itemset, 0, prefix.length);
		itemset[prefix.length] = node.itemID;
		// save the resulting itemset to the file with its support
		writeItemsetToFile(itemset, node.counter);
			
		if(node.childs.size() != 0) {
			addAllCombinationsForPathAndPrefix(node.childs.get(0), itemset);
			addAllCombinationsForPathAndPrefix(node.childs.get(0), prefix);
		}
	}
	

	/**
	 * Write a frequent itemset that is found to the output file.
	 */
	private void writeItemsetToFile(String [] itemset, float support) throws IOException {
		// increase the number of itemsets found for statistics purpose
		itemsetCount++;
		if(allfreequentItemset==null) allfreequentItemset=new ArrayList<>();
        Itemset is=new Itemset();
        //float sup= (float) (node.counter / transactionCount);
        is.setCardinality(support);
        is.addNames(itemset);
        allfreequentItemset.add(is);
		// create a string buffer 
		StringBuffer buffer = new StringBuffer();
		// write items from the itemset to the stringbuffer
		for(int i=0; i< itemset.length; i++){
			buffer.append(itemset[i]);
			if(i != itemset.length-1){
				buffer.append(' ');
			}
		}
		// append the support of the itemset
		buffer.append(':');
		buffer.append(support);
		// write the strinbuffer and create a newline so that we are
		// ready for the next itemset to be written
		writer.write(buffer.toString());
		writer.newLine();
	}

	/**
	 * Print statistics about the algorithm execution to System.out.
	 */
	public void printStats() {
		System.out
				.println("=============  FP-GROWTH - STATS =============");
		long temps = endTime - startTimestamp;
		System.out.println(" Transactions count from database : " + transactionCount);
		System.out.println(" Frequent itemsets count : " + itemsetCount); 
		System.out.println(" Total time ~ " + temps + " ms");
		System.out
				.println("===================================================");
	}
    @Override
    public float calculateSupport(Itemset is) {
        // TODO Auto-generated method stub
        for(Itemset it : allfreequentItemset)
        {
            //System.out.println("calculateSupport:"+is.toString()+"=="+it.toString()+"="+it.getCardinality());
            if(is.equals(it)){                
                return it.getCardinality();
            }
            
        }
        Assert.assertEquals("CalculateSupport"+is.toString()+" false, not found", 1, 2);
        return 0;
    }
}
