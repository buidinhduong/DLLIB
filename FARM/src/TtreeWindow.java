/* -------------------------------------------------------------------------- */
/*                                                                            */
/*                             T-TREE WINDOW                                  */
/*                                                                            */
/*                              Frans Coenen                                  */
/*                                                                            */
/*                              30 June 2003                                  */
/*                                                                            */
/*                      Department of Computer Science                        */
/*                       The University of Liverpool                          */
/*                                                                            */ 
/* -------------------------------------------------------------------------- */

import java.awt.*;
import java.awt.geom.*;

/** Class to create a window in which the T-tree canvas can be displayed. */

// Java extension packages
import javax.swing.*;

public class TtreeWindow extends JFrame {

    // ------------------- FIELDS ------------------------

    /* CONSTANTS */

    static final int NODE_WIDTH  = 25;
    static final int NODE_HEIGHT = 13;
    static final int ARC_WIDTH   = 10;
    static final int ARC_HEIGHT  = 10;
    static final int GAP_WIDTH   = 28;
    static final int GAP_HEIGHT  = 22;
    static final int BORDER      = 20;
    static int maxXindex = 0;
    static int maxYindex = 0;
    static int numSupOneItemSets = 0;
    static double minSupport = 0.0;

    // ------------------ CONSTRUCTORS -------------------

    public TtreeWindow(int num, FuzzyTtreeNode[] startTtreeRef, double supp) {
        super("T-tree Structure");
	numSupOneItemSets=num;
	minSupport=supp;

	// Determine size of drawing area (add 20 to width to be in safe side)
	int numNodesWidth  = 2*areasize(startTtreeRef);
	int numNodesHeight = 0;
	if (numSupOneItemSets>7) numNodesHeight = (int) Math.pow(2.0,8);
	else numNodesHeight = (int) Math.pow(2.0,(numSupOneItemSets-1));

System.out.println("TtreeWondow: numNodesWidth = " + numNodesWidth + ", numNodesHeight = " +
numNodesHeight);
	// Create drawing area
	TtreeCanvas canvas = new TtreeCanvas(numNodesWidth,numNodesHeight,
				startTtreeRef,minSupport);
	canvas.setBackground(Color.white);
	int width  = BORDER+(numNodesWidth*NODE_WIDTH)+((numNodesWidth-1)*
			GAP_WIDTH)+BORDER;
	int height = 40 + (numNodesHeight*NODE_HEIGHT)+((numNodesHeight-1)*
			GAP_HEIGHT)+BORDER;
	canvas.setPreferredSize(new Dimension(width,height));

	// Create scroll pane
        JScrollPane sp1 = new JScrollPane(canvas);

	// Get container and add scroll pane
        Container container = getContentPane();
        container.setBackground(Color.pink);

	//container.setLayout(new BorderLayout(5,5)); // 5 pixel gaps
	container.add(sp1);
        setSize(400,300);
        setVisible(true);
        }

    // ------------------ METHODS ------------------------

    /* --------------------------------------------------------- */
    /*                                                           */
    /*                    DIMENSION CANVAS                       */
    /*                                                           */
    /* --------------------------------------------------------- */

    private int areasize(TtreeNode[] treeRef) {
	// Empty tree branch
        if (treeRef==null) return(0);

	// Count number of supported nodes in top row (i.e. size of top row)
	int numSupportedNodes=0;
	for(int index=1;index<treeRef.length;index++) {
	    if (treeRef[index]!=null) numSupportedNodes++;
	    }

	// Initialise number supported one item sets field (for later use).
	numSupOneItemSets=numSupportedNodes;

	// Process last branch descending from top row of tree
	for(int index=treeRef.length-1;index>0;index--) {
	    if (treeRef[index]!=null) {
	        return(areaSize(numSupportedNodes,treeRef[index].childRef));
	        }
	    }

	// Default return size of top level (no branches)
	return(numSupportedNodes);
	}

    private int areaSize(int size, TtreeNode[] treeRef) {

        // Empty tree branch
        if (treeRef==null) return(size);

	// Count number of supported nodes
	int numSupportedNodes=0;
	for(int index=1;index<treeRef.length;index++) {
	    if (treeRef[index]!=null) numSupportedNodes++;
	    }

	// If no more nodes return size, otherwise update size subtracting 1
	if (numSupportedNodes==0) return(size);
	size = size+numSupportedNodes-1;

	// Process last branch
	for(int index=treeRef.length-1;index>0;index--) {
	    if (treeRef[index]!=null) {
	        return(areaSize(size,treeRef[index].childRef));
	        }
	    }

	// Default return
	return(size);
	}
    }
