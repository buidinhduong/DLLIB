/* -------------------------------------------------------------------------- */
/*                                                                            */
/*                       F U Z Z Y   A P R I O R I   T                        */
/*                                                                            */
/*                               Frans Coenen                                 */
/*                                                                            */
/*                           Tuesday 4 Match 2008                             */
/*                         (Revised 22 August 2008)                           */
/*                                                                            */
/*                       Department of Computer Science                       */
/*                        The University of Liverpool                         */
/*                                                                            */
/* -------------------------------------------------------------------------- */

/** Class that contains methods to support Fuzzy Association Rule Mining (FARM)
based in the Apriori-T algorithm. */

/* Structure:

AssocRuleMining
      |
      +-- TotalSupportTree
      		  |
		  +-- FuzzyAprioriT	 */

//package lucsKDD_ARM;

// Java packages
import java.io.*;
import java.util.*;

// Java GUI packages
import javax.swing.*;

public class FuzzyAprioriT extends TotalSupportTree {

    /*------------------------------------------------------------------------*/
    /*                                                                        */
    /*                                   FIELDS                               */
    /*                                                                        */
    /*------------------------------------------------------------------------*/

    /** 2-D aray to hold input data from data file. <P>First index is row (record or
    TID) number starting from 0, and second is attribute (column) number starting from
    zero. */
    protected FuzzyDataItem[][] dataArray = null;
    /** Array used to renumber columns for input data in terms of frequency of single
    attributes (reordering will enhance performance for some ARM algorithms). */
    protected FuzzyDataItem[] conversionArray = null;
    /** The reference to start of t-tree. */
    protected FuzzyTtreeNode[] startTtreeRef;

    /*---------------------------------------------------------------------*/
    /*                                                                     */
    /*                           CONSTRUCTORS                              */
    /*                                                                     */
    /*---------------------------------------------------------------------*/

    /** With argument from existing instance of class AssocRuleMining.
    @param newInstance the given instance of the <TT>AssocRuleMining</TT>
    class. */

    public FuzzyAprioriT(FuzzyAprioriT newInstance) {
	super(newInstance);
	dataArray = newInstance.dataArray;
        }

    /** Default constructor. */

    public FuzzyAprioriT() {
        }

    /*-------------------------------------------------------------------*/
    /*                                                                   */
    /*                   T-TREE BUILDING METHODS                         */
    /*                                                                   */
    /*-------------------------------------------------------------------*/


    /** Commences start process of generating a total support tree (T-tree): GUI
    version. Min support is used in non-weighted Apriori-T and is the support in 
    terms of number of records, this has no meaning with respect to fuzzy 
    support as defined here. Min support therefore has the same value as the 
    support field.
    @param textArea the given instance of the <TT>JTextArea</TT> class. */

    public void createTotalSupportTree(JTextArea textArea) {
	    // Calculate support in terms of number of records
	    double tempSup = numRows*support; 
	    textArea.append("Apriori-T with X-Cchecking\nMinimum support " +
	              "threshold = " + twoDecPlaces(support) + " (" +
                            twoDecPlaces(tempSup) + " records)\n");

	// If no data (possibly as a result of an order and pruning operation)
	// return
	if (numOneItemSets==0) return;

	// Initilise T-tree data structure and diagnostic counters. Set number
	// of t-tree nodes to zero (this is a static field so will not be reset
	// in repeat calls to the T-tree constructor).
	startTtreeRef   = null;
	numFrequentSets = 0;
	numUpdates      = 0l;
	TtreeNode.setNumberOfNodesFieldToZero();

        // Continue
        contCreateTtree(textArea);

        // Potential output
        if (outputTtreeStatsFlag) outputTtreeStats(textArea);
        if (outputTtreeFlag) outputTtree(textArea);
//if (outputTtreeGraphFlag) drawTtreeGraph();
	}
	
    /* CREATE T-TREE TOP LEVEL */
    /** Generates level 1 (top) of the T-tree. */

    protected void createTtreeTopLevel() {
	// Dimension and initialise top level of T-tree
	startTtreeRef = new FuzzyTtreeNode[numOneItemSets+1];
	for (int index=1;index<=numOneItemSets;index++)
	    			startTtreeRef[index] = new FuzzyTtreeNode();

        // Add support for each 1 itemset
	createTtreeTopLevel2();

	// Prune top level, setting any unsupported 1-itemsets to null
	pruneLevelN(startTtreeRef,1);
	}

    /** Adds supports to level 1 (top) of the T-tree. */

    protected void createTtreeTopLevel2() {
        numLevelsInTtree = 1;

        // Loop through data set record by record and add support for each
	// 1 itemset
	for (int index1=0;index1<dataArray.length;index1++) {
	    // Non null record (if initial data set has been reordered and
	    // pruned some records may be empty!
	    if (dataArray[index1] != null) {
    	        for (int index2=0;index2<dataArray[index1].length;index2++) {
                    int attNum   = dataArray[index1][index2].getItemNumber();
                    double value = dataArray[index1][index2].getFuzzyValue();
		    startTtreeRef[attNum].support=startTtreeRef[attNum].support+value;
		    numUpdates++;
		    }
		}
	    }
	}

    /** Commences the process of determining the remaining levels in the T-tree
    (other than the top level), level by level in an "Apriori" manner --- GUI
    version.
    @param textArea the given instance of the <TT>JTextArea</TT> class. */

    protected void createTtreeLevelN(JTextArea textArea) {
        int nextLevel=2;

	// Loop while a further level exists

	while (nextLevelExists) {
	    // Add support
	    addSupportToTtreeLevelN(nextLevel);
	    // Prune unsupported candidate sets
	    pruneLevelN(startTtreeRef,nextLevel);
	    // Attempt to generate next level
	    nextLevelExists=false;
	    generateLevelN(startTtreeRef,nextLevel,null);
//if (nextLevel==3)
//outputTtree(textArea);
	    nextLevel++;
	    }

	//End
	numLevelsInTtree = nextLevel-1;
	textArea.append("Levels in T-tree = " + numLevelsInTtree + "\n");
	}

    /* ---------------------------- */
    /* ADD SUPPORT VALUES TO T-TREE */
    /* ---------------------------- */

    /* ADD SUPPORT VALUES TO T-TREE LEVEL N */
    /** Commences process of adding support to a given level in the T-tree
    (other than the top level).
    @param level the current level number (top level = 1). */

    protected void addSupportToTtreeLevelN(int level) {
	// Loop through data set record by record
        for (int index=0;index<dataArray.length;index++) {
	    // Non null record (if initial data set has been reordered and
	    // pruned some records may be empty
	    if (dataArray[index] != null) addSupportToTtree(startTtreeRef,
	                      level,dataArray[index].length,dataArray[index],1.0);
	    }
	}

    /* ADD SUPPORT TO T-TREE FIND LEVEL */
    /** Adds support to a given level in the T-tree (other than the top level).
    <P> Operates in a recursive manner to first find the appropriate level in
    the T-tree before processing the required level (when found).
    @param linkRef the reference to the current sub-branch of T-tree (start at
    top of tree)
    @param level the level marker, set to the required level at the start and
    then decremented by 1 on each recursion.
    @param endIndex the index into the item set array at which processing
    should be stopped. This should be the index of the attribute in the item
    set that is the parent T-tree node of the current level. On start this
    will usually be the length of the input item set.
    @param itemSet the current itemset (record from data array) under
    consideration.
    @param supportSoFar the support value. */

    protected void addSupportToTtree(FuzzyTtreeNode[] linkRef, int level,
    			int endIndex, FuzzyDataItem[] itemSet, double supportSoFar) {
//System.out.print("addSupportToTtree: level = " + level + ", endIndex = " +
//endIndex + ", itemSet = ");
//outputItemSet(itemSet);
//System.out.println(", supSoFar = " + supportSoFar);

	// At right level;
	if (level == 1) {
	    // Step through itemSet
	    for (int index1=0;index1<endIndex;index1++) {
                short attNum = itemSet[index1].getItemNumber();
		// If valid node update, i.e. a non null node
		if (linkRef[attNum] != null) {
                    double support = supportSoFar*itemSet[index1].getFuzzyValue();
		    linkRef[attNum].support = linkRef[attNum].support+support;
		    numUpdates++;
		    }
		}
	    }

	// At wrong level
	else {
	    // Step through itemSet
	    for (int index=level-1;index<endIndex;index++) {
		// If child branch step down branch
		short attNum = itemSet[index].getItemNumber();
                double support = supportSoFar*itemSet[index].getFuzzyValue();
		if (linkRef[attNum] != null) {
		    if (linkRef[attNum].childRef != null)
		    	 addSupportToTtree(linkRef[attNum].childRef,
					       level-1,index,itemSet,support);
		    }
		}
	    }
	}

    /*---------------------------------------------------------------------- */
    /*                                                                       */
    /*                                 PRUNING                               */
    /*                                                                       */
    /*---------------------------------------------------------------------- */

    /* PRUNE LEVEL N */

    /** Prunes the given level in the T-tree. <P> Operates in a recursive
    manner to first find the appropriate level in the T-tree before processing
    the required level (when found). Pruning carried out according to value of
    <TT>minSupport</TT> field.
    @param linkRef The reference to the current sub-branch of T-tree (start at
    top of tree)
    @param level the level marker, set to the required level at the start and
    then decremented by 1 on each recursion.
    @return true if all nodes at a given level in the given branch of the
    T-tree have been pruned (in which case the t-tree generation processing can
    be stopped), false otherwise. */

    protected boolean pruneLevelN(FuzzyTtreeNode [] linkRef, int level) {
        int size = linkRef.length;
	// At right level;
	if (level == 1) {
	    boolean allUnsupported = true;
	    // Step through level and set to null where below min support
	    for (int index1=1;index1<size;index1++) {
	        if (linkRef[index1] != null) {
                    linkRef[index1].support=linkRef[index1].support/(double) numRows;
	            if (linkRef[index1].support < minSupport)
		    		linkRef[index1] = null;
	            else {
		        numFrequentSets++;
			allUnsupported = false;
			}
		    }
		}
	    return(allUnsupported);
	    }

	// Wrong level, Step through row
	for (int index1=level;index1<size;index1++) {
	    if (linkRef[index1] != null) {
		// If child branch step down branch
		if (linkRef[index1].childRef != null) {
		    if (pruneLevelN(linkRef[index1].childRef,level-1))
			    		linkRef[index1].childRef=null;
		    }
		}
	    }
	return(false);
	}

