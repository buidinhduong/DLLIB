package LIRMM;

public class Rule{
    protected Itemset premise;
    protected Itemset conclusion;
    protected float confidence;
    protected float support;
    
    public Rule(Itemset premise, Itemset conclusion) {
        this.premise = premise;
        this.conclusion = conclusion;
    }

    // Accessors
    
    public Itemset getPremise() {
        return premise;
    }

    public Itemset getConclusion() {
        return conclusion;
    }

    public double getConfidence() {
        confidence = ((float) support) / premise.getCardinality();
        //System.out.println(premise.toString()+", Support/ premise:"+getSupport()+"/"+premise.getCardinality()+"="+confidence);
        return confidence;
    }
    public void setSupport(float support) {
        this.support = support;
    }
    public float getSupport() {
        return support;
    }

    public double confidence() {
        return confidence;
    }
    
    // To String!
    public String toString() {
        
        return premise
                .toString()
                .concat(" => ")
                .concat(conclusion.toString())
                .concat(" (support:" + getSupport() + ", confidence: "
                        + getConfidence() + ")");
    }
    public Rule reverse()
    {
        return  new Rule(conclusion,premise);

    }

    public String description() {
        // TODO Auto-generated method stub
        return premise.toString() +"==>"+conclusion.toString();
    }
}
