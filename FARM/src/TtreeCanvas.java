/* -------------------------------------------------------------------------- */
/*                                                                            */
/*                             T-TREE CANVAS                                  */
/*                                                                            */
/*                              Frans Coenen                                  */
/*                                                                            */
/*                              27 June 2003                                  */
/*                                                                            */
/*                      Department of Computer Science                        */
/*                       The University of Liverpool                          */
/*                                                                            */
/* -------------------------------------------------------------------------- */

/** Modules to paint a representation of the T-tree so that all T-tree nodes 
are placed in an X-Y plane which is then processed and output.  */

import java.awt.*;

// Java extension packages
import javax.swing.*;

public class TtreeCanvas extends JPanel {

    /** 3-D array, indexes: (1) the index for the node, (2) is the Y node index,
    (3) is an index into the data --- label (item set number), support, node
    type). <P>Five types of node: (a) no branches, (b) siblimng branch only,
    (c) child branch only, (d) sibling and child branches, and (e) vertical 
    arc.   */
    int[][][] nodeArray;

    /* FIELDS */

    /** The width of a single T-tree node. */
    static final int NODE_WIDTH  = 25;
    /** The height of a single T-tree node. */
    static final int NODE_HEIGHT = 13;
    static final int ARC_WIDTH   = 10;
    static final int ARC_HEIGHT  = 10;
    static final int GAP_WIDTH   = 28;
    static final int GAP_HEIGHT  = 20;
    static final int SPACING_W   = NODE_WIDTH+GAP_WIDTH;
    static final int SPACING_H   = NODE_HEIGHT+GAP_HEIGHT;
    static final int BORDER      = 20;

    /** Referece to start of T-tree data structure. */
    private FuzzyTtreeNode[] startTtreeRef = null;
    /** Thr ninmum support threshold value in terms of a number of records. */
    private double minSupport;

    /* CONSTRUCTOR */

    public TtreeCanvas(int numXnodes, int numYnodes, FuzzyTtreeNode[] tTreeRef,
    			double supp) {
        super();
	startTtreeRef=tTreeRef;
	minSupport = supp;
System.out.println("TtreeCanvas: numXnodes = " + numXnodes + ", numYnodes = " +
numYnodes);
	// Initialise node array
	nodeArray=new int[numXnodes][numYnodes][3];
	for (int colIndex=0;colIndex<nodeArray.length;colIndex++) {
	    for (int rowIndex=0;rowIndex<nodeArray[colIndex].length;rowIndex++) {
	        for (int index3=0;index3<3;index3++) {
	    	    nodeArray[colIndex][rowIndex][index3]=0;
	            }
	        }
	    }

	// Populate node array
	populateArray(0,0,startTtreeRef);
//outputNodeArray();
	}

    /* METHODS */

    public void paintComponent(Graphics g) {

        // Make the panel opaque
        super.paintComponent(g);

        // Loop
        for(int colIndex=0;colIndex<nodeArray.length;colIndex++) {
            for(int rowIndex=0;rowIndex<nodeArray[colIndex].length;rowIndex++) {
	        if (nodeArray[colIndex][rowIndex][0]!=0) {
		    drawNode(g,colIndex,rowIndex,
	    			Integer.toString(nodeArray[colIndex][rowIndex][0]),
				nodeArray[colIndex][rowIndex][1],
				nodeArray[colIndex][rowIndex][2]);
	            }
		}
	    }
        }

    /* --------------------------------------------------------- */
    /*                                                           */
    /*                       NODE DRAWING                        */
    /*                                                           */
    /* --------------------------------------------------------- */

    /** Starts process of drawing a T-tree node in the drawing area.
    @param g  the graphics context
    @param i1 the "x" index
    @param i2 the "y" index
    @param label the node label
    @param support the support for the itemset represented by the node
    @param type the arac indicator: 1) no arcs, 2) horizontal arc, 3) vertical
           arc, 4) horizontal and vertical arcs. */