    /*---------------------------------------------------------------------- */
    /*                                                                       */
    /*                            LEVEL GENERATION                           */
    /*                                                                       */
    /*---------------------------------------------------------------------- */

    /* GENERATE LEVEL 2 */

    /** Generates level 2 of the T-tree. <P> The general
    <TT>generateLevelN</TT> method assumes we have to first find the right
    level in the T-tree, that is not necessary in this case of level 2. */

    protected void generateLevel2() {
	// Set next level flag
	nextLevelExists=false;

	// loop through top level (start at index 2 because cannot generate a
	// level from index 1 as there will be no proceeding attributes,
	// remember index 0 is unused.
	for (int index=2;index<startTtreeRef.length;index++) {
	    // If supported T-tree node (i.e. it exists)
	    if (startTtreeRef[index] != null) generateNextLevel(startTtreeRef,
	    				index,realloc2(null,(short) index));
	    }
	}
                

    /* GENERATE LEVEL N */

    /** Commences process of generating remaining levels in the T-tree (other
    than top and 2nd levels). <P> Proceeds in a recursive manner level by level
    until the required level is reached. Example, if we have a T-tree of the form:

    <PRE>
    (A) ----- (B) ----- (C)
               |         |
	       |         |
	      (A)       (A) ----- (B)
    </PRE><P>
    Where all nodes are supported and we wish to add the third level we would
    walk the tree and attempt to add new nodes to every level 2 node found.
    Having found the correct level we step through starting from B (we cannot
    add a node to A), so in this case there is only one node from which a level
    3 node may be attached.
    @param linkRef the reference to the current sub-branch of T-tree (start at
    top of tree).
    @param level the level marker, set to the required level at the start and
    then decremented by 1 on each recursion.
    @param itemSet the current itemset (T-tree node) under consideration. */

    protected void generateLevelN(FuzzyTtreeNode[] linkRef, int level,
    							short[] itemSet) {
	int localSize = linkRef.length;

	// Correct level
	if (level == 1) {
	    // Step through T-tree array in current branch at current level
	    for (int index=2;index<localSize;index++) {
	        // If T-tree node exists (i.e. is supported) add next level
	    	if (linkRef[index] != null) {
			generateNextLevel(linkRef,index,
					     realloc2(itemSet,(short) index));
		    }
		}
	    }

	// Wrong level
	else {
	    for (int index=level;index<localSize;index++) {
	        // If T-tree node exists and it is has a child node proceed
	        if (linkRef[index]!=null && linkRef[index].childRef!=null)
		               generateLevelN(linkRef[index].childRef,level-1,
		    			     realloc2(itemSet,(short) index));
		}
	    }
	}
    /* GENERATE NEXT LEVEL */

    /** Generates a new level in the T-tree from a given "parent" node. <P>
    Example 1, given the following:

    <PRE>
    (A) ----- (B) ----- (C)
               |         |
	       |         |
	      (A)       (A) ----- (B)
    </PRE><P>
    where we wish to add a level 3 node to node (B), i.e. the node {A}, we
    would proceed as follows:
    <OL>
    <LI> Generate a new level in the T-tree attached to node (B) of length
    one less than the numeric equivalent of B i.e. 2-1=1.
    <LI> Loop through parent level from (A) to node immediately before (B).
    <LI> For each supported parent node create an itemset label by combing the
    index of the parent node (e.g. A) with the complete itemset label for B ---
    {C,B} (note reverse order), thus for parent node (B) we would get a new
    level in the T-tree with one node in it --- {C,B,A} represented as A.
    <LI> For this node to be a candidate large item set its size-1 subsets must
    be supported, there are three of these in this example {C,A}, {C,B} and
    {B,A}. We know that the first two are supported because they are in the
    current branch, but {B,A} is in another branch. So we must generate this
    set and test it. More generally we must test all cardinality-1 subsets
    which do not include the first element. This is done using the method
    <TT>testCombinations</TT>.
    </OL>
    <P>Example 2, given:
    <PRE>
    (A) ----- (D)
               |
	       |
	      (A) ----- (B) ----- (C)
	                           |
				   |
				  (A) ----- (B)
    </PRE><P>
    where we wish to add a level 4 node (A) to (B) this would represent the
    complete label {D,C,B,A}, the N-1 subsets will then be {{D,C,B},{D,C,A},
    {D,B,A} and {C,B,A}}. We know the first two are supported because they are
    contained in the current sub-branch of the T-tree, {D,B,A} and {C,B,A} are
    not.
    </OL>
    @param parentRef the reference to the level in the sub-branch of the T-tree
    under consideration.
    @param endIndex the index of the current node under consideration.
    @param itemSet the complete label represented by the current node (required
    to generate further itemsets to be X-checked). */

    protected void generateNextLevel(FuzzyTtreeNode[] parentRef, int endIndex,
    			short[] itemSet) {
	parentRef[endIndex].childRef = new FuzzyTtreeNode[endIndex];	// New level
        short[] newItemSet;

	// Generate a level in Ttree
	FuzzyTtreeNode currentNode = parentRef[endIndex];

	// Loop through parent sub-level of siblings upto current node
	for (int index=1;index<endIndex;index++) {
	    // Check if "uncle" element is supported (i.e. it exists)
	    if (parentRef[index] != null) {
		// Create an appropriate itemSet label to test
	        newItemSet = realloc2(itemSet,(short) index);
		if (testCombinations(newItemSet)) {
		    currentNode.childRef[index] = new FuzzyTtreeNode();
		    nextLevelExists=true;
		    }
	        else currentNode.childRef[index] = null;
	        }
	    }
	}

    /*---------------------------------------------------------------------- */
    /*                                                                       */
    /*                        T-TREE SEARCH METHODS                          */
    /*                                                                       */
    /*---------------------------------------------------------------------- */

    /* GET CONFIDENCE */

    /** Calculates and returns the confidence for an AR given the antecedent
    item set and the support for the total item set.
    @param antecedent the antecedent (LHS) of the AR.
    @param support the support for the large itemset from which the AR is
    generated.
    @return the associated confidence value (as a precentage) correct to two
    decimal places. */

    protected double getConfidence(short[] antecedent, double support) {
        // Get support for antecedent
        double supportForAntecedent = (double)
				getFuzzySupportForItemSetInTtree(antecedent);

	// Return confidence
	double confidenceForAR = ((double) support/supportForAntecedent)*10000;
	int tempConf = (int) confidenceForAR;
	confidenceForAR = (double) tempConf/100;
	return(confidenceForAR);
	}

    /* GET SUPPORT FOT ITEM SET IN T-TREE */

    /** Commences process for finding the support value for the given item set
    in the T-tree (which is know to exist in the T-tree). <P> Used when
    generating Association Rules (ARs). Note that itemsets are stored in
    reverse order in the T-tree therefore the given itemset must be processed
    in reverse.
    @param itemSet the given itemset.
    @return returns the support value (0 if not found). */

    private double getFuzzySupportForItemSetInTtree(short[] itemSet) {
	int endInd = itemSet.length-1;

        // Test if endItem exists in top level.
        if (itemSet[endInd]>=startTtreeRef.length) return(0);

    	// Last element of itemset in Ttree (Note: Ttree itemsets stored in
	// reverse)
  	if (startTtreeRef[itemSet[endInd]] != null) {
	    // If "current index" is 0, then this is the last element (i.e the
	    // input is a 1 itemset)  and therefore item set found
	    if (endInd == 0) return(startTtreeRef[itemSet[0]].support);
	    // Otherwise continue down branch
	    else {
	    	FuzzyTtreeNode[] tempRef = startTtreeRef[itemSet[endInd]].childRef;
	        if (tempRef != null) return(getSupForIsetInTtree2(itemSet,
							   endInd-1,tempRef));
	    	// No further branch therefore rerurn 0
		else return(0);
		}
	    }
	// Item set not in Ttree thererfore return 0
    	else return(0);
	}


    /** Returns the support value for the given itemset if found in the T-tree
    and 0 otherwise. <P> Operates recursively.
    @param itemSet the given itemset.
    @param index the current index in the given itemset.
    @param linRef the reference to the current Fuzzy T-tree level.
    @return returns the support value (0 if not found). */

    private double getSupForIsetInTtree2(short[] itemSet, int index,
    							FuzzyTtreeNode[] linkRef) {
        // Element at "index" in item set exists in Ttree
  	if (linkRef[itemSet[index]] != null) {
  	    // If "current index" is 0, then this is the last element of the
	    // item set and therefore item set found
	    if (index == 0) return(linkRef[itemSet[0]].support);
	    // Otherwise continue provided there is a child branch to follow
	    else if (linkRef[itemSet[index]].childRef != null)
	    		          return(getSupForIsetInTtree2(itemSet,index-1,
	    		                    linkRef[itemSet[index]].childRef));
	    else return(0);
	    }
	// Item set not in Ttree therefore return 0
	else return(0);
    	}

    /* FIND ITEM SET IN T-TREE */

    /** Commences process of determining if an itemset exists in a T-tree. <P>
    Used to X-check existence of Ttree nodes when generating new levels of the
    Tree. Note that T-tree node labels are stored in "reverse", e.g. {3,2,1}.
    @param itemSet the given itemset (IN REVERSE ORDER).
    @return returns true if itemset found and false otherwise. */

