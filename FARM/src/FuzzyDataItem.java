/* -------------------------------------------------------------------------- */
/*                                                                            */
/*                             FUZZY DATA ITEM                                */
/*                               Frans Coenen                                 */
/*                           Tuesday 4 March 2008                             */
/*                                                                            */
/* -------------------------------------------------------------------------- */

/** Class (record) describing a "fuzzy" data item is per input data. */

public class FuzzyDataItem {
    /** Item (attribute) number. */
    private short itemNumber = 0;
    /** Item (attribute) fuzzy membership value. */
    private double fuzzyValue = 0.0;

/* ------ CONSTRUCTORS ------ */

    /** Two argument constructor.
    @param num the item number.
    @param value the item membership value.     */

    public FuzzyDataItem(short num, double value) {
        itemNumber = num;
        fuzzyValue = value;
        }

    /** Copy constructor.
    @param item itemset to be copied. */

    public FuzzyDataItem(FuzzyDataItem item) {
        this(item.getItemNumber(),item.getFuzzyValue());
        }
    
    /** Increments fuzzy value wuith the given amount.
    @param increment the additional incremental value.     */

    public void incValue(double increment) {
        fuzzyValue = fuzzyValue+increment;
        }

    /** Divides the fuzzy value bu the given value (typically this is the 
    number of records in the input data).
    @param divisor yje given value.          */

    public void divideValue(double divisor) {
        fuzzyValue = fuzzyValue/divisor;
        }

    /** Get item number.
    @return the item/attribute number (often treated as the label). */

    public short getItemNumber() {
        return(itemNumber);
        }

    /** Get item fuzzy membership value..
    @return the item value. */

    public double getFuzzyValue() {
        return(fuzzyValue);
        }

    /** Sets item number fiejd to given value.
    @param newItemNum the given value. */

    public void setItemNumber(short newItemNum) {
        itemNumber = newItemNum;
        }

    /** Sets fuzzy value field to given value.
    @param newFuzzyValue the given value. */

    public void setFuzzyValue(double newFuzzyValue) {
        fuzzyValue = newFuzzyValue;
        }

    /** Sets fuzzy value field to given value.
    @param newItemNum the given value.
    @param newFuzzyValue the given value. */

    public void setItemNumAndfValue(short newItemNum, double newFuzzyValue) { 
        itemNumber = newItemNum;        
        fuzzyValue = newFuzzyValue;
        }

    /** To string method.
    @return ouput string.    */

    public String toString() {
        // Convert
    	int numInt    = (int) ((fuzzyValue+0.005)*100.0);
        double numDub = ((double) numInt)/100.0;

        // Return
        return("<" + itemNumber + "," + numDub + ">");
	}

    /** To string method with schema label instead of number.
    @return ouput string.    */

    public String toString(String schemaLabel) {
        // Convert
    	int numInt    = (int) ((fuzzyValue+0.005)*100.0);
        double numDub = ((double) numInt)/100.0;

        // Return
        return("<" + schemaLabel + "," + numDub + ">");
	}
    }