    public void drawNode(Graphics g, int i1, int i2, String label,
    				int support, int type) {
	int x = BORDER+(i1*SPACING_W);
	int y = BORDER+(i2*SPACING_H);

	// Arc
	drawArc(g,x,y,i1,i2,type);

	// Node
        if (support>=minSupport) g.setColor(new Color(255,255,204));
	else g.setColor(Color.pink);
	g.fillRoundRect(x,y,NODE_WIDTH,NODE_HEIGHT,ARC_WIDTH,ARC_HEIGHT);
	g.setColor(Color.red);
	g.drawRoundRect(x,y,NODE_WIDTH,NODE_HEIGHT,ARC_WIDTH,ARC_HEIGHT);

	// Label
        g.setColor(Color.black);
        Font mono = new Font("Monospaced",Font.BOLD,10);
        g.setFont(mono);
        FontMetrics newFM = g.getFontMetrics(mono);
        int xLength = newFM.stringWidth(label);
        g.drawString(label,(x+(NODE_WIDTH/2)-(xLength/2)),y+11);

	// Support
	double supp=support/100.0;
	if (supp>=minSupport) g.setColor(Color.blue);
	else g.setColor(Color.orange);
        g.drawString(Double.toString(supp),x+NODE_WIDTH,y+NODE_HEIGHT+11);
	}

    /* DRAW ARC */

    /** Draws arcs between two Ttree nodes according to "type" setting:
    0) no arcs, 1) horizontal arc, 2) vertical arc, 3) horizontal and vertical
    arcs.
    @param g  the graphics context
    @param x the x coordinate
    @param y the y coordinate
    @param i1 the "x" index
    @param i2 the "y" index
    @param type the nature of the arc: horizontal, vertical or both. */

    private void drawArc(Graphics g, int x, int y, int i1, int i2, int type) {
        g.setColor(Color.black);
	// Horizontal arc only
        if (type==1) g.drawLine(x+NODE_WIDTH/2,y+NODE_HEIGHT/2,
			x+NODE_WIDTH/2+SPACING_W,y+NODE_HEIGHT/2);

	// Vertical Arc only
	if (type==2) {
	    int length=arcVerticalLength(i1,i2);
	    g.drawLine(x+NODE_WIDTH/2,y+NODE_HEIGHT/2,x+NODE_WIDTH/2,
			y+(length*SPACING_H)+NODE_HEIGHT/2);
	    }

	// Horizontal and vertical arcs
	if (type==3) {
	    g.drawLine(x+NODE_WIDTH/2,y+NODE_HEIGHT/2,
				x+NODE_WIDTH/2+SPACING_W,y+NODE_HEIGHT/2);
	    int length=arcVerticalLength(i1,i2);
	    g.drawLine(x+NODE_WIDTH/2,y+NODE_HEIGHT/2,x+NODE_WIDTH/2,
			y+(length*SPACING_H)+NODE_HEIGHT/2);
	    }
	}

    /* ARC VERTICAL LENGTH */

    /** Determines vertical distance of arc in T-tree.
    @param i1 the "x" index
    @param i2 the "y" index
    @retun the length of the vertical arc. */

    private int arcVerticalLength(int i1, int i2) {
        int length=1;

	// Icrement "Y" index and loop
	i2++;
	while(true) {
	    if (nodeArray[i1][i2][0]!=0) break;
	    else {
	      	length++;
		i2++;
		}
	    }

	// Return
	return(length);
	}

    /* --------------------------------------------------------- */
    /*                                                           */
    /*                   POPULATE NODE ARRAY                     */
    /*                                                           */
    /* --------------------------------------------------------- */

    /**
    @param startColIndex the X index into the node array for the start of the
    T-tree array (0 at start).
    @rowIndex rowIndex the Y index unto the node array (current level) for the
    T-tree array (0 at start).
    @param treeRef the reference into the current level in the T-tree (top at
    start).   */