    protected boolean findItemSetInTtree(short[] itemSet) {

    	// first element of itemset in Ttree (Note: Ttree itemsets stored in
	// reverse)
  	if (startTtreeRef[itemSet[0]] != null) {
    	    int lastIndex = itemSet.length-1;
	    // If "current index" is 0, then this is the last element (i.e the
	    // input is a 1 itemset) and therefore item set found
	    if (lastIndex == 0) return(true);
	    // Otherwise continue down branch
	    else if (startTtreeRef[itemSet[0]].childRef!=null) {
	        return(findItemSetInTtree2(itemSet,1,lastIndex,
			startTtreeRef[itemSet[0]].childRef));
	        }
	    else return(false);
	    }
	// Item set not in Ttree
    	else return(false);
	}
	
    /*-------------------------------------------------------------------*/
    /*                                                                   */
    /*                   INPUT FILE HANDLING METHODS                     */
    /*                                                                   */
    /*-------------------------------------------------------------------*/

    /* INPUT DATA SET */

    /** Commences process of getting input data.
    @param textArea the text area in the GUI used for output.
    @param fName the name of the input file to be read.  */

    public void inputDataSet(JTextArea textArea, File fName) {
    	// Set filePath instance field
	    filePath = fName;
        // Read the file (method below overwrites metjhod in Association Rules
        // Class
	    readFile(textArea);

	    // Once input file has been read, processed and stored in data array 
	    // count number of columns. Input format flag set during readFile process.
	    if (inputFormatOkFlag) {
	        countNumCols();
	        textArea.append("Number of columns = " + numCols + "\n");
	        // Set have data flag to true
	        haveDataFlag = true;
	        }
	    else {
	        textArea.append("Operation failed!\n");
	        dataArray=null;
	        }
	    }

    /* COUNT NUMBER OF COLUMNS */
    /** Counts number of columns represented by input data. */

    protected void countNumCols() {
        short maxAttribute=0;

	    // Loop through data array
        for(int index=0;index<dataArray.length;index++) {
//System.out.println("index = " + index);
	        int lastIndex = dataArray[index].length-1;
	        short itemNum = dataArray[index][lastIndex].getItemNumber();
	        if (itemNum>maxAttribute) maxAttribute = itemNum;
	        }

	    numCols        = maxAttribute;
	    numOneItemSets = numCols; 	// default value only may change if pruned
	    }

    /* READ FILE */

    /** M ain method that reads input data from file specified in command line 
    argument. <P>Proceeds as follows:
    <OL>
    <LI>Gets number of lines in file, checking format of each line (space
    separated, etc), if incorrectly formatted line found
    <TT>inputFormatOkFlag</TT> set to <TT>false</TT>.
    <LI>Diminsions input array.
    <LI>Reads data
    </OL>
    @param textArea the text area in the gui used for output. */

    public void readFile(JTextArea textArea) {
        try {
	        // Assume format OK by default
	        inputFormatOkFlag = true;
	        // Dimension data structure. Get number of lines method is in 
	        // AssociationRules class, this calls the check line method below 
	        // which sets the input format OK flag.
	        numRows = getNumberOfLines(fileName);
            textArea.append("Number of records = " + numRows + "\n");
	        
	        // If OK dimension and populate  data array
	        if (inputFormatOkFlag) {
	            dataArray = new FuzzyDataItem[numRows][];
	            // Read file and process input line methods below.
		        textArea.append("Reading input file:\n" + filePath + "\n");
	            readInputDataSet();
		        }
		     // Else error   
	         else {
		        haveDataFlag = false;
		        textArea.append("Error reading file:\n" + filePath + "\n\n");
		        }
	         }
	catch(IOException ioException) {
	    JOptionPane.showMessageDialog(this,"Error reading File",
					"Error: ",JOptionPane.ERROR_MESSAGE);
	    textArea.append("Error reading File\n");
	    closeFile();
	    // Set have data flag to false
	    haveDataFlag = false;
	    }
	}

    /* CHECK LINE */

    /** Check whether given line from input file is of appropriate format
    (space separated integers or attribute number and weighting tuples), if 
    incorrectly formatted line found <TT>inputFormatOkFlag</TT> set to 
    <TT>false</TT>.
    @param counter the line number in the input file.
    @param str the current line from the input file. */

    protected void checkLine(int counter, String str) {

        for (int index=0;index <str.length();index++) {
            char c = str.charAt(index);
//System.out.println("c = '" + c + "'");
            if (!Character.isDigit(c) && c!='.' && c!='<' && c!='>' && c!=',' &&
                                               !Character.isWhitespace(c)) {
		        JOptionPane.showMessageDialog(null,"FILE INPUT ERROR:\n" +
				         "charcater on line " + counter + ", (\"" + c + "\") " +
					   "is not a digit, decimal point,\n',' (comma), '<', '>' " + 
					                                          "or white space.");
		        inputFormatOkFlag = false;
		        haveDataFlag = false;
		        break;
		        }
	        }
	    }


    /* READ INPUT DATA SET */
    /** Reads input data from given file.
    @param fName the given file name.  */

    protected void readInputDataSet(String fName) throws IOException {
	int rowIndex=0;

	// Open the file
	if (filePath==null) openFileName(fName);
	else openFilePath();

	// Get first row.
	String line = fileInput.readLine();

	// Process rest of file
	while (line != null) {
	    // Process line
	    if (!processInputLine(line,rowIndex)) inputFormatOkFlag = false;
	    // Increment first (row) index in 2-D data array
	    rowIndex++;
	    // get next line
            line = fileInput.readLine();
	    }

	// Close file
	closeFile();
	}

    /* PROCESS INPUT LINE */

    /**	Processes a line from the input file and places it in the
    <TT>dataArray</TT> structure.
    @param inputString the line to be processed from the input file
    @param rowIndex the index to the current location in the
    <TT>dataArray</TT> structure.
    @return true if successfull, false if empty record. */

    protected boolean processInputLine(String inputString, int rowIndex) {
        boolean lineOK = true;
//System.out.println("processInputLine: inputString = " + inputString + 
//", rowIndex = " + rowIndex);

        // Tokenize
	    StringTokenizer dataLine = new StringTokenizer(inputString);

        // Get number of tokens (items) dimension row in data array
        int numberOfTokens = dataLine.countTokens();
        dataArray[rowIndex] = new FuzzyDataItem[numberOfTokens];
        
        // Process tokens
        for (int colIndex=0;colIndex<numberOfTokens;colIndex++) {
            if (!processToken(dataLine.nextToken(),rowIndex,colIndex)) {
                lineOK=false;
                break;
                }
            }

        // End
        return(lineOK);
        }
        
    /** Process single token from input line.
    @param daraString the ntoken to be processed
    @param lineNumber the line number of the current line being process.
    @param index the token number
    @returm true if token successfully processed and false otherwise. */

    private boolean processToken(String dataString, int lineNum, int index) {
//System.out.println("processToken: dataString = " + dataString + ", lineNum = " +
//lineNum + ", index = " + index);
        boolean formattingOK = false;
        
        // Check for '<'
        if (dataString.charAt(0)=='<') {
            int endIndex = dataString.indexOf('>');
            // Check for closing '>'
            if (endIndex>0) {
               int commaIndex = dataString.indexOf(',');
               if (commaIndex>0 && commaIndex<endIndex) {
                   // Get attribute string
                   String attString = dataString.substring(1,commaIndex);
                   String whtString = dataString.substring(commaIndex+1,
                                                               endIndex);
//System.out.println("attString = \"" + attString + "\", whtString = \"" +
//whtString + "\"");
                   short attNum = processTokenAttribute(attString,index,lineNum);
//System.out.println("attNum = " + attNum);
                   // Attribute number will be negative if not prperly formatted.
                   if (attNum>0) {
                       // Get weighting (returns a negative number if weight
                       // is not in the correct format)
                       double weight = processTokenWeighting(whtString,index,
                                                                     lineNum);
//System.out.println("weight = " + weight);
                       if (weight>0.0) {
                           dataArray[lineNum][index] = 
                                              new FuzzyDataItem(attNum,weight);
                           formattingOK = true;
                           }
                       // Else formatting not OK
                       }
                   // Else formatting not OK
                   }
               // Else formatting not OK
               else JOptionPane.showMessageDialog(null,"Error: Line number " + 
                      (lineNum+1) + ", item number " + (index+1) + " (\"" + dataString +
                   "\"),\nno separating ',' for tuple <N,M>!","Data Input Error.",
                                                    JOptionPane.ERROR_MESSAGE);
               }
            // Else formatting not OK
            else JOptionPane.showMessageDialog(null,"Error: Line number " + 
                     (lineNum+1) + ", item number " +  (index+1) + ", (\"" + dataString +
                      "\"),\nno closing '>' for tuple <N,M>!","Data Input Error.",
                                                    JOptionPane.ERROR_MESSAGE);
            }
        // otherwise token is a binary attribute
        else {
           // Check for closing '>' even if no opening '<'
           int endIndex = dataString.indexOf('>');
           if (endIndex>0) {
               JOptionPane.showMessageDialog(null,"Error: Line number " + 
                     (lineNum+1) + ", item number " +  (index+1) + ", (\"" + dataString +
                      "\"),\nno opening '<' for tuple <N,M>!","Data Input Error.",
                                                    JOptionPane.ERROR_MESSAGE);
                }
           else {
               short attNum = processTokenAttribute(dataString,index,lineNum);
               if (attNum>0) {
                   dataArray[lineNum][index] = new FuzzyDataItem(attNum,1.0);
                   formattingOK = true;
                   }
               // Else formatting not OK
               }
           }
        
        // End
        return(formattingOK);
        }

    /** Process attribute
    @param attString the binary attribute in the form of an input string.
    @param index the current index in the attribute array.
    @param lineNum the current line number.
    @return attribute number if successful, and -1 otherwise. */

