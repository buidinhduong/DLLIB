/* -------------------------------------------------------------------------- */
/*                                                                            */
/*              FUZZY APRIORI T ARM ALGORITHM GUI CONTROL CLASS               */
/*                                                                            */
/*                                Frans Coenen                                */
/*                                                                            */
/*                           Monday 10 match 2008                             */
/*                              (Revised ....)                                */
/*                                                                            */
/*                       Department of Computer Science                       */
/*                         The University of Liverpool                        */
/*                                                                            */
/* -------------------------------------------------------------------------- */

/** Control class for Fuzzy Apriori-T, contains methods for manipulating the GUI
etc. */

//package lucsKDD_ARM;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class FuzzyAprioriTcontrol extends JFrame implements ActionListener {

    /* ------ FIELDS ------ */

    // CONSTANTS
    
    /** Minimum support and confidancs threshold value. */
    final static double MIN_THOLD = 0.0;
    /** Maximum support threshold value. */
    final static double SUPP_MAX_THOLD = 1.0;
    /** Maximum confidance threshold value. */
    final static double CONF_MAX_THOLD = 100.0;
    /** Maximum number of T-tree nodes that can be "drawn". */
    final static int MAX_NODES_IN_T_TREE_GRAPH = 1000;

    // OBJECTS
    /** Instance of Fuzzy Apriori T class. */
    private FuzzyAprioriT newAprioriT = null;
    /** Second Instance of Fuzzy Apriori T class. */
    private FuzzyAprioriT newFuzzyAprioriT = null;

    // GUI ITEMS
    // Menues
    /** File menu. */
    private JMenu fileMenu;
    /** Threshold input menu. */
    private JMenu tHoldInputMenu;
    /** Data pre-processing input menu. */
    private JMenu dataPreProcMenu;
    /** Apriori-T menu. */
    private JMenu aprioriTmenu;
    /** Generator menu. */
    private JMenu generatorMenu;
    /** Output menu. */
    private JMenu outputMenu;
    /** Data array output sub-menu. */
    private JMenu dataArrayOutputMenu;
    /** T-tree output sub-menu. */
    private JMenu tTreeOutputMenu;
    /** Frequent set output sub-menu. */
    private JMenu freqSetOutputMenu;
    /** Association rule output sub-menu. */
    private JMenu assocRuleOutputMenu;
    /** Diagnostic output sub-menu. */
    private JMenu diagnosticOutputSubMenu;

    // Menu items
    /** File menu items. */
    private JMenuItem[] fileMenuItems;
    /** Threshold input menu items. */
    private JMenuItem[] tholdInputMenuItems;
    /** Data preprocessing menu items. */
    private JMenuItem[] dataPreProcMenuItems;
    /** Apriori-T menu items. */
    private JMenuItem[] aprioriTmenuItems;
    /** Generator menu items. */
    private JMenuItem[] generatorMenuItems;
    /** Output menu items. */
    private JMenuItem[] outputMenuItems;
    /** Data array output sub-menu items. */
    private JMenuItem[] dataArrayOutputMenuItems;
    /** T-tree output sub-menu items. */
    private JMenuItem[] tTreeOutputMenuItems;
    /** Frequent set output sub-menu items. */
    private JMenuItem[] freqSetOutputMenuItems;
    /** Association rule output sub-menu items. */
    private JMenuItem[] assocRuleOutputMenuItems;
    /** Diagnostic output sub-menu items. */
    private JMenuItem[] diagnosticOutputMenuItems;

    // Menu labels
    /** File menu labels. */
    private String fileMenuLabels[] = {"About","Load Data","Load Schema","Exit"};
    /** Threshold parameter input menu labels. */
    private String tHoldInputMenuLabels[] = {"Support","Confidence"};
    /** Data pre-processing menu labels. */
    private String dataPreProcMenuLabels[] = {"Sort","Sort & Prune"};
    /** Apriori-T menu labels. */
    private String aprioriTmenuLabels[] = {"Fuzzy Apriori T (with X check)"};
    /** Generator menu labels. */
    private String generatorMenuLabels[] = {"Generate ARs (Min Conf)",
                                                       "Generate ARs (Lift)"};
    /** Ouput menu labels. */
    private String outputMenuLables[] = {"Output Schema"};
    /** Output data array sets sub-menu labels. */
    private String outputDataArrayMenuLabels[] = {"Data Att. Numbers",
                                                        "Data Output Schema"};
    /** Output T-tree sub-menu labels. */
    private String outputTtreeMenuLabels[] = {"T-tree Statistics",
                                     "T-Tree (Att. Numbers)","T-tree (Graph)"};
    /** Output frequent sets sub-menu labels. */
    private String outputFreqSetsMenuLabels[] = {"FSs Att. Numbers",
                                                          "FSs Output Schema"};
    /** Output ARs sub-menu labels. */
    private String outputAssocRulesMenuLabels[] = {"ARs Att. Numbers",
                                                          "ARs Output Schema"};
    /** Diagnostic output sub-menu labels. */
    private String outputDiagnosticMenuLabels[] = {"Conversion arrays"};

    // OTHER COMPONBENTS
    /** Credits panel */
    private JPanel creditsPanel;
    /** Text Area. */
    private JTextArea textArea;

    // FLAGS
    /** Has support threshold flag. */
    private boolean hasSupportThold = false;
    /** Has confidence threshold flag. */
    private boolean hasConfThold = false;
    /** Has data flag. */
    private boolean hasDataFlag = false;
    /** Is sorted flag. */
    private boolean isSortedFlag = false;
    /** Has output schema flag. */
    private boolean hasOutputSchema = false;
    /** Has frequent sets flag. */
    private boolean hasFrequentSetsFlag = false;
    /** Has ARs flag. */
    private boolean hasAssocRules = false;
    /** Using support-confidence frame work. */
    private boolean usingSCfWork = false;
    /** Using support-lift frame work. */
    private boolean usingSLfWork = false;

    // OTHER FIELDS
    /** Input data file name. */
    private File outputFileName;

    /* --------------------------------------------------- */
    /*                                                     */
    /*                    CONSTRUCTORS                     */
    /*                                                     */
    /* --------------------------------------------------- */

    /** One argument constructor to create the text mining GUI.
    @param newNewAprioriT new isntance of <TT>FuzzyAprioriT</TT> class.  */

    public FuzzyAprioriTcontrol(FuzzyAprioriT newNewAprioriT) {
        super("LUCS-KDD: Fuzzy Apriori-T ARM algorith demonstrator GUI");

        // Set fields
        newAprioriT = newNewAprioriT;

        // Content pane
        getContentPane().setBackground(Color.pink);
        getContentPane().setLayout(new BorderLayout(5,5)); // 5 pixel gaps

	// Create menus
	createFileMenu();
	createTholdInputMenu();
	createDataPreProcMenu();
	createAprioriTmenu();
	createGeneratorMenu();
	createOutputMenu();

	// Create menu bar
	JMenuBar bar = new JMenuBar();
	setJMenuBar(bar);
	bar.add(fileMenu);
	bar.add(tHoldInputMenu);
        bar.add(dataPreProcMenu);
        bar.add(aprioriTmenu);
        bar.add(generatorMenu);
        bar.add(outputMenu);

	// Add text area
	textArea = new JTextArea(40,40);
	textArea.setEditable(false);
        getContentPane().add(new JScrollPane(textArea),BorderLayout.CENTER);

	// Credits Panel
	createCreditsPanel();
	getContentPane().add(creditsPanel,BorderLayout.SOUTH);
	}

    /** Creates file menu */

    private void createFileMenu() {
        // Create file menu
	fileMenu = new JMenu("File");
	fileMenu.setMnemonic('F');

        // Create file menu items
        fileMenuItems = new JMenuItem[fileMenuLabels.length];
	for (int index=0;index<fileMenuLabels.length;index++) {
            fileMenuItems[index] = new JMenuItem(fileMenuLabels[index]);
            fileMenuItems[index].setEnabled(true);
	    fileMenuItems[index].addActionListener(this);
            if (index>0) fileMenu.addSeparator();
            fileMenu.add(fileMenuItems[index]);
            }
        }

    /** Creates parameter input menu */

    private void createTholdInputMenu() {
        // Create file menu
	tHoldInputMenu = new JMenu("Param. Input");
	tHoldInputMenu.setMnemonic('P');

        // Create threshold menu items
        tholdInputMenuItems = new JMenuItem[tHoldInputMenuLabels.length];
	for (int index=0;index<tHoldInputMenuLabels.length;index++) {
            tholdInputMenuItems[index] =
                                     new JMenuItem(tHoldInputMenuLabels[index]);
            tholdInputMenuItems[index].setEnabled(true);
	    tholdInputMenuItems[index].addActionListener(this);
            if (index>0) tHoldInputMenu.addSeparator();
            tHoldInputMenu.add(tholdInputMenuItems[index]);
            }
        }

    /** Creates data pre-processing menu. */

    private void createDataPreProcMenu() {
        // Create data pre-processing menu
	dataPreProcMenu = new JMenu("Data Pre-proc.");
	dataPreProcMenu.setMnemonic('D');

        // Create data pre-processing menu items
        dataPreProcMenuItems = new JMenuItem[dataPreProcMenuLabels.length];
	for (int index=0;index<dataPreProcMenuLabels.length;index++) {
            dataPreProcMenuItems[index] =
                                    new JMenuItem(dataPreProcMenuLabels[index]);
            dataPreProcMenuItems[index].setEnabled(true);
	    dataPreProcMenuItems[index].addActionListener(this);
            if (index>0) dataPreProcMenu.addSeparator();
            dataPreProcMenu.add(dataPreProcMenuItems[index]);
            }
        }

    /** Creates Aprior-T ARM menu. */

    private void createAprioriTmenu()  {
        // Create data pre-processing menu
	aprioriTmenu = new JMenu("Apriori-T");
	aprioriTmenu.setMnemonic('A');

        // Create Apriori-T menu items
        aprioriTmenuItems = new JMenuItem[aprioriTmenuLabels.length];
	for (int index=0;index<aprioriTmenuLabels.length;index++) {
            aprioriTmenuItems[index] = new JMenuItem(aprioriTmenuLabels[index]);
            aprioriTmenuItems[index].setEnabled(true);
	    aprioriTmenuItems[index].addActionListener(this);
            if (index>0) aprioriTmenu.addSeparator();
            aprioriTmenu.add(aprioriTmenuItems[index]);
            }
        }

    /** Creates generator menu */

    private void createGeneratorMenu() {
        // Create generator menu
	generatorMenu = new JMenu("Generator");
	generatorMenu.setMnemonic('G');

        // Create generator menu items
        generatorMenuItems = new JMenuItem[generatorMenuLabels.length];
	for (int index=0;index<generatorMenuLabels.length;index++) {
            generatorMenuItems[index] = new JMenuItem(generatorMenuLabels[index]);
            generatorMenuItems[index].setEnabled(true);
	    generatorMenuItems[index].addActionListener(this);
            if (index>0) generatorMenu.addSeparator();
            generatorMenu.add(generatorMenuItems[index]);
            }
        }

    /** Creates output menu */

    private void createOutputMenu() {
        // Create output menu
	outputMenu = new JMenu("Output");
	outputMenu.setMnemonic('O');

        // Define array of output menu items.
        outputMenuItems = new JMenuItem[outputMenuLables.length];

        // Create output menu items
        outputMenuItems = new JMenuItem[outputMenuLables.length];
	for (int index=0;index<outputMenuLables.length;index++) {
            outputMenuItems[index] = new JMenuItem(outputMenuLables[index]);
            outputMenuItems[index].setEnabled(true);
	    outputMenuItems[index].addActionListener(this);
            if (index>0) outputMenu.addSeparator();
            outputMenu.add(outputMenuItems[index]);
            }
        outputMenu.addSeparator();

        // Create output data array sub-menu items
        createDataArrayOutputMenu();
        dataArrayOutputMenu.setEnabled(true);
        outputMenu.add(dataArrayOutputMenu);
        outputMenu.addSeparator();

        // Create output T-tree sub-menu items
        createTtreeOutputMenu();
        tTreeOutputMenu.setEnabled(true);
        outputMenu.add(tTreeOutputMenu);
        outputMenu.addSeparator();

        // Create output frequent sets sub-menu items
        createFreqSetsOutputMenu();
        freqSetOutputMenu.setEnabled(true);
        outputMenu.add(freqSetOutputMenu);
        outputMenu.addSeparator();

        // Create output association rules sub-menu items
        createARsOutputMenu();
        assocRuleOutputMenu.setEnabled(true);
        outputMenu.add(assocRuleOutputMenu);
        outputMenu.addSeparator();

        // Create output diagnistics sub-menu items
        createdDagnosticOutputSubMenu();
        diagnosticOutputSubMenu.setEnabled(true);
        outputMenu.add(diagnosticOutputSubMenu);
        }

    /** Creates data array output sub-menu */

    private void createDataArrayOutputMenu() {
        // Create data array output menu
	dataArrayOutputMenu = new JMenu("Data Array");
	dataArrayOutputMenu.setMnemonic('D');

        // Create buttons
        dataArrayOutputMenuItems = new JMenuItem[outputDataArrayMenuLabels.length];
	for (int index=0;index<outputDataArrayMenuLabels.length;index++) {
            dataArrayOutputMenuItems[index] = new JMenuItem(outputDataArrayMenuLabels[index]);
            dataArrayOutputMenuItems[index].setEnabled(true);
	    dataArrayOutputMenuItems[index].addActionListener(this);
            if (index>0) dataArrayOutputMenu.addSeparator();
            dataArrayOutputMenu.add(dataArrayOutputMenuItems[index]);
            }
        }

    /** Creates T-tree output sub-menu */

    private void createTtreeOutputMenu() {
        // Create T-tree output menu
	tTreeOutputMenu = new JMenu("T-Tree");
	tTreeOutputMenu.setMnemonic('T');

        // Create buttons
        tTreeOutputMenuItems = new JMenuItem[outputTtreeMenuLabels.length];
	for (int index=0;index<outputTtreeMenuLabels.length;index++) {
            tTreeOutputMenuItems[index] = new JMenuItem(outputTtreeMenuLabels[index]);
            tTreeOutputMenuItems[index].setEnabled(true);
	    tTreeOutputMenuItems[index].addActionListener(this);
            if (index>0) tTreeOutputMenu.addSeparator();
            tTreeOutputMenu.add(tTreeOutputMenuItems[index]);
            }
        }

    /** Creates frequent sets output sub-menu */

    private void createFreqSetsOutputMenu() {
        // Create output frequent sets menu
	freqSetOutputMenu = new JMenu("Frequent Sets");
	freqSetOutputMenu.setMnemonic('S');

        // Create buttons
        freqSetOutputMenuItems = new JMenuItem[outputFreqSetsMenuLabels.length];
	for (int index=0;index<outputFreqSetsMenuLabels.length;index++) {
            freqSetOutputMenuItems[index] = new JMenuItem(outputFreqSetsMenuLabels[index]);
            freqSetOutputMenuItems[index].setEnabled(true);
	    freqSetOutputMenuItems[index].addActionListener(this);
            if (index>0) freqSetOutputMenu.addSeparator();
            freqSetOutputMenu.add(freqSetOutputMenuItems[index]);
            }
        }

    /** Creates association rules output sub-menu */

    private void createARsOutputMenu() {
        // Create output association rules menu
	assocRuleOutputMenu = new JMenu("Association Rules");
	assocRuleOutputMenu.setMnemonic('A');

        // Create buttons
        assocRuleOutputMenuItems = new JMenuItem[outputAssocRulesMenuLabels.length];
	for (int index=0;index<outputAssocRulesMenuLabels.length;index++) {
            assocRuleOutputMenuItems[index] = new JMenuItem(outputAssocRulesMenuLabels[index]);
            assocRuleOutputMenuItems[index].setEnabled(true);
	    assocRuleOutputMenuItems[index].addActionListener(this);
            if (index>0) assocRuleOutputMenu.addSeparator();
            assocRuleOutputMenu.add(assocRuleOutputMenuItems[index]);
            }
        }

    /** Creates association rules output sub-menu */

    private void createdDagnosticOutputSubMenu() {
        // Create diagnostic output menu
	diagnosticOutputSubMenu = new JMenu("Diagnostics");
	diagnosticOutputSubMenu.setMnemonic('D');

        // Create buttons
        diagnosticOutputMenuItems = new JMenuItem[outputDiagnosticMenuLabels.length];
	for (int index=0;index<outputDiagnosticMenuLabels.length;index++) {
            diagnosticOutputMenuItems[index] = new JMenuItem(outputDiagnosticMenuLabels[index]);
            diagnosticOutputMenuItems[index].setEnabled(true);
	    diagnosticOutputMenuItems[index].addActionListener(this);
            if (index>0) diagnosticOutputSubMenu.addSeparator();
            diagnosticOutputSubMenu.add(diagnosticOutputMenuItems[index]);
            }
        }
    /* --------------------------------- */
    /*                                   */
    /*       CREATE CREDITS PANELS       */
    /*                                   */
    /* --------------------------------- */

    /** Creates credits panel. */

    private void createCreditsPanel() {
	// Swet up panel
	creditsPanel = new JPanel();
	creditsPanel.setBackground(Color.pink);
	creditsPanel.setLayout(new GridLayout(5,1));

	// Create labels
	Label creditLabel1 = new Label("LUCS-KDD (Liverpool University " +
				"Computer Science - Knowledge Discovery");
	Label creditLabel2 = new Label("in Data) group Fuzzy Apriori-T ARM " +
                                                          "demonstrator.");
	Label creditLabel3 = new Label(" ");
	Label creditLabel4 = new Label("Version 1 Created by Frans Coenen " +
			"(10 March 2008)");    
	Label creditLabel5 = new Label("Version 2 Created by Frans Coenen " +
			"(30 August 2008)");
	// Add labels
	creditsPanel.add(creditLabel1);
	creditsPanel.add(creditLabel2);
	creditsPanel.add(creditLabel3);
	creditsPanel.add(creditLabel4);  
	creditsPanel.add(creditLabel5);
	}

    /* -------------------- */
    /*                      */
    /*       HANDLERS       */
    /*                      */
    /* -------------------- */

    /** Action performed handler.
    @param event the triggered event. */

    public void actionPerformed(ActionEvent event) {
        // File menu item About
	if (event.getActionCommand().equals("About")) {
	    textArea.append("ABOUT THE ARM DATA GENERATOR:\n");
	    aboutSoftware();
	    textArea.append("-------------------------------------\n");
	    }

        // File menu load data
        else if (event.getActionCommand().equals("Load Data")) {
	    textArea.append("LOAD DATA FILE:\n");
            loadData();
	    textArea.append("-------------------------------------\n");
            }

        // File menu load output schema
        else if (event.getActionCommand().equals("Load Schema")) {
	    textArea.append("READ OUTPUT SCHEMA FILE:\n");
            loadOutputSchema();
	    textArea.append("-------------------------------------\n");
            }

        // File menu exit
        else if (event.getActionCommand().equals("Exit")) {
	    System.exit(0);
            }

        // Threshold input menu item add support threshold
        else if (event.getActionCommand().equals("Support")) {
	    textArea.append("INPUT SUPPORT THRESHOLD:\n");
            inputSupportThold();
	    textArea.append("-------------------------------------\n");
            }

        // Threshold input menu item add confidence threshold
        else if (event.getActionCommand().equals("Confidence")) {
	    textArea.append("INPUT CONFIDENCE THRESHOLDS:\n");
            inputConfidenceThold();
	    textArea.append("-------------------------------------\n");
            }

        // Data pre-processing menu item sort input data. */
        else if (event.getActionCommand().equals("Sort")) {
            textArea.append("SORT INPUT DATA:\n");
            sortInputData();
	    textArea.append("-------------------------------------\n");
            }

        // Data pre-processing menu item sort and prune input data. */
        else if (event.getActionCommand().equals("Sort & Prune")) {
            textArea.append("SORT AND PRUNE INPUT DATA:\n");
            sortAndPruneInputData();
	    textArea.append("-------------------------------------\n");
            }

        // Apriori-T menu item apriori-T without X checking. */
        else if (event.getActionCommand().equals("Fuzzy Apriori T (with X check)")) {
            textArea.append("FUZZY APRIORI-T (WITH X CHECK):\n");
            aprioriTwithXcheck();
	    textArea.append("-------------------------------------\n");
            }

        // Generate menu item generate ARs with min. confidence framework.
        else if (event.getActionCommand().equals("Generate ARs (Min Conf)")) {
	    textArea.append("GENERATE ARs (SUPPORT AND CONFIDENCE FRAMEWORK):\n");
            generateARsMinConf();
	    textArea.append("-------------------------------------\n");
            }

        // Generate menu item generate ARs with lift framework.
        else if (event.getActionCommand().equals("Generate ARs (Lift)")) {
	    textArea.append("GENERATE ARs (SUPPORT AND LIFT FRAMEWORK):\n");
            generateARsLift();
	    textArea.append("-------------------------------------\n");
            }

        // Output schema output sub-memu item output using attribute numbers
        else if (event.getActionCommand().equals("Output Schema")) {
	    textArea.append("OUTPUT OUTPUT SCHEMA CONTENTS:\n");
            outputOutputSchema();
	    textArea.append("-------------------------------------\n");
            }

        // Data array output sub-memu item output using attribute numbers
        else if (event.getActionCommand().equals("Data Att. Numbers")) {
	    textArea.append("OUTPUT DATA ARRAY USING ATTRIBUTE NUMBERS:\n");
            outputDataArrayAtts();
	    textArea.append("-------------------------------------\n");
            }

        // Frequent sets output sub-memu item output using output schema
        else if (event.getActionCommand().equals("Data Output Schema")) {
	    textArea.append("OUTPUT DATA ARRAY USING OUTPUT SCHEMA:\n");
            outputDataArraySchema();
	    textArea.append("-------------------------------------\n");
            }

        // Frequent sets output sub-memu item output using output schema
        else if (event.getActionCommand().equals("T-tree Statistics")) {
	    textArea.append("OUTPUT T-TREE STATISTICS:\n");
            outputTtreeStats();
	    textArea.append("-------------------------------------\n");
            }

        // Frequent sets output sub-memu item output using output schema
        else if (event.getActionCommand().equals("T-Tree (Att. Numbers)")) {
	    textArea.append("OUTPUT T-TREE AS TEXTUAL lIST:\n");
            outputTtree();
	    textArea.append("-------------------------------------\n");
            }

        // T-tree output sub-memu item output using output schema
        else if (event.getActionCommand().equals("T-tree (Graph)")) {
	    textArea.append("OUTPUT T-TREE AS GRAPH IMAGE:\n");
            outputTtreeGraph();
	    textArea.append("-------------------------------------\n");
            }

        // Frequent sets output sub-memu item output using attribute numbers
        else if (event.getActionCommand().equals("FSs Att. Numbers")) {
	    textArea.append("OUTPUT FREQUENT SETS USING ATTRIBUTE NUMBERS:\n");
            outputFreqSetsAtts();
	    textArea.append("-------------------------------------\n");
            }

        // Frequent sets output sub-memu item output using output schema
        else if (event.getActionCommand().equals("FSs Output Schema")) {
	    textArea.append("OUTPUT FREQUENT SETS USING OUTPUT SCHEMA:\n");
            outputFreqSetsSchema();
	    textArea.append("-------------------------------------\n");
            }

        // Association rules output sub-memu item output using attribute numbers
        else if (event.getActionCommand().equals("ARs Att. Numbers")) {
	    textArea.append("OUTPUT ASSOCIATION RULES USING ATTRIBUTE NUMBERS:\n");
            outputARsAtts();
	    textArea.append("-------------------------------------\n");
            }

        // Association rules output sub-memu item output using output schema
        else if (event.getActionCommand().equals("ARs Output Schema")) {
	    textArea.append("OUTPUT ASSOCIATION RULES USING OUTPUT SCHEMA:\n");
            outputARsSchema();
	    textArea.append("-------------------------------------\n");
            }

        // Diagnostic output sub-memu item output concersion arrays
        else if (event.getActionCommand().equals("Conversion arrays")) {
	    textArea.append("OUTPUT CONVERSION AND RECONVERSION ARRAYS:\n");
            outputConversionArrays();
	    textArea.append("-------------------------------------\n");
            }

        // Defualt
        else {
            JOptionPane.showMessageDialog(null,"ERROR:\n Unrecognised " +
                "action event,  " + event.getActionCommand() + "\n");
            }
        }

    /* ------------------------- */
    /*                           */
    /*       ABOUT SOFTWARE      */
    /*                           */
    /* ------------------------- */

    /* ABOUT SOFTWARE */

    /** Outputs text describing software. */

    private void aboutSoftware() {
	textArea.append("To run the software:\n");
	textArea.append("\t1. Specify input data (and if desired output " +
                                                                  "schema).\n");
	textArea.append("\t2. Set support and (if appropriate) confidence " +
                                                               "thresholds.\n");
	textArea.append("\t3. Select algorithum to be run\n");
        textArea.append("\t4. When complete generate ARs\n");
	textArea.append("\t5. Output results\n");
        }

    /* -------------------------------------- */
    /*                                        */
    /*              LOAD DATA                 */
    /*                                        */
    /* -------------------------------------- */

    /** Reads input data from file specified in command line argument and
    places data in inputDataArray.*/

    private void loadData() {
        // Display file dialog so user can select file to open
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    int result = fileChooser.showOpenDialog(this);

	    // If cancel button selected return
	    if (result == JFileChooser.CANCEL_OPTION) {
	        textArea.append("Operation cancelled\n");
            return;
	        }

	    // Obtain selected file
	    File inputFileName = fileChooser.getSelectedFile();
        
        // Reset flafs. Set has data flag to false; set is sorted flag to false 
        // (ready for sorting if desired)
        hasDataFlag         = false;
        isSortedFlag        = false;
        hasFrequentSetsFlag = false;
        hasAssocRules       = false;
        usingSCfWork        = false;
        usingSLfWork        = false;
        newFuzzyAprioriT    = null;    

	    // Read file if readabale (i.e not a direcrory etc.).
	    if (checkFileName(inputFileName)) {
	        textArea.append("inputFileName = " +  inputFileName + "\n");
	        newAprioriT.inputDataSet(textArea,inputFileName);
	        // If data laoded successfully cjeck with schema (if it exists)
	        if (newAprioriT.getHaveDataFlag()) {
	            if (hasOutputSchema) newAprioriT.checkSchemaVdata();
                hasDataFlag = true;
                }
            }
        }

    /* CHECK FILE NAME */
    /** Checks nature of input file name selected by user.
    @param fileName name of file to be checked
    @return false if selected file is a directory, access is denied or is
    not a file name and true otherwise. */

    private boolean checkFileName(File fileName) {
        boolean fileOK = true;

        // Check if file name exist
	if (fileName.exists()) {
	    // Check if file name is readable
            if (fileName.canRead()) {
                // Check id file name is actually a file
		if (fileName.isFile()) return(fileOK);
		else JOptionPane.showMessageDialog(null,
				"FILE ERROR: File is a directory");
		}
	    else JOptionPane.showMessageDialog(null,
	    			"FILE ERROR: Access denied");
	    }
	else JOptionPane.showMessageDialog(null,
				"FILE ERROR: No such file!");
	// Return

	return(!fileOK);
	}

    /* ----------------------------------------------- */
    /*                                                 */
    /*              LOAD OUTPUT SCHEMA                 */
    /*                                                 */
    /* ----------------------------------------------- */

    /** Commences process of loading output schema to be used for the outpur of
    the generated frequent sets and ARs. */

    private void loadOutputSchema() {
        // Display file dialog so user can select file to open
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	int result = fileChooser.showOpenDialog(this);

	// If cancel button selected return
	// If cancel button selected return
	if (result == JFileChooser.CANCEL_OPTION) {
	    textArea.append("Operation cancelled\n");
            return;
	    }

	// Obtain selected file
	File inputFileName = fileChooser.getSelectedFile();

	// Read file if readabale (i.e not a direcrory etc.).
	if (checkFileName(inputFileName)) {
	    textArea.append("inputFileName = " +  inputFileName + "\n");
	    newAprioriT.inputOutputSchema(textArea,inputFileName);
	    if (hasDataFlag) newAprioriT.checkSchemaVdata();
            hasOutputSchema = true;
            // It may be that vwe have run Fuzzy Apriori-T in which case we 
            // should add the scheme to this instance.
            if (newFuzzyAprioriT!=null) 
                newFuzzyAprioriT.setOutputSchemaArray(newAprioriT.outputSchema);
            }
        else hasOutputSchema = false;
        }

    /* ---------------------------------------------------- */
    /*                                                      */
    /*              INPUT SUPPORT THRESHOLD                 */
    /*                                                      */
    /* ---------------------------------------------------- */

    /** Input the support threshold to be used in the identification of
    frequent sets.                 */

    private void inputSupportThold() {
        String stNum = null;

        // Try catch block
        try{
            while (true) {
                stNum = JOptionPane.showInputDialog("Input desired support " +
	       		        "threshold (real number between" + MIN_THOLD +
                                              " and " + SUPP_MAX_THOLD + ")\n\n" );
	        // Check if operation cancelled
                if (stNum == null) {
                    textArea.append("Operation cancelled!\n");
                    break;
                    }
                // Process input
                double newSupportThold = Double.parseDouble(stNum);
                // Check if within range
                if ((newSupportThold < MIN_THOLD) ||
                                               (newSupportThold > SUPP_MAX_THOLD)) {
		    JOptionPane.showMessageDialog(null,"INPUT ERROR:\n " +
			       "support threshold input,  " + newSupportThold +
                                  ",\nnot within range " + MIN_THOLD + " to " +
                                                            SUPP_MAX_THOLD + ".\n");
                    textArea.append("Try agin!\n");
                    }
	        else {
                    newAprioriT.setSupport(newSupportThold);
                    newAprioriT.setMinSupport();
	            outputSupportThold();
	            hasSupportThold=true;
	            break;
	            }
		}
	    }
        catch(NumberFormatException e) {
	    JOptionPane.showMessageDialog(null,"INPUT ERROR:\n " +
			"Number format exception, support\nthreshold (%) " +
                        "input =  \"" + stNum + "\".\n");
            textArea.append("Operation failed!\n");
	    }
        }

    /* ------------------------------------------------------- */
    /*                                                         */
    /*              INPUT CONFIDENCE THRESHOLD                 */
    /*                                                         */
    /* ------------------------------------------------------- */

    /** Input the confidence threshold to be used in the identification of ARs
    (if not using lift). */

    private void inputConfidenceThold() {
        String stNum = null;

        // Try catch block
        try{
            while (true) {
                stNum = JOptionPane.showInputDialog("Input desired " +
	       		               "confidence threshold (%)\n\n" );
	        // Check if operation cancelled
                if (stNum == null) {
                    textArea.append("Operation cancelled!\n");
                    break;
                    }
                // Process input
                double newConfidenceThold = Double.parseDouble(stNum);
                // Check if within range
                if ((newConfidenceThold < MIN_THOLD) ||
                                           (newConfidenceThold > CONF_MAX_THOLD)) {
		    JOptionPane.showMessageDialog(null,"INPUT ERROR:\n " +
			"confidence threshold input,  " + newConfidenceThold +
                                 ",\nnot within range " + MIN_THOLD + " to " +
                                                           CONF_MAX_THOLD + ".\n");
                    textArea.append("Try agin!\n");
                    }
	        else {
                    newAprioriT.setConfidence(newConfidenceThold);
                if (newFuzzyAprioriT!=null) newFuzzyAprioriT.setConfidence(newConfidenceThold);    
	            outputConfidenceThold();
	            hasConfThold=true;
	            break;
	            }
		}
	    }
        catch(NumberFormatException e) {
	    JOptionPane.showMessageDialog(null,"INPUT ERROR:\n " +
			"Number format exception, confidence\nthreshold (%) " +
                        "input =  \"" + stNum + "\".\n");
            textArea.append("Operation failed!\n");
	    }
        }

    /* ------------------------------ */
    /*                                */
    /*       SORT INPUT DATA          */
    /*                                */
    /* ------------------------------ */

    /** Commences process of sorting input data in order of support value,
    highest supported attributers listed first. */

    private void sortInputData() {
        boolean okToProceed = true;

        // Check if data already has been preprocessed
        if (isSortedFlag) {
	    JOptionPane.showMessageDialog(null,"PRE-PROCESSING ERROR:\n " +
			             "Data has already been pre-processed.\n");
            okToProceed = false;
            }
        // Check data
        else if (!hasDataFlag) {
	    JOptionPane.showMessageDialog(null,"PRE-PROCESSING ERROR:\n " +
			                                "No data supplied.\n");
            okToProceed = false;
            }

        // If OK to proceed reorder input data according to frequency of single
        // attributes
        if (okToProceed) {
            newAprioriT.idInputDataOrdering();
	    newAprioriT.recastInputData();
	    isSortedFlag = true;
            }
        else textArea.append("Operation failed!\n");
        }

    /* ---------------------------------------- */
    /*                                          */
    /*       SORT AND PRUNE INPUT DATA          */
    /*                                          */
    /* ---------------------------------------- */

    /** Commences process of sorting and pruning input data in order of support
    value, highest supported attributers listed first, attributes with support
    below support threshold deleted. */

    private void sortAndPruneInputData() {
        boolean okToProceed = true;

        if (isSortedFlag) {
	    JOptionPane.showMessageDialog(null,"PRE-PROCESSING ERROR:\n " +
			             "Data has already been pre-processed.\n");
            okToProceed = false;
            }
        else {
            // Check data
            if (!hasDataFlag) {
	        JOptionPane.showMessageDialog(null,"PRE-PROCESSING ERROR:\n " +
			                                "No data supplied.\n");
                okToProceed = false;
                }
            // Check support threshold
            if (!hasSupportThold) {
	        JOptionPane.showMessageDialog(null,"PRE-PROCESSING ERROR:\n " +
			                   "No support threshold supplied.\n");
                okToProceed = false;
                }
            }

        // If OK to proceed reorder and prune input data according to frequency
        // of single attributes
        if (okToProceed) {
            newAprioriT.idInputDataOrdering();
	    newAprioriT.recastInputDataAndPruneUnsupportedAtts();
	    newAprioriT.setNumOneItemSets();
	    isSortedFlag = true;
            }
        else textArea.append("Operation failed!\n");
        }

    /* ---------------------------------------- */
    /*                                          */
    /*       APRIORI-T WITH X CHECKING          */
    /*                                          */
    /* ---------------------------------------- */

    /** Commences application of Fuzzy Apriori-T algorithm (with X-checking). */

    private void aprioriTwithXcheck() {
        boolean okToProceed = true;

        // Check data
        if (!hasDataFlag) {
	    JOptionPane.showMessageDialog(null,"APRIORI-T ERROR:\n " +
			                                "No data supplied.\n");
            okToProceed = false;
            }

        // Check support threshold
        if (!hasSupportThold) {
	    JOptionPane.showMessageDialog(null,"APRIORI-T ERROR:\n " +
			                   "No support threshold supplied.\n");
            okToProceed = false;
            }

        // If OK to proceed apply Apriori-T (with X check)
        if (okToProceed) {
            // Create new  FuzzyAprioriT class
            newFuzzyAprioriT = new FuzzyAprioriT(newAprioriT);
            double time1 = (double) System.currentTimeMillis();
            newFuzzyAprioriT.createTotalSupportTree(textArea);
            newFuzzyAprioriT.outputDuration(textArea,time1,
                                                   System.currentTimeMillis());
            // Check if limit has been reached
            int numFsets = newFuzzyAprioriT.getNumFreqSets();
            int maxFsets = newFuzzyAprioriT.getMaxNumFrequentSets();
            if (numFsets>maxFsets) textArea.append("Number of frequent sets " +
                                              "generated so far, " + numFsets +
                                     "exceeds maximum of " + maxFsets + ".\n");
            else textArea.append("Number of frequent sets = " + numFsets +
                                                                        ".\n");
            // Set flag
            hasFrequentSetsFlag = true;
            }
        else {
            textArea.append("Operation failed!\n");
            hasFrequentSetsFlag = false;
            }
        }

    /* ---------------------------------------------------------------------- */
    /*                                                                        */
    /*   GENERATE ASSOCIATION RULES USING SUPPORT AND CONFIDENCE FRAMEWORK    */
    /*                                                                        */
    /* ---------------------------------------------------------------------- */

    /** Commences process of generating ARs using support and confidence
    framework by checking that system has frequent sets and confidence
    threshold. */

    private void generateARsMinConf() {
        boolean okToProceed = true;

        // Check frequent sets
        if (!hasFrequentSetsFlag) {
	    JOptionPane.showMessageDialog(null,"AR GENERATOR ERROR:\n " +
			"No frequent sets generated.\n");
            okToProceed = false;
            }

        // Check confidence threshold
        if (!hasConfThold) {
	    JOptionPane.showMessageDialog(null,"AR GENERATOR ERROR:\n " +
			"No confidence threshold supplied.\n");
            okToProceed = false;
            }

        // Check if OK to proceed
        if (okToProceed) {
            usingSCfWork = true;
            usingSLfWork = false;
            if (newFuzzyAprioriT!=null) {    
                textArea.append("Fuzzy Apriori-T\nConfidence Thold = " +
                		(newFuzzyAprioriT.getConfidence()) + "\n");
                newFuzzyAprioriT.generateARs(textArea);
                newFuzzyAprioriT.outputNumRules(textArea);
                hasAssocRules = true;
                }
            else  {
                JOptionPane.showMessageDialog(null,"AR GENERATION ERROR:\n " +
			"Unrecognised Fuzzy ARM algorirthm run!\n");
                textArea.append("Operation failed!\n");
                }
            }
        else {
            textArea.append("Operation failed!\n");
            hasAssocRules = false;
            }
        }

    /* ---------------------------------------------------------------------- */
    /*                                                                        */
    /*      GENERATE ASSOCIATION RULES USING SUPPORT AND LIFT FRAMEWORK       */
    /*                                                                        */
    /* ---------------------------------------------------------------------- */

    /** Commences ARM brute force algorithm using support and lift framework
    by checking that system has data and support threshold . */

    private void generateARsLift() {
        boolean okToProceed = true;

        // Check frequent sets
        if (!hasFrequentSetsFlag) {
	    JOptionPane.showMessageDialog(null,"AR GENERATOR ERROR:\n " +
			"No frequent sets generated.\n");
            okToProceed = false;
            }

        // Check if OK to proceed
        if (okToProceed) {
            usingSCfWork = false;
            usingSLfWork = true;
            if (newFuzzyAprioriT!=null) {   
                textArea.append("Fuzzy Apriori-T\n");
                newFuzzyAprioriT.generateARsLift(textArea);
                newFuzzyAprioriT.outputNumRules(textArea);
                hasAssocRules = true;
                }
            else  {
                JOptionPane.showMessageDialog(null,"AR GENERATION ERROR:\n " +
			"Unrecognised Fuzzy ARM algorirthm run!\n");
                textArea.append("Operation failed!\n");
                }
            }
        else {
            textArea.append("Operation failed!\n");
            hasAssocRules = false;
            }
        }

    /* -------------------------------- */
    /*                                  */
    /*       OUTPUT OUTPUT SCHEMA       */
    /*                                  */
    /* -------------------------------- */

    /** Outputs output schema. */

    private void outputOutputSchema() {
        if (hasOutputSchema) newAprioriT.outputOutputSchema(textArea);
        }

    /* -------------------------------------------------- */
    /*                                                    */
    /*       OUTPUT DATA ARRAY AS ATTRIBUTE NUMBERS       */
    /*                                                    */
    /* -------------------------------------------------- */

    /** Outputs data array using attribute number representation. */

    private void outputDataArrayAtts() {
	if (hasDataFlag) newAprioriT.outputDataArray(textArea);
        else {
            JOptionPane.showMessageDialog(null,"OUTPUT ERROR:\n " +
			"No input data loaded!\n");
            textArea.append("Operation failed!\n");
            }
        }

    /* ---------------------------------------------- */
    /*                                                */
    /*       OUTPUT DATA ARRAY AS SCHEMA LABELS       */
    /*                                                */
    /* ---------------------------------------------- */

    /** Outputs data array using schema label representation. */

    private void outputDataArraySchema() {
	if (hasDataFlag) {
	    if (hasOutputSchema) newAprioriT.outputDataArraySchema(textArea);
            else {
                JOptionPane.showMessageDialog(null,"OUTPUT ERROR:\n " +
			"No output schema loaded!\n");
                textArea.append("Operation failed!\n");
                }
            }
        else {
            JOptionPane.showMessageDialog(null,"OUTPUT ERROR:\n " +
			"No input data loaded!\n");
            textArea.append("Operation failed!\n");
            }
        }

    /* ------------------------------------ */
    /*                                      */
    /*       OUTPUT T-TREE STATISTICS       */
    /*                                      */
    /* ------------------------------------ */

    /** Outputs conversion (and reconversion) arrays. */

    private void outputTtreeStats() {
        // Check frequent sets
        if (newFuzzyAprioriT!=null && hasFrequentSetsFlag)
                                   newFuzzyAprioriT.outputTtreeStats(textArea);
	else {
	    JOptionPane.showMessageDialog(null,"T-TREE OUTPUT ERROR:\n " +
			                        "No T-tree generated.\n");
            textArea.append("Operation failed!\n");
            }
        }

    /* -------------------------- */
    /*                            */
    /*       OUTPUT T-TREE        */
    /*                            */
    /* -------------------------- */

    /** Outputs T tree as a textual list. */

    private void outputTtree() {
        // Check frequent sets
        if (newFuzzyAprioriT!=null && hasFrequentSetsFlag)
                                        newFuzzyAprioriT.outputTtree(textArea);
	else {
	    JOptionPane.showMessageDialog(null,"T-TREE OUTPUT ERROR:\n " +
			                        "No T-tree generated.\n");
            textArea.append("Operation failed!\n");
            }
        }

    /* ------------------------------- */
    /*                                 */
    /*       OUTPUT T-TREE GRAPH       */
    /*                                 */
    /* ------------------------------- */

    /** Outputs T-tree graph. */

    private void outputTtreeGraph() {
	// Check if T-tree generated
        if (newFuzzyAprioriT==null || !hasFrequentSetsFlag) {
	    JOptionPane.showMessageDialog(null,"T-TREE GRAPH OUTPUT ERROR:\n " +
			"No T-tree generated.\n");
            textArea.append("Operation failed!\n");
	    return;
            }

        // If more than 1000 frequent sets return
        if (newFuzzyAprioriT.getNumFreqSets()>MAX_NODES_IN_T_TREE_GRAPH) {
	    JOptionPane.showMessageDialog(null,"TOO MANY FREQUENT SETS:\n" +
	       		"Only T-trees with less than " + MAX_NODES_IN_T_TREE_GRAPH +
			"\nlarge sets can be graphically presented.\n");
            textArea.append("Operation failed!\n");
	    return;
	    }

	// Otherwise process
	TtreeWindow tTreeWinApp = new
	    		    TtreeWindow(newFuzzyAprioriT.getNnumFreqOneItemSets(),
					      newFuzzyAprioriT.getStartOfTtree(),
					      newFuzzyAprioriT.getMinSupport());
        tTreeWinApp.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }

    /* ----------------------------------------------------- */
    /*                                                       */
    /*       OUTPUT FREQUENT SETS AS ATTRIBUTE NUMBERS       */
    /*                                                       */
    /* ----------------------------------------------------- */

    /** Outputs frequent sets using attribute number representation. */

    private void outputFreqSetsAtts() {
        // Check if ARM algorithm has been run.
        if (hasFrequentSetsFlag) {
            if (newFuzzyAprioriT!=null) {
                textArea.append("Fuzzy Apriori-T\n");
                newFuzzyAprioriT.outputFrequentSets(textArea);
                }
            else  {
                JOptionPane.showMessageDialog(null,"OUTPUT ERROR:\n " +
			"Unrecognised Fuzzy ARM algorirthm run!\n");
                textArea.append("Operation failed!\n");
                }
            }
        else {
            JOptionPane.showMessageDialog(null,"OUTPUT ERROR:\n " +
			"Fuzzy ARM algorirthm not run, therefore\n" +
                        "no frequent sets generated.\n");
            textArea.append("Operation failed!\n");
            }
        }

    /* ------------------------------------------------- */
    /*                                                   */
    /*       OUTPUT FREQUENT SETS AS SCHEMA LABELS       */
    /*                                                   */
    /* ------------------------------------------------- */

    /** Commences output of frequent sets using lables contained in output schema
    by checking that system has data and support threshold . */

    private void outputFreqSetsSchema() {
        boolean okToProceed = true;

        // Check if output schema exists
        if (!hasOutputSchema) {
            JOptionPane.showMessageDialog(null,"OUTPUT ERROR:\n " +
			"No output schema provided.\n");
            okToProceed = false;
            }

        // Check if ARM algorithm has been run.
        if (!hasFrequentSetsFlag) {
            JOptionPane.showMessageDialog(null,"OUTPUT ERROR:\n " +
			"ARM algorirthm not run, therefore\n" +
                        "no frequent sets generated.\n");
            okToProceed = false;
            }

        // If OK to proceed
        if (okToProceed) {
            if (newFuzzyAprioriT!=null) {
                textArea.append("Fuzzy Apriori-T\n");
                newFuzzyAprioriT.outputFrequentSetsSchema(textArea);
                }
            else  {
                JOptionPane.showMessageDialog(null,"OUTPUT ERROR:\n " +
			               "Unrecognised Fuzzy ARM algorirthm run!\n");
                textArea.append("Operation failed!\n");
                }
            }
        else textArea.append("Operation failed!\n");
        }

    /* -------------------------------------------------------- */
    /*                                                          */
    /*       OUTPUT ASSOCIAION RULES AS ATTRIBUTE NUMBERS       */
    /*                                                          */
    /* -------------------------------------------------------- */

    /** Outputs Association Rules (ARs) using attribute number representation. */

    private void outputARsAtts() {
        // Check if ARM algorithm has been run.
        if (hasAssocRules) {
            textArea.append("(N) ANTECEDENT -> CONSEQUENT ");
            if (usingSCfWork) textArea.append("CONFIDENCE (%)\n");
            else if (usingSLfWork) textArea.append("LIFT\n");
            // Output rules
//if (newDIC_Ttree!=null) newDIC_Ttree.outputRules(textArea);
//else
            newFuzzyAprioriT.outputRules(textArea);
            }
        else {
            JOptionPane.showMessageDialog(null,"OUTPUT ERROR:\n " +
			"ARM algorirthm not run, therefore\n" +
                        "no association rules generated.\n");
            textArea.append("Operation failed!\n");
            }
        }

    /* ---------------------------------------------------- */
    /*                                                      */
    /*       OUTPUT ASSOCIAION RULES AS SCHEMA LABELS       */
    /*                                                      */
    /* ---------------------------------------------------- */

    /** Commences output Association Rules (ARs) using lables contained in output schema
    by checking that system has data and support threshold . */

    private void outputARsSchema() {
        boolean okToProceed = true;

        // Check output schema exists
        if (!hasOutputSchema) {
            JOptionPane.showMessageDialog(null,"OUTPUT ERROR:\n " +
			"No output schema provided.\n");
            okToProceed = false;
            }

        // Check if ARM algorithm has been run.
        if (!hasAssocRules) {
            JOptionPane.showMessageDialog(null,"OUTPUT ERROR:\n " +
			"ARM algorirthm not run, therefore\n" +
                        "no association rules generated.\n");
            okToProceed = false;
            }

        // Check if OK to proceed
        if (okToProceed) {
            textArea.append("(N) ANTECEDENT -> CONSEQUENT ");
            if (usingSCfWork) textArea.append("CONFIDENCE (%)\n");
            else if (usingSLfWork) textArea.append("LIFT\n");
            // Output rules
//if (newDIC_Ttree!=null) newDIC_Ttree.outputRulesSchema(textArea);
//else
            newFuzzyAprioriT.outputRulesSchema(textArea);
            }
        else textArea.append("Operation failed!\n");
        }

    /* ------------------------------------ */
    /*                                      */
    /*       OUTPUT CONVERSION ARRAYS       */
    /*                                      */
    /* ------------------------------------ */

    /** Outputs conversion (and reconversion) arrays. */

    private void outputConversionArrays() {
        if (isSortedFlag) newAprioriT.outputConversionArrays(textArea);

        // Error output of no data
        else {
            if (!hasDataFlag) {
                JOptionPane.showMessageDialog(null,"OUTPUT ERROR:\n " +
			                    "No input data loaded!\n");
                textArea.append("Operation failed!\n");
                }
            else {
                JOptionPane.showMessageDialog(null,"OUTPUT ERROR:\n " +
			      "Data has not been sorted, hence there is no\n" +
			 "reconversion array generated that can be output!\n");
                textArea.append("Operation failed!\n");
                }
            }
        }

    /* ----------------------------------- */
    /*                                     */
    /*              OUTPUT                 */
    /*                                     */
    /* ----------------------------------- */

    /** Outputs the support tyhreshold to the text area. */

    private void outputSupportThold() {
        textArea.append("Support threshold (%) = " +
                                 newAprioriT.getSupport() + "\n");
        }

    /** Outputs the confidence threshold to the text area. */

    private void outputConfidenceThold() {
	textArea.append("Confidence threshold (%) = " +
                                newAprioriT.getConfidence() + "\n");
        }

    /** Outputs the minimum support value to the text area. */

    private void outputMinSupport() {
        textArea.append("Minimum support = " +
                                newAprioriT.getMinSupport() + "\n");
        }

    /* -------------------------------- */
    /*                                  */
    /*        OUTPUT UTILITIES          */
    /*                                  */
    /* -------------------------------- */

    /* TWO DECIMAL PLACES */

    /** Converts given real number to real number rounded up to two decimal
    places.
    @param number the given number.
    @return the number to two decimal places. */

    protected double twoDecPlaces(double number) {
    	int numInt = (int) ((number+0.005)*100.0);
	number = ((double) numInt)/100.0;
	return(number);
	}
    }