    private void populateArray(int startColIndex, int rowIndex, FuzzyTtreeNode[] treeRef) {

	// Count number of supported nodes at current level (we do not want
	// to output unsupported nodes)
	int numSupportedNodes=0;
	for(int index=1;index<treeRef.length;index++) {
	    if (treeRef[index]!=null) numSupportedNodes++;
	    }

	// Check For room, nay have to increment row index
	rowIndex = checkForSpace(startColIndex,rowIndex,numSupportedNodes);

	// Add current level in T-tree working along T-tree level but placing
	// details in node array space in reverse.
	int colIndex=startColIndex+numSupportedNodes-1;
	boolean start=true;
	for(int index=treeRef.length-1;index>0;index--) {
	    // Check if supported
            if (treeRef[index]!=null) {
	        // Lable
                nodeArray[colIndex][rowIndex][0] = index;
	        // support
                nodeArray[colIndex][rowIndex][1] = (int)
                                                 ((treeRef[index].support+0.005)*100);
		// If strat and supported child nodes then type 2 else type 0
                if (start) {
		    if (supportedChildNodes(treeRef[index].childRef)) {
		        nodeArray[colIndex][rowIndex][2]=2;
			populateArray(colIndex,rowIndex+1,treeRef[index].childRef);
			}
		    else nodeArray[colIndex][rowIndex][2]=0;
		    }
		// If not start and supported child nodes then type 3, otherwise
		// type 1.
		else {
		    if (supportedChildNodes(treeRef[index].childRef)) {
		    	nodeArray[colIndex][rowIndex][2]=3;
			populateArray(colIndex,rowIndex+1,treeRef[index].childRef);
			}
		    else nodeArray[colIndex][rowIndex][2]=1;
		    }
		// Decrement volumn index
	        colIndex--;
	        // Set start flag to false
		start=false;
		}
	    }
	}

    /* CHECK FOR SUPPORT CHILD NODES */

    private boolean supportedChildNodes(FuzzyTtreeNode[] treeRef) {
        if (treeRef==null) return(false);

	// Loop
	for (int index=1;index<treeRef.length;index++) {
	    if (treeRef[index]!=null) return(true);
	    }

	// Default return
	return(false);
	}

    /* CHECK FOR SPACE */

    /** Checks if there is sufficient space in the node array, if so returns
    current row index, otherwise increments row index and trys again.
    @param startColIndex the curent x coordinate.
    @param rowIndex the current Y coordinate
    @param numSupportedNodes the number ofnodes to be fitted into the array.
    @return the revise Y coordinate. */

    private int checkForSpace(int startColIndex, int rowIndex,
    					int numSupportedNodes) {
    	// Step along level
//System.out.println("checkForSpace: startColIndex = " + startColIndex +
//", rowIndex = " + rowIndex);
        for (int index=startColIndex;index<startColIndex+numSupportedNodes;
								index++) {
            // Slot occupied by node, or slot not occupied but used by vertical
	    // arc from some other node
//System.out.println("index = " + index + ", rowIndex = " + rowIndex);
//System.out.println("nodeArray.length = " + nodeArray.length +
//", nodeArray[index].length = " + nodeArray[index].length);
	    if ((nodeArray[index][rowIndex][0]!=0) ||
	    				(nodeArray[index][rowIndex][2]==4)) {
	        // Set current slot to "occupied by vertical arc"
	        nodeArray[startColIndex][rowIndex][2]=4;
	        // Increment Y (row index) and repeat
	        return(checkForSpace(startColIndex,rowIndex+1,numSupportedNodes));
	        }
	    }

        // Return
        return(rowIndex);
        }

    /* ----------------------------------------------------- */
    /*                                                       */
    /*             OUTPUT (DIAGNOSTIC PUPOSES ONLY)          */
    /*                                                       */
    /* ----------------------------------------------------- */

    /* OUTPUT NODE ARRAY */

    /** Outputs node array. */

    private void outputNodeArray() {
        for (int rowIndex=0;rowIndex<nodeArray.length;rowIndex++) {
            for (int colIndex=0;colIndex<nodeArray[rowIndex].length;colIndex++) {
                System.out.print("[" + rowIndex + "," + colIndex + "] =");
         	for(int index=0;index<nodeArray[rowIndex][colIndex].length;
					index++) {
                    System.out.print(" " +
				nodeArray[rowIndex][colIndex][index]);
		    }
                System.out.print(" || ");
		}
            System.out.println();
            }
        }
    }