    private short processTokenAttribute(String attString, int index,
                                                            int lineNum) {
//System.out.println("processTokenAttribute: attString = "  + attString + ", lineNum = " +
//lineNum + ", index = " + index);                                                         
        // try-catch block
        try {
            short attNum = new Short(attString).shortValue();
            // Check attribute is greater than prvious attribite (provided
            // such an attribute exists
            if (index>0) {
                int preAttNum = dataArray[lineNum][index-1].getItemNumber();
                if (attNum<=preAttNum) {
                    JOptionPane.showMessageDialog(null,"Error: Line number " + 
                         (lineNum+1) + ", item number " + (index+1) + " (\"" + 
                           attNum + "\"), atttibute\nnumber is not greater " + 
                              "than previous number (\"" + preAttNum + "\")!",
                               "Data Input Error.",JOptionPane.ERROR_MESSAGE);
                    return(-1);
                    }
                else return(attNum);
                }
            // Check if first attribute number that it is greater than zero
            else if (attNum<=0) {
                JOptionPane.showMessageDialog(null,"Error: Line number " + 
                          (lineNum+1) + ", item number " + (index+1) + " (\"" + 
                      attNum + "\"); atttibute\nnumber must be greater than " + 
                        "zero!","Data Input Error.",JOptionPane.ERROR_MESSAGE);
                return(-1);
                }
            // OK found attribute
            else return(attNum);
            }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,"Error: Line number " + 
                          (lineNum+1) + " item number " + (index+1) + " (\"" + 
                           attString + "\"); is not\n in the right format; " + 
                                       "expecting an integer greater than 0!",
                               "Data Input Error.",JOptionPane.ERROR_MESSAGE);
            return(-1);
            }
        }


    /** Process weighting
    @param dataString the binary attribute in the form of an input string.
    @param index the current index in the attribute array.
    @param lineNum the current line number.
    @return weighting if successful, and -1.0 otherwise. */

    private double processTokenWeighting(String whtString, int index,
                                                            int lineNum) {
        // try-catch block
        try {
            double weight = new Double(whtString).doubleValue();
            // Check
            if (weight>=0.0 && weight<=1.0) return(weight);
            else {
                JOptionPane.showMessageDialog(null,"Error: Line number " + 
                                  (lineNum+1) + ", item number " + (index+1) + 
                               ", weighting \"" + weight + "\";\nweighting " +
                           "must be between 0.0 and 1.0.","Data Input Error!",
                                                   JOptionPane.ERROR_MESSAGE);
                return(-1.0);
                }
            }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,"Error: Line number " + 
                                   (lineNum+1) + ", item number " + (index+1) + 
                      ", weighting \"" + whtString + "\"; weighting is not\n" + 
                  "in the right format; " + "expecting a double between 0.0 " + 
                     "and 1.0!","Data Input Error.",JOptionPane.ERROR_MESSAGE);
            return(-1);
            }
        }

    /* ------------------------------------------------------ */
    /*                                                        */
    /*        OUTPUT SCHEMA METHODS (GUI VERSIONS)            */
    /*                                                        */
    /* ------------------------------------------------------ */


    /** Check if number of attributes in output schema are same as number of
    attributes in input file. <P>If either the output schema or the input
    file has nor been loaded then method will return false.
    @return true if number of attributes are the same and false otherwise. */

    public boolean checkSchemaVdata() {
        boolean schemaAndDataAttsSame = true;

        // Check schema
        if (outputSchema==null) {
            JOptionPane.showMessageDialog(null,"No output schema file.",
	    	                 "CHECK SCHEMA v DATA ATTRIBUTES ERROR",
                                             JOptionPane.ERROR_MESSAGE);
            return(!schemaAndDataAttsSame);
            }

        // Check data array
        if (dataArray==null) {
            JOptionPane.showMessageDialog(null,"No input data file.",
	    	                 "CHECK SCHEMA v DATA ATTRIBUTES ERROR",
                                             JOptionPane.ERROR_MESSAGE);
            return(!schemaAndDataAttsSame);
            }

        // Check lengths.
        if (outputSchema.length==numCols) return(schemaAndDataAttsSame);
        else  {
            JOptionPane.showMessageDialog(null,"Number of attributes in " +
	                "schema file (" + outputSchema.length + ") not\n" +
                            "same as number of attributes in data file (" +
                    numCols + ")\n","CHECK SCHEMA v DATA ATTRIBUTES ERROR",
                                                JOptionPane.ERROR_MESSAGE);
            return(!schemaAndDataAttsSame);
            }
        }
    /*----------------------------------------------------------------------- */
    /*                                                                        */
    /*                    ASSOCIATION RULE (AR) GENERATION                    */
    /*                                                                        */
    /*----------------------------------------------------------------------- */

    /** Loops through top level of T-tree as part of the AR generation
    process. */

    protected void generateARs2() {
//System.out.println("GenerateARs2: confidence = " + confidence);
	    // Loop
	    for (short index=1;index<=numOneItemSets;index++) {
	        if (startTtreeRef[index]!=null) {
	            if (startTtreeRef[index].support >= minSupport) {
	                short[] itemSetSoFar = new short[1];
		            itemSetSoFar[0] = index;
		            generateARs(itemSetSoFar,index,
		    		 	         startTtreeRef[index].childRef);
		            }
		        }
	        }
	    }

    /* GENERATE ASSOCIATION RULES */

    /** Continues process of generating association rules from a T-tree by
    recursively looping through T-tree level by level.
    @param itemSetSofar the label for a T-tree node as generated sofar.
    @param size the length/size of the current array level in the T-tree.
    @param linkRef the reference to the current array level in the T-tree. */

    protected void generateARs(short[] itemSetSofar, int size,
    							FuzzyTtreeNode[] linkRef) {
	// If no more nodes return
	if (linkRef == null) return;

	// Otherwise process
	for (int index=1; index < size; index++) {
	    if (linkRef[index] != null) {
	        if (linkRef[index].support >= minSupport) {
		    // Temp itemset
		    short[] tempItemSet = realloc2(itemSetSofar,(short) index);
		    // Generate ARs for current large itemset
		    generateARsFromItemset(tempItemSet,linkRef[index].support);
	            // Continue generation process
		    generateARs(tempItemSet,index,linkRef[index].childRef);
	            }
		}
	    }
	}

    /* GENERATE ASSOCIATION RULES */

    /** Generates all association rules for a given large item set found in a
    T-tree structure. <P> Called from <TT>generateARs</TT> method.
    @param itemSet the given large itemset.
    @param support the associated support value for the given large itemset. */

    protected void generateARsFromItemset(short[] itemSet, double support) {
    	// Determine combinations
	short[][] combinations = combinations(itemSet);

	// Loop through combinations
	for(int index=0;index<combinations.length;index++) {
            // Find complement of combination in given itemSet
	    short[] complement = complement(combinations[index],itemSet);
	    // If complement is not empty generate rule
	    if (complement != null) {
	        // Support confidence framework
                if (supConfFworkFlag) testRuleSupConfFwork(combinations[index],
                                                       complement,support);
	        // Support lift framework
                else if (supLiftFworkFlag)
                                      testRuleSupLiftFwork(combinations[index],
                                                           complement,support);
		}
	    }
	}

    /* TEST RULE USING SUPPORT CONFIDENCE FRAMEWORK */

    /** Tests given rule using support-confidence framework. */

    private void testRuleSupConfFwork(short[] antecedent, short[] consequent,
                                                       double totalSupport) {
        // Calculate confidence for antecedent
        double confidenceForAR = getConfidence(antecedent,totalSupport);
//System.out.println("confidenceForAR = " + confidenceForAR);

        // Test confidence
        if (confidenceForAR >= confidence) insertRuleIntoRulelist(antecedent,
		     		    consequent,confidenceForAR,totalSupport);
	}

    /* TEST RULE USING SUPPORT LIFT FRAMEWORK */

    /** Tests given rule using support-lift framework. */

    private void testRuleSupLiftFwork(short[] antecedent, short[] consequent,
                                                       double totalSupport) {
        // Calculate support for antecedent and consequent
	double anteSupport = (double) getFuzzySupportForItemSetInTtree(antecedent);
	double consSupport = (double) getFuzzySupportForItemSetInTtree(consequent);
        consSupport        = 100.0 * consSupport/numRows;

        // Calculate lift
        double confidence = 100.0*(((double) totalSupport)/anteSupport);
	double lift = confidence/consSupport;

	// If lift greater than 1 add to rule list, second ordinal value set
        // to 0.0.
	if (lift > 1.0) insertRuleIntoRulelist(antecedent,consequent,lift,0.0);
        }

    /* ---------------------------------------------------------------- */
    /*                                                                  */
    /*        REORDER DATA SET ACCORDING TO ATTRIBUTE FREQUENCY         */
    /*                                                                  */
    /* ---------------------------------------------------------------- */

    /* REORDER INPUT DATA: */

    /** Reorders input data according to frequency of single attributes. <P>
    Example, given the data set:
    <PRE>
    1 2 5
    1 2 3
    2 4 5
    1 2 5
    2 3 5
    </PRE>
    This would produce a countArray (ignore index 0 because there is no
    attributr number 0):
    <PRE>
    +---+---+---+---+---+---+
    |   | 1 | 2 | 3 | 4 | 5 |
    +---+---+---+---+---+---+
    |   | 3 | 5 | 2 | 1 | 4 |
    +---+---+---+---+---+---+
    </PRE>
    Which sorts to:
    <PRE>
    +---+---+---+---+---+---+
    |   | 2 | 5 | 1 | 3 | 4 |
    +---+---+---+---+---+---+
    |   | 5 | 4 | 3 | 2 | 1 |
    +---+---+---+---+---+---+
    </PRE>
    Giving rise to the conversion Array of the form (no index 0):
    <PRE>
    +---+---+---+---+---+---+
    |   | 3 | 1 | 4 | 5 | 2 |
    +---+---+---+---+---+---+
    |   | 3 | 5 | 2 | 1 | 4 |
    +---+---+---+---+---+---+
    </PRE>
    Note that the first row gives the new attribute number (old attribute
    number is the index). The second row here are the counts used to identify
    the ordering but which now no longer play a role in the conversion
    exercise. Thus the new column (attriburte) number for column/attribute 1 is
    column 3 (i.e. the first vale at index 1). The reconversion array will be
    of the form (values are the indexes from the conversion array while indexes
    represent the first vlaue from the conversion array):
    <PRE>
    +---+---+---+---+---+---+
    |   | 2 | 5 | 1 | 3 | 4 |
    +---+---+---+---+---+---+
    </PRE>
    For example to convert the attribute number 3 back to its original number
    we look up the value at index 3.
    */

    public void idInputDataOrdering() {

	// Count singles and store in countArray;
        FuzzyDataItem[] countArray = countOneItemSets();

	// Bubble sort count array on support value (second index)
	orderCountArray(countArray);
//for (int index=1;index<countArray.length;index++)
//System.out.println(countArray[index]);

//outputDataArray();
        // Define conversion and reconversion arrays
	defConvertArrays(countArray);
//outputConversionArrays();

	// Set sorted flag
	isOrderedFlag = true;
	}

    /* COUNT SINGLES */

    /** Counts number of occurrences of each single attribute in the
    input data.
    @return 2-D array where first row represents column numbers
    and second row represents support counts. */

    private FuzzyDataItem[] countOneItemSets() {
	// Dimension and initialize count array
	FuzzyDataItem[] countArray = new FuzzyDataItem[numCols+1];
	for (int index=0;index<countArray.length;index++) {
	    countArray[index] = new FuzzyDataItem((short) index,0.0);
	    }

	// Step through input data array counting singles and incrementing
	// appropriate element in the count array
	for(int rowIndex=0;rowIndex<dataArray.length;rowIndex++) {
	     if (dataArray[rowIndex] != null) {
		for (int colIndex=0;colIndex<dataArray[rowIndex].length;colIndex++) {
		    int countIndex = dataArray[rowIndex][colIndex].getItemNumber();
		    double fValue  = dataArray[rowIndex][colIndex].getFuzzyValue();
                    countArray[countIndex].incValue(fValue);
		    }
		}
	    }

        // Calculate support
        for (int index=0;index<countArray.length;index++) {
            countArray[index].divideValue((double) numRows);
            }

	// Return
	return(countArray);
	}


    /* ORDER COUNT ARRAY */

    /** Bubble sorts count array produced by <TT>countOneItemSets</TT> method
    so that array is ordered according to frequency of single items.
    @param countArray The 2-D array returned by the <TT>countOneItemSets</TT>
    method. */

    private void orderCountArray(FuzzyDataItem[] countArray) {
        int attribute, quantity;
        boolean isOrdered;
        int index;

        do {
	    isOrdered = true;
            index     = 1;
            while (index < (countArray.length-1)) {
                double value1 = countArray[index].getFuzzyValue();
                double value2 = countArray[index+1].getFuzzyValue();
                if (value1 >= value2) index++;
	        else {
	            isOrdered=false;
                    // Swap
		    short attribute1 = countArray[index].getItemNumber();
		    short attribute2 = countArray[index+1].getItemNumber();
                    countArray[index].setItemNumAndfValue(attribute2,value2);
                    countArray[index+1].setItemNumAndfValue(attribute1,value1);
	            // Increment index
		    index++;
	            }
	  	}
	    } while (isOrdered==false);
    	}

    /* DEFINE CONVERSION ARRAYS: */

    /** Defines conversion and reconversion arrays.
    @param countArray The array sorted by the <TT>orderCcountArray</TT>
    method.*/

    protected void defConvertArrays(FuzzyDataItem[] countArray) {
	// Dimension arrays
	conversionArray   = new FuzzyDataItem[numCols+1];
        reconversionArray = new short[numCols+1];

	// Assign values by processing the count array which has now been
	// ordered.
	for(int index=1;index<countArray.length;index++) {
	    short attNum  = countArray[index].getItemNumber();
            double fValue = countArray[index].getFuzzyValue();
            conversionArray[attNum] = new FuzzyDataItem((short) index,fValue);
	    reconversionArray[index] = attNum;
	    }
//outputDataArray();

	// Diagnostic ouput if desired
	//outputConversionArrays();
	}

    /** Recasts the contents of the (input) data array so that each record is
    ordered according to conversion array.
    <P>Proceed as follows:

    1) For each record in the data array. Create an empty new itemSet array.
    2) Place into this array attribute/column numbers that correspond to the
       appropriate equivalents contained in the conversion array.
    3) Reorder this itemSet and return into the data array. */

    public void recastInputData() {
        // Step through data array using loop construct
        for(int rowIndex=0;rowIndex<dataArray.length;rowIndex++) {
	    // For each element in the itemSet replace with attribute number
	    // from conversion array
	    for(int colIndex=0;colIndex<dataArray[rowIndex].length;colIndex++) {
	        short oldAttNum = dataArray[rowIndex][colIndex].getItemNumber();
	        short newAttNum = conversionArray[oldAttNum].getItemNumber();
		dataArray[rowIndex][colIndex].setItemNumber(newAttNum);
		}
	    // Sort itemSet and return to data array
	    sortItemSet(dataArray[rowIndex]);
	    }
	}

    /* RECAST INPUT DATA AND REMOVE UNSUPPORTED SINGLE ATTRIBUTES. */

    /** Recasts the contents of the data array so that each record is
    ordered according to ColumnCounts array and excludes non-supported
    elements. <P> Proceed as follows:

    1) For each record in the data array. Create an empty new itemSet array.
    2) Place into this array any column numbers in record that are
       supported at the index contained in the conversion array.
    3) Assign new itemSet back into to data array */

    public void recastInputDataAndPruneUnsupportedAtts() {

	// Step through data array using loop construct
        for(int rowIndex=0;rowIndex<dataArray.length;rowIndex++) {
	    // Check for empty row
	    if (dataArray[rowIndex]!= null) {
	        FuzzyDataItem[] itemSet = null;
	        // For each element in the current record find if supported with
	        // reference to the conversion array. If so add to "itemSet".
	    	for(int colIndex=0;colIndex<dataArray[rowIndex].length;colIndex++) {
	            short oldAttNum = dataArray[rowIndex][colIndex].getItemNumber();
		    // Check support
		    if (conversionArray[oldAttNum].getFuzzyValue() >= minSupport) {
	                short newAttNum   = conversionArray[oldAttNum].getItemNumber();
	                double fuzzyValue =
                                         dataArray[rowIndex][colIndex].getFuzzyValue();
                        FuzzyDataItem newItem =
                                              new FuzzyDataItem(newAttNum,fuzzyValue);
                        itemSet = reallocInsert(itemSet,newItem);
		        }
		    }
	        // Return new item set to data array
	        dataArray[rowIndex] = itemSet;
	 	}
	    }

	// Set isPrunedFlag (used with GUI interface)
	isPrunedFlag=true;
	// Reset number of one item sets field
	numOneItemSets = getNumSupOneItemSets();
	}

    /* GET NUM OF SUPPORTE ONE ITEM SETS */
    /** Gets number of supported single item sets (note this is not necessarily
    the same as the number of columns/attributes in the input set).
    @return Number of supported 1-item sets */

    protected int getNumSupOneItemSets() {
        int counter = 0;

	// Step through conversion array incrementing counter for each
	// supported element found
	for (int index=1;index < conversionArray.length;index++) {
	    if (conversionArray[index].getFuzzyValue()>=minSupport) counter++;
	    }

	// Return
	return(counter);
	}

    /** Reconverts given item set according to contents of reconversion array.
    @param itemSet the fgiven itemset.
    @return the reconverted itemset. */

    protected FuzzyDataItem[] reconvertItemSet(FuzzyDataItem[] itemSet) {
        // If no conversion return orginal item set
	if (reconversionArray==null) return(itemSet);

	// If item set null return null
	if (itemSet==null) return(null);

	// Define new item set
	FuzzyDataItem[] newItemSet = new FuzzyDataItem[itemSet.length];

	// Copy
	for(int index=0;index<newItemSet.length;index++) {
	    short oldAttNum   = itemSet[index].getItemNumber();
            double fuzzyValue = itemSet[index].getFuzzyValue();
	    short newAttNum   = reconversionArray[oldAttNum];
            newItemSet[index] = new FuzzyDataItem(newAttNum,fuzzyValue);
	    }

	// Return
	return(newItemSet);
        }


    /* SORT ITEM SET: Given an unordered itemSet, sort the set */

    /** Sorts an unordered item set.
    @param itemSet the given item set. */

    protected void sortItemSet(FuzzyDataItem[] itemSet) {
        FuzzyDataItem temp;
        boolean isOrdered;
        int index;

        do {
	    isOrdered = true;
            index     = 0;
            while (index < (itemSet.length-1)) {
                if (itemSet[index].getItemNumber() <=
                                       itemSet[index+1].getItemNumber()) index++;
	        else {
	            isOrdered=false;
                    // Swap
		    temp = itemSet[index];
	            itemSet[index] = itemSet[index+1];
                    itemSet[index+1] = temp;
	            // Increment index
		    index++;
	            }
	  	}
	    } while (isOrdered==false);
    	}

    /*----------------------------------------------------------------------- */
    /*                                                                        */
    /*                        GET AND SET METHODS                             */
    /*                                                                        */
    /*----------------------------------------------------------------------- */

    /* GET NUMBER OF FREQUENT SETS */
    /** Returns number of frequent/large (supported) sets in T-tree.
    @return the number of support (frequent/large) sets. */

    public int getNumFreqSets() {
        return(numFrequentSets);
	}

    /* SET MIN SUPPORT */
    /** Sets the minimum support (in terms or number of rows in data set)
    setting. */

    public void setMinSupport() {
	minSupport = support;
	}                         

    /* GET START OF T-TRRE */
    /** Returns the reference to the start of the T-tree.
    @return The start of the T-tree. */

    public FuzzyTtreeNode[] getStartOfTtree() {
    	return(startTtreeRef);
	}

    /* ----------------------------------------------------- */
    /*                                                       */
    /*             BOOLEAN ITEM SET METHODS ETC.             */
    /*                                                       */
    /* ----------------------------------------------------- */

    /* CHECK ITEM SET: */

    /** Determines relationship between two item sets (same, parent,
    before, child or after).
    @param itemSet1 the first item set (from input data).
    @param itemSet2 the second item set to be compared with first (from P-tree).
    @return 1 = same, 2 = itemSet2 is parent of itemSet1, 3 = itemSet2
    lexicographically before itemSet1, 4 = itemSet2 is child of itemSet1,
    and 5 = itemSet2 lexicographically after itemSet1. */

    protected int checkItemSets(FuzzyDataItem[] itemSet1, short[] itemSet2) {
        // Check if the same
	if (isEqual(itemSet1,itemSet2)) return(1);

	// Check whether before or after and subset/superset.
	if (isBefore(itemSet1,itemSet2)) {
	    if (isSubset(itemSet1,itemSet2)) return(2);
	    else return(3);
	    }
        if (isSubset(itemSet2,itemSet1)) return(4);
        return(5);
        }

    /* EQUALITY CHECK */

    /** Checks whether two item sets are the same.
    @param itemSet1 the first item set (from input data).
    @param itemSet2 the second item set to be compared with first (from P-tree).
    @return true if itemSet1 is equal to itemSet2, and false otherwise. */

    protected boolean isEqual(FuzzyDataItem[] itemSet1, short[] itemSet2) {
	// If no itemSet2 (i.e. itemSet2 is null return false)
	if (itemSet2 == null) return(false);

	// Compare sizes, if not same length they cannot be equal.
	int length1 = itemSet1.length;
	int length2 = itemSet2.length;
	if (length1 != length2) return(false);

        // Same size compare elements
        for (int index=0;index < length1;index++) {
	    if (itemSet1[index].getItemNumber() != itemSet2[index]) return(false);
	    }

        // itemSet the same.
        return(true);
        }

    /* BEFORE CHECK */

    /** Checks whether one item set is lexicographically before a second
    item set.
    @param itemSet1 the first item set (from input data).
    @param itemSet2 the second item set to be compared with first (from P-tree).
    @return true if itemSet1 is less than or equal (before) itemSet2 and
    false otherwise. Note that before here is not numerical but lexical,
    i.e. {1,2} is before {2} */

    public static boolean isBefore(FuzzyDataItem[] itemSet1, short[] itemSet2) {
        int length2 = itemSet2.length;

	// Compare elements
	for(int index1=0;index1<itemSet1.length;index1++) {
	    if (index1 == length2) return(false);
            // itemSet2 is a proper subset of itemSet1
    	    if (itemSet1[index1].getItemNumber() < itemSet2[index1]) return(true);
	    if (itemSet1[index1].getItemNumber() > itemSet2[index1]) return(false);
	    }

	// Return true
	return(true);
	}

    /* SUBSET CHECK */

    /** Checks whether one item set is subset of a second item set.
    @param itemSet1 the first item set (from input data).
    @param itemSet2 the second item set to be compared with first (from P-tree).
    @return true if itemSet1 is a subset of itemSet2, and false otherwise.
    */

    protected boolean isSubset(short[] itemSet1, FuzzyDataItem[] itemSet2) {
	// Check for empty itemsets
	if (itemSet1==null) return(true);
	if (itemSet2==null) return(false);

	// Loop through itemSet1
	for(int index1=0;index1<itemSet1.length;index1++) {
	    if (notMemberOf(itemSet1[index1],itemSet2)) return(false);
	    }

	// itemSet1 is a subset of itemSet2
	return(true);
	}       

    protected boolean isSubset(FuzzyDataItem[] itemSet1, short[] itemSet2) {
	// Check for empty itemsets
	if (itemSet1==null) return(true);
	if (itemSet2==null) return(false);

	// Loop through itemSet1
	for(int index1=0;index1<itemSet1.length;index1++) {
	    if (notMemberOf(itemSet1[index1],itemSet2)) return(false);
	    }

	// itemSet1 is a subset of itemSet2
	return(true);
	}

    /* NOT MEMBER OF */

    /** Checks whether a particular element/attribute identified by a
    column number is not a member of the given item set.
    @param number the attribute identifier (column number).
    @param itemSet the given item set.
    @return true if first argument is not a member of itemSet, and false
    otherwise */

    protected boolean notMemberOf(short number, FuzzyDataItem[] itemSet) {
	// Loop through itemSet
	for(int index=0;index<itemSet.length;index++) {
	    if (number <  itemSet[index].getItemNumber()) return(true);
	    if (number == itemSet[index].getItemNumber()) return(false);
	    }

	// Got to the end of itemSet and found nothing, return true
	return(true);
	}

    protected boolean notMemberOf(FuzzyDataItem set, short[] itemSet) {
        short number = set.getItemNumber();

        // Loop through itemSet
	for(int index=0;index<itemSet.length;index++) {
	    if (number <  itemSet[index]) return(true);
	    if (number == itemSet[index]) return(false);
	    }

	// Got to the end of itemSet and found nothing, return true
	return(true);
	}

    /* CHECK FOR LEADING SUB STRING */

    /** Checks whether two itemSets share a leading substring.
    @param itemSet1 the first item set (from input data).
    @param itemSet2 the second item set to be compared with first (from P-tree).
    @return the substring if a shared leading substring exists, and null
    otherwise. */

    protected FuzzyDataItem[] checkForLeadingSubString(FuzzyDataItem[] itemSet1,
    							short[] itemSet2) {
        //int index3=0;
	FuzzyDataItem[] itemSet3 = null;

	// Loop through itemSets
	for(int index=0;index<itemSet1.length;index++) {
	    if (index == itemSet2.length) break;
	    if (itemSet1[index].getItemNumber() == itemSet2[index])
	    			itemSet3 = realloc1(itemSet3,itemSet1[index]);
	    else break;
	    }

	// Return

	return(itemSet3);
	}

    /* ----------------------------------------------- */
    /*                                                 */
    /*        ITEM SET INSERT AND ADD METHODS          */
    /*                                                 */
    /* ----------------------------------------------- */

    /* REALLOC INSERT */

    /** Resizes given item set so that its length is increased by one
    and new element inserted.
    @param oldItemSet the original item set
    @param newElement the new element/attribute to be inserted
    @return the combined item set */

    protected FuzzyDataItem[] reallocInsert(FuzzyDataItem[] oldItemSet,
                                                        FuzzyDataItem newElement) {
	// No old item set
	if (oldItemSet == null) {
	    FuzzyDataItem[] newItemSet = {newElement};
	    return(newItemSet);
	    }

	// Otherwise create new item set with length one greater than old
	// item set
	int oldItemSetLength = oldItemSet.length;
	FuzzyDataItem[] newItemSet = new FuzzyDataItem[oldItemSetLength+1];

	// Loop
	int index1;
	short newAttNum = newElement.getItemNumber();
	for (index1=0;index1<oldItemSetLength;index1++) {
	    short oldAttNum = oldItemSet[index1].getItemNumber();
	    if (newAttNum < oldAttNum) {
		newItemSet[index1] = newElement;
		// Copy rest
		for(int index2 = index1+1;index2<newItemSet.length;index2++)
			newItemSet[index2] = new FuzzyDataItem(oldItemSet[index2-1]);
		return(newItemSet);
		}
	    // Copy
            else newItemSet[index1] = new FuzzyDataItem(oldItemSet[index1]);
	    }

	// Add to end
	newItemSet[newItemSet.length-1] = newElement;

	// Return new item set
	return(newItemSet);
	}

    /* REALLOC 1 */

    /** Resizes given item set so that its length is increased by one
    and appends new element (identical to append method)
    @param oldItemSet the original item set
    @param newElement the new element/attribute to be appended
    @return the combined item set */

    protected FuzzyDataItem[] realloc1(FuzzyDataItem[] oldItemSet,
                                                          FuzzyDataItem newElement) {
	// No old item set
	if (oldItemSet == null) {
	    FuzzyDataItem[] newItemSet = new FuzzyDataItem[1];
            newItemSet[0]              = new FuzzyDataItem(newElement);
	    return(newItemSet);
	    }

	// Otherwise create new item set with length one greater than old
	// item set
	int oldItemSetLength = oldItemSet.length;
	FuzzyDataItem[] newItemSet = new FuzzyDataItem[oldItemSetLength+1];

	// Loop
	int index;
	for (index=0;index < oldItemSetLength;index++)
		newItemSet[index] = new FuzzyDataItem(oldItemSet[index]);
	newItemSet[index] = newElement;

	// Return new item set
	return(newItemSet);
	}

    /* REALLOC 2 */

    /** Resizes given array so that its length is increased by one element
    and new element added to front
    @param oldItemSet the original item set
    @param newElement the new element/attribute to be appended
    @return the combined item set */

    /*protected FuzzyDataItem[] realloc2(FuzzyDataItem[] oldItemSet,
                                          FuzzyDataItem newElement) {

	// No old array
	if (oldItemSet == null) {
	    FuzzyDataItem[] newItemSet = {newElement};
	    return(newItemSet);
	    }

	// Otherwise create new array with length one greater than old array
	int oldItemSetLength = oldItemSet.length;
	FuzzyDataItem[] newItemSet = new FuzzyDataItem[oldItemSetLength+1];

	// Loop
	newItemSet[0] = newElement;
	for (int index=0;index < oldItemSetLength;index++)
		newItemSet[index+1] = oldItemSet[index];

	// Return new array
	return(newItemSet);
	}  */

    /* REALLOC 3 */

    /** Resizes given array so that its length is decreased by one element
    and first element removed
    @param oldItemSet the original item set
    @return the shortened item set */

    protected FuzzyDataItem[] realloc3(FuzzyDataItem[] oldItemSet) {

	// If old item set comprises one element return null
	if (oldItemSet.length == 1) return null;

	// Create new array with length one greater than old array
	int newItemSetLength = oldItemSet.length-1;
	FuzzyDataItem[] newItemSet = new FuzzyDataItem[newItemSetLength];

	// Loop
	for (int index=0;index < newItemSetLength;index++)
		newItemSet[index] = new FuzzyDataItem(oldItemSet[index+1]);

	// Return new array
	return(newItemSet);
	}

    /* REALLOC 4 */

    /** Resize given array so that its length is decreased by size of
    second array (which is expected to be a leading subset of the first)
    and remove second array.
    @param oldItemSet The first item set.
    @param array2 The leading subset of the <TT>oldItemSet</TT>.
    @return Revised item set with leading subset removed. */

    protected short[] realloc4(short[] oldItemSet, FuzzyDataItem[] array2) {
        int array2length     = array2.length;
	int newItemSetLength = oldItemSet.length-array2length;

	// Create new array
	short[] newItemSet = new short[newItemSetLength];

	// Loop
	for (int index=0;index < newItemSetLength;index++)
		newItemSet[index] = oldItemSet[index+array2length];

	// Return new array
	return(newItemSet);
	}
   
   protected FuzzyDataItem[] realloc4(FuzzyDataItem[] oldItemSet, short[] array2) {
        int array2length     = array2.length;
	int newItemSetLength = oldItemSet.length-array2length;

	// Create new array
	FuzzyDataItem[] newItemSet = new FuzzyDataItem[newItemSetLength];

	// Loop
	for (int index=0;index < newItemSetLength;index++)
		newItemSet[index] = new FuzzyDataItem(oldItemSet[index+array2length]);

	// Return new array
	return(newItemSet);
	}

   protected FuzzyDataItem[] realloc4(FuzzyDataItem[] oldItemSet,
                                                      FuzzyDataItem[] array2) {
        int array2length     = array2.length;
	int newItemSetLength = oldItemSet.length-array2length;

	// Create new array
	FuzzyDataItem[] newItemSet = new FuzzyDataItem[newItemSetLength];

	// Loop
	for (int index=0;index < newItemSetLength;index++)
		newItemSet[index] = new FuzzyDataItem(oldItemSet[index+array2length]);

	// Return new array
	return(newItemSet);
	}

    /* ----------------------------------------------------- */
    /*                                                       */
    /*                   GUI OUTPUT METHODS                  */
    /*                                                       */
    /* ----------------------------------------------------- */

    /* OUTPUT DATA TABLE */

    /** Outputs stored input data set; initially read from input data file, but
    may be reordered or pruned if desired by a particular application.
    @param textArea the text area. */

    public void outputDataArray(JTextArea textArea) {
        if (isPrunedFlag) textArea.append("DATA SET (Ordered and Pruned)\n");
	else {
	    if (isOrderedFlag) textArea.append("DATA SET (Ordered)\n");
	    else textArea.append("DATA SET\n");
	    }
        textArea.append("<Item Number, Fuzzy Membership>\n");
        textArea.append("-------------------------------\n");

        // Loop
	for(int index=0;index<dataArray.length;index++) {
	    outputItemSet(textArea,dataArray[index]);
	    textArea.append("\n");
	    }
	}

    /** Outputs stored input data set; initially read from input data file, but
    may be reordered or pruned if desired by a particular application.  */

    public void outputDataArray() {
        if (isPrunedFlag) System.out.print("DATA SET (Ordered and Pruned)\n" +
					"-----------------------------\n");
	else {
	    if (isOrderedFlag) System.out.print("DATA SET (Ordered)\n" +
					"------------------\n");
	    else System.out.print("DATA SET\n" + "--------\n");
	    }

	for(int index=0;index<dataArray.length;index++) {
	    outputItemSet(dataArray[index]);
	    System.out.print("\n");
	    }
	}

    /** Outputs a given item set.
    @param textArea the text area.
    @param itemSet the given item set. */

    protected void outputItemSet(JTextArea textArea, FuzzyDataItem[] itemSet) {
	// Check for emty set
	if (itemSet == null) textArea.append(" null ");
	// Process
	else {
	    // Reconvert where input dataset has been reordered and possible
	    // pruned.
	    FuzzyDataItem[] tempItemSet = reconvertItemSet(itemSet);
	    // Loop through item set elements
            int counter = 0;
	    for (int index=0;index<tempItemSet.length;index++) {
	        if (counter == 0) {
	    	    counter++;
		    textArea.append(" {");
		    }
	        else textArea.append(" ");
	        String s = tempItemSet[index].toString();
	        textArea.append(s);
		}
	    textArea.append("} ");
	    }
	}


    /** Outputs a given item set.
    @param itemSet the given item set. */

    protected void outputItemSet(FuzzyDataItem[] itemSet) {
	// Check for emty set
	if (itemSet == null) System.out.print(" null ");
	// Process
	else {
	    // Reconvert where input dataset has been reordered and possible
	    // pruned.
	    FuzzyDataItem[] tempItemSet = reconvertItemSet(itemSet);
	    // Loop through item set elements
            int counter = 0;
	    for (int index=0;index<tempItemSet.length;index++) {
	        if (counter == 0) {
	    	    counter++;
		    System.out.print(" {");
		    }
	        else System.out.print(" ");
	        String s = tempItemSet[index].toString();
	        System.out.print(s);
		}
	    System.out.print("} ");
	    }
	}

    /* OUTPUT DATA TABLE SCHEMA */
    /** Outputs stored input data set as a set of output schema labels.
    @param textArea the text area. */

    public void outputDataArraySchema(JTextArea textArea) {
        // Check that we have a schema
        if (!hasOutputSchemaFlag) {
            textArea.append("No schema found!\nOperation failed\n");
            return;
            }
        
        // Output
        if (isPrunedFlag) textArea.append("DATA SET (pruned)\n" +
					"-----------------------------\n");
	else {
	    if (isOrderedFlag) textArea.append("DATA SET (Ordered)\n" +
					"------------------\n");
	    else textArea.append("DATA SET\n" + "--------\n");
	    }

	for(int index=0;index<dataArray.length;index++) {
	    outputItemSetSchema(textArea,dataArray[index]);
	    textArea.append("\n");
	    }
	}

    /** Outputs a given item set using output schema labels.
    @param textArea the text area.
    @param itemSet the given item set. */

    protected void outputItemSetSchema(JTextArea textArea, FuzzyDataItem[] itemSet) {
	// Check for emty set
	if (itemSet == null) textArea.append(" {} ");
	// Process
	else {
	    // Reconvert where input dataset has been reordered and possible
	    // pruned.
	    FuzzyDataItem[] tempItemSet = reconvertItemSet(itemSet);
	    // Loop through item set elements
            int counter = 0;
	    for (int index=0;index<tempItemSet.length;index++) {
	        
	        if (counter == 0) {
	    	    counter++;
		    textArea.append(" {");
		    }
	        else textArea.append(" ");
	        String schemaLabel = outputSchema[tempItemSet[index].getItemNumber()-1];
	        String s           = tempItemSet[index].toString(schemaLabel);
	        textArea.append(s);
		}
	    textArea.append("} ");
	    }
	}
	
    /* ------------- */
    /* OUTPUT T-TREE */
    /* ------------- */

    /** Commences process of outputting T-tree structure contents to screen
    (GUI version).
    @param textArea the given instance of the <TT>JTextArea</TT> class. */

    public void outputTtree(JTextArea textArea) {
	int number = 1;

        // Start
	textArea.append("T-TREE:\n-------\n");
	textArea.append("Format: [N] {I} = S, where N is the node " +
		"number, I is the item set and S the support.\n");

        // Check
        if (startTtreeRef==null) {
            textArea.append("Ttree empty!\n--------------------------------");
            return;
            }

	// Loop
	for (short index=1; index < startTtreeRef.length; index++) {
	    if (startTtreeRef[index] !=null) {
	        // Create itemset so far array
                short[] itemSetSofar = new short[1];
	        itemSetSofar[0] = index;
                // Output
                textArea.append("[" + number + "]");
                outputItemSet(textArea,itemSetSofar);
	        textArea.append("= " + twoDecPlaces(startTtreeRef[index].support) +
                                   "\n");
	        outputTtree(textArea,new Integer(number).toString(),
                                itemSetSofar,startTtreeRef[index].childRef);
		number++;
		}
	    }

	// End
        textArea.append("------------------------------------\n");
        }

    /** Continue process of outputting T-tree. <P> Operates in a recursive
    manner.
    @param textArea the given instance of the <TT>JTextArea</TT> class.
    @param number the ID number of a particular node.
    @param itemSetSofar the label for a T-tree node as generated sofar.
    @param linkRef the reference to the current array level in the T-tree. */

    private void outputTtree(JTextArea textArea, String number,
                                short[] itemSetSofar, FuzzyTtreeNode[] linkRef) {
	// Set output local variables.
	int num=1;
	number = number + ".";

	// Check for empty branch/sub-branch.
	if (linkRef == null) return;

	// Loop through current level of branch/sub-branch.
	for (short index=1;index<linkRef.length;index++) {
	    if (linkRef[index] != null) {
	        short[] newItemSet = realloc2(itemSetSofar,index);
	        textArea.append("[" + number + num + "]");
	        outputItemSet(textArea,newItemSet);
	        textArea.append("= " + twoDecPlaces(linkRef[index].support) + "\n");
	        outputTtree(textArea,number + num,newItemSet,
                                                    linkRef[index].childRef);
	        num++;
		}
	    }
	}

    /** Commences process of outputting T-tree structure contents to screen. */

    public void outputTtree() {
	int number = 1;

	// Start
	System.out.println("T-TREE:\n-------");
	System.out.println("Format: [N] {I} = S, where N is the node " +
		"number, I is the item set and S the support.");

        // Check
        if (startTtreeRef==null) {
            System.out.println("Ttree empty!");
            return;
            }

	// Loop
	for (short index=1;index<startTtreeRef.length;index++) {
	    if (startTtreeRef[index] !=null) {
	        // Create itemset so far array
                short[] itemSetSofar = new short[1];
	        itemSetSofar[0] = index;
                // Output
                System.out.print("[" + number + "]");
                outputItemSet(itemSetSofar);
	        System.out.println("= " + twoDecPlaces(startTtreeRef[index].support));
	        outputTtree(new Integer(number).toString(),itemSetSofar,
			                        startTtreeRef[index].childRef);
		number++;
		}
	    }

	// End
        System.out.println("--------------------------------");
	}

    /** Continue process of outputting T-tree. <P> Operates in a recursive
    manner.
    @param number the ID number of a particular node.
    @param itemSetSofar the label for a T-tree node as generated sofar.
    @param linkRef the reference to the current array level in the T-tree. */

    private void outputTtree(String number, short[] itemSetSofar,
    				FuzzyTtreeNode[] linkRef) {
	// Set output local variables.
	int num=1;
	number = number + ".";

	// Check for empty branch/sub-branch.
	if (linkRef == null) return;

	// Loop through current level of branch/sub-branch.
	for (short index=1;index<linkRef.length;index++) {
	    if (linkRef[index] != null) {
	        short[] newItemSet = realloc2(itemSetSofar,index);
	        System.out.print("[" + number + num + "]");
                outputItemSet(newItemSet);
	        System.out.println("= " + twoDecPlaces(linkRef[index].support));
	        outputTtree(number + num,newItemSet,linkRef[index].childRef);
	        num++;
		}
	    }
	}

    /* --------------------------- */
    /*  OUTPUT T-TREE STATISTICS   */
    /* --------------------------- */
    /** Commences the process of outputting T-tree statistics (for diagnostic
    purposes): (a) Storage, (b) Number of nodes on P-tree, (c) number of
    partial support increments (updates) and (d) generation time.
    @param textArea the given instance of the <TT>JTextArea</TT> class. */

    public void outputTtreeStats(JTextArea textArea) {
        textArea.append("T-TREE STATISTICS (ARM)\n-----------------\n");
	textArea.append(numLevelsInTtree + " Levels in T-tree created\n");
        textArea.append(FuzzyTtreeNode.getNumberOfNodes() + " Total # nodes " +
                                                       "created\n");
	textArea.append(numUpdates + " Total # support value increments\n");
	textArea.append(numFrequentSets + " Frequent sets\n");
        textArea.append(calculateStorage() + " Total storage (Bytes)" +
                                              " on completion\n");
	textArea.append("-----------------------------------\n");
        }

    /* CALCULATE STORAGE */
    /** Commences process of calculating storage requirements for  T-tree. */

    protected int calculateStorage() {
        // If emtpy tree (i.e. no supported sets) return 0
	if (startTtreeRef ==  null) return(0);

	/* Step through top level */
	int storage = 4;	// For element 0
	for (int index=1; index<=numOneItemSets; index++) {
	    if (startTtreeRef[index]!=null) storage = storage + 12 +
	    		calculateStorage(0,startTtreeRef[index].childRef);
	    else storage = storage+4;
	    }

        // Return
	return(storage);
	}

    /** Calculate storage requirements for a sub-branch of the T-tree.
    @param localStorage the storage as calculated sofar (set to 0 at start).
    @param linkRef the reference to the current sub-branch of the T-tree. */

    private int calculateStorage(int localStorage, FuzzyTtreeNode[] linkRef) {
	if (linkRef == null) return(0);

	// Loop through current level in current sub-nranch of T-tree
	for (int index=1; index < linkRef.length; index++) {
	    if (linkRef[index] !=null) localStorage = localStorage + 12 +
	    		calculateStorage(0,linkRef[index].childRef);
	    else localStorage = localStorage + 4;
	    }

	 /* Return */
	 return(localStorage+4);	// For element 0
	 }


    /* ----------------------- */
    /*  OUTPUT FREQUENT SETS   */
    /* ----------------------- */

    /** Commences the process of outputting the frequent sets contained in
    the T-tree (GUI version).
    @param textArea the given instance of the <TT>JTextArea</TT> class. */

    public void outputFrequentSets(JTextArea textArea) {
	int number = 1;

        // Labels
	textArea.append("Format: [N] {I} = S, where N is a sequential " +
		"number, I is the item set and S the support.\n");

	// Loop
	for (short index=1; index <= numOneItemSets; index++) {
	    if (startTtreeRef[index] !=null) {
	        if (startTtreeRef[index].support >= minSupport) {
	            short[] itemSetSofar = new short[1];
	            itemSetSofar[0] = index;
	            textArea.append("[" + number + "]");
                    outputItemSet(textArea,itemSetSofar);
		    textArea.append("= " + (twoDecPlaces(startTtreeRef[index].support)) + "\n");
	            number = outputFrequentSets(textArea,number+1,itemSetSofar,
		    			  index,startTtreeRef[index].childRef);
		    }
		}
	    }
	}

    /** Outputs T-tree frequent sets (GUI version). <P> Operates in a
    recursive manner.
    @param textArea the given instance of the <TT>JTextArea</TT> class.
    @param number the number of frequent sets so far.
    @param itemSetSofar the label for a T-treenode as generated sofar.
    @param size the length/size of the current array level in the T-tree.
    @param linkRef the reference to the current array level in the T-tree.
    @return the incremented (possibly) number the number of frequent sets so
    far. */

    private int outputFrequentSets(JTextArea textArea, int number,
                        short[] itemSetSofar, int size, FuzzyTtreeNode[] linkRef) {

	// No more nodes
	if (linkRef == null) return(number);

	// Otherwise process
	for (short index=1; index < size; index++) {
	    if (linkRef[index] != null) {
	        if (linkRef[index].support >= minSupport) {
	            short[] newItemSet = realloc2(itemSetSofar,index);
	            textArea.append("[" + number + "]");
                    outputItemSet(textArea,newItemSet);
                    textArea.append("= " + (twoDecPlaces(linkRef[index].support)) + "\n");
		    number = outputFrequentSets(textArea,number + 1,newItemSet,
                                                index,linkRef[index].childRef);
	            }
		}
	    }

	// Return
	return(number);
	}

    /** Commences the process of outputting the frequent sets contained in
    the T-tree as series of schema labels (GUI version).
    @param textArea the given instance of the <TT>JTextArea</TT> class. */

    public void outputFrequentSetsSchema(JTextArea textArea) {
        // Check that we have a schema
        if (!hasOutputSchemaFlag) {
            textArea.append("No schema found!\nOperation failed\n");
            return;
            }
	
	    // Proceed
	    int number = 1;
	textArea.append("Format: [N] {I} = S, where N is a sequential " +
		"number, I is the item set and S the support.\n");

	// Loop
	for (short index=1; index <= numOneItemSets; index++) {
	    if (startTtreeRef[index] !=null) {
	        if (startTtreeRef[index].support >= minSupport) {
	            short[] itemSetSofar = new short[1];
	            itemSetSofar[0] = index;
	            textArea.append("[" + number + "]");
                    outputItemSetSchema(textArea,itemSetSofar);
                    textArea.append("= " + (twoDecPlaces(startTtreeRef[index].support)) + "\n");
	            number = outputFrequentSetsSchema(textArea,number+1,
                          itemSetSofar,index,startTtreeRef[index].childRef);
		    }
		}
	    }

	// End

	System.out.println("\n");
	}

    /** Outputs T-tree frequent sets (GUI version). <P> Operates in a
    recursive manner.
    @param textArea the given instance of the <TT>JTextArea</TT> class.
    @param number the number of frequent sets so far.
    @param itemSetSofar the label for a T-treenode as generated sofar.
    @param size the length/size of the current array level in the T-tree.
    @param linkRef the reference to the current array level in the T-tree.
    @return the incremented (possibly) number the number of frequent sets so
    far. */

    private int outputFrequentSetsSchema(JTextArea textArea, int number,
                        short[] itemSetSofar, int size, FuzzyTtreeNode[] linkRef) {

	// No more nodes
	if (linkRef == null) return(number);

	// Otherwise process
	for (short index=1; index < size; index++) {
	    if (linkRef[index] != null) {
	        if (linkRef[index].support >= minSupport) {
	            short[] newItemSet = realloc2(itemSetSofar,index);
	            textArea.append("[" + number + "]");
                    outputItemSetSchema(textArea,newItemSet);
                    textArea.append("= " + (twoDecPlaces(linkRef[index].support)) + "\n");
		    number = outputFrequentSetsSchema(textArea,number + 1,
                                     newItemSet,index,linkRef[index].childRef);
	            }
		}
	    }

	// Return
	return(number);
	} 


    /* ------------------------ */
    /* OUTPUT CONVERSION ARRAYS */
    /* ------------------------ */

    /** Outputs conversion array (used to renumber columns for input data
    in terms of frequency of single attributes --- reordering will enhance
    performance for some ARM algorithms). */

    public void outputConversionArrays() {
        // Conversion array
        System.out.println("CONVERSION ARRAY\nindex = old attribute number\n" +
	                              "<new attribute number,support count>.");
	for(int index=1;index<conversionArray.length;index++) {
	    System.out.println("(" + index + ") " + conversionArray[index]);
	    }

        // Reconversion array
        System.out.println("\nRECONVERSION ARRAY\nindex = new attribute " +
		                      "number, value = old attribute number.");
	for(int index=1;index<reconversionArray.length;index++) {
	    System.out.println("(" + index + ") " + reconversionArray[index]);
	    }
	}

    /** Outputs conversion array (used to renumber columns for input data
    in terms of frequency of single attributes --- reordering will enhance
    performance for some ARM algorithms) (GUI Version.
    @param textArea the given instance of the <TT>JTextArea</TT> class.  */

    public void outputConversionArrays(JTextArea textArea) {
        if (conversionArray==null) {
            textArea.append("No conversion arrays found.\n");
            return;
            }

        // Conversion array
        textArea.append("CONVERSION ARRAY\nindex = old attribute number\n" +
	                          "<new attribute number,support count>.\n");
	for(int index=1;index<conversionArray.length;index++) {
	    textArea.append("(" + index + ") " + conversionArray[index] + "\n");
	    }

        // Reconversion array
        textArea.append("\nRECONVERSION ARRAY\nindex = new attribute " +
		                    "number, value = old attribute number.\n");
	for(int index=1;index<reconversionArray.length;index++) {
	    textArea.append("(" + index + ") " + reconversionArray[index] +
                                                                         "\n");
	    }
	}
    }
