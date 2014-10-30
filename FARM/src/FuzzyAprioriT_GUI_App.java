/* -------------------------------------------------------------------------- */
/*                                                                            */
/*                FUZZY APRIORI T ARM ALGORITHM DEMONSRATOR GUI               */
/*                                Frans Coenen                                */
/*                            Tuesday 4 March 2008                            */
/*                                                                            */
/* -------------------------------------------------------------------------- */

/** Fuzzy AprioriT GUI application class. */

import javax.swing.JFrame;

public class FuzzyAprioriT_GUI_App {

    /* ------ FIELDS ------ */

    // No fields

    /* ------ METHODS ------ */

    /** Main method (to start the "ball rolling"). */

    public static void main(String Args[]) {
        // Create Fuzzy Apriori T object
        FuzzyAprioriT newFuzzyAprioriT = new FuzzyAprioriT();

	// Create and display Apriori T control GUI object
        FuzzyAprioriTcontrol newFuzzyAprioriTcontrol = 
                                      new FuzzyAprioriTcontrol(newFuzzyAprioriT);
        newFuzzyAprioriTcontrol.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFuzzyAprioriTcontrol.setSize(700,700);
        newFuzzyAprioriTcontrol.setVisible(true);
	}
    }

