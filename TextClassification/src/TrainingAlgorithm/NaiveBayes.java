package TrainingAlgorithm;

import ObjectModel.*;
import TextRepresentation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author BUI DUONG
 */
public class NaiveBayes {
    private double chisquareCriticalValue = 10.83; //equivalent to pvalue 0.001. It is used by feature selection algorithm
    private ClassifierModel classifierModel;
    
    /**
     * This constructor is used when we load an already train classifier
     * 
     * @param classifierModel 
     */
    public NaiveBayes(ClassifierModel classifierModel) {
        this.classifierModel = classifierModel;
    }
    
    /**
     * This constructor is used when we plan to train a new classifier.
     */
    public NaiveBayes() {
        this(null);
    }
    
    /**
     * Gets the classifierModel parameter
     * 
     * @return 
     */
    public ClassifierModel getClassifierModel() {
        return classifierModel;
    }
    
    /**
     * Gets the chisquareCriticalValue paramter.
     * 
     * @return 
     */
    public double getChisquareCriticalValue() {
        return chisquareCriticalValue;
    }
    
    /**
     * Sets the chisquareCriticalValue parameter.
     * 
     * @param chisquareCriticalValue 
     */
    public void setChisquareCriticalValue(double chisquareCriticalValue) {
        this.chisquareCriticalValue = chisquareCriticalValue;
    }
    
    /**
     * Preprocesses the original dataset and converts it to a List of Documents.
     * 
     * @param trainingDataset
     * @return 
     */
    private List<Document> preprocessDataset(Map<String, String[]> trainingDataset) {
        List<Document> dataset = new ArrayList<>();
                
        String category;
        String[] examples;
        
        Document doc;
        
        Iterator<Map.Entry<String, String[]>> it = trainingDataset.entrySet().iterator();
        
        //loop through all the categories and training examples
        while(it.hasNext()) {
            Map.Entry<String, String[]> entry = it.next();
            category = entry.getKey();
            examples = entry.getValue();
            
            for(int i=0;i<examples.length;++i) {
                //for each example in the category tokenize its text and convert it into a Document object.
                
                doc = TextTokenizer.tokenize(examples[i]);
                doc.category = category;
                dataset.add(doc);
                
                examples[i] = null; //try freeing some memory
            }
            
            it.remove(); //try freeing some memory
        }
        
        return dataset;
    }
    
    /**
     * Gathers the required counts for the features and performs feature selection
     * on the above counts. It returns a DatasetFeature object that is later used 
     * for calculating the probabilities of the model.
     * 
     * @param dataset
     * @return 
     */
    private DatasetFeature selectFeatures(List<Document> dataset) {        
        FeatureExtraction featureExtractor = new FeatureExtraction();
        
        //the DatasetFeature object contains statistics about all the features found in the documents
        DatasetFeature stats = featureExtractor.extractDatasetFeature(dataset); //extract the stats of the dataset
        
        //we pass this information to the feature selection algorithm and we get a list with the selected features
        Map<String, Double> selectedFeatures = featureExtractor.chisquare(stats, chisquareCriticalValue);
        
        //clip from the stats (DatasetFeature) all the features that are not selected
        Iterator<Map.Entry<String, Map<String, Integer>>> it = stats.featureCategoryJointCount.entrySet().iterator();
        while(it.hasNext()) {
            String feature = it.next().getKey();
        
            if(selectedFeatures.containsKey(feature)==false) {
                //if the feature is not in the selectedFeatures list remove it
                it.remove();
            }
        }
        
        return stats;
    }
    
    /**
     * Trains a Naive Bayes classifier by using the Multinomial Model by passing
     * the trainingDataset and the prior probabilities.
     * 
     * @param trainingDataset
     * @param categoryPriors
     * @throws IllegalArgumentException 
     */
    public void train(Map<String, String[]> trainingDataset, Map<String, Double> categoryPriors) throws IllegalArgumentException {
        //preprocess the given dataset
        List<Document> dataset = preprocessDataset(trainingDataset);
        //produce the feature stats and select the best features
        DatasetFeature datasetFeatureStats =  selectFeatures(dataset);
        //intiliaze the classifierModel of the classifier
        classifierModel = new ClassifierModel();
        classifierModel.numOfObservations = datasetFeatureStats.numOfObservations; //number of observations
        classifierModel.numOfFeatures = datasetFeatureStats.featureCategoryJointCount.size(); //number of features
        //check is prior probabilities are given
        if(categoryPriors==null) { 
            //if not estimate the priors from the sample
            classifierModel.numOfCategories = datasetFeatureStats.categoryCounts.size(); //number of cateogries
            classifierModel.logPriors = new HashMap<>();
            
            String category;
            int count;
            for(Map.Entry<String, Integer> entry : datasetFeatureStats.categoryCounts.entrySet()) {
                category = entry.getKey();
                count = entry.getValue();
                
                classifierModel.logPriors.put(category, Math.log((double)count/classifierModel.numOfObservations));
            }
        }
        else {
            //if they are provided then use the given priors
            classifierModel.numOfCategories = categoryPriors.size();
            
            //make sure that the given priors are valid
            if(classifierModel.numOfCategories!=datasetFeatureStats.categoryCounts.size()) {
                throw new IllegalArgumentException("Invalid priors Array: Make sure you pass a prior probability for every supported category.");
            }
            
            String category;
            Double priorProbability;
            for(Map.Entry<String, Double> entry : categoryPriors.entrySet()) {
                category = entry.getKey();
                priorProbability = entry.getValue();
                if(priorProbability==null) {
                    throw new IllegalArgumentException("Invalid priors Array: Make sure you pass a prior probability for every supported category.");
                }
                else if(priorProbability<0 || priorProbability>1) {
                    throw new IllegalArgumentException("Invalid priors Array: Prior probabilities should be between 0 and 1.");
                }
                
                classifierModel.logPriors.put(category, Math.log(priorProbability));
            }
        }
        
        //We are performing laplace smoothing (also known as add-1). This requires to estimate the total feature occurrences in each category
        Map<String, Double> featureOccurrencesInCategory = new HashMap<>();
        
        Integer occurrences;
        Double featureOccSum;
        for(String category : classifierModel.logPriors.keySet()) {
            featureOccSum = 0.0;
            for(Map<String, Integer> categoryListOccurrences : datasetFeatureStats.featureCategoryJointCount.values()) {
                occurrences=categoryListOccurrences.get(category);
                if(occurrences!=null) {
                    featureOccSum+=occurrences;
                }
            }
            featureOccurrencesInCategory.put(category, featureOccSum);
        }
        
        //estimate log likelihoods
        String feature;
        Integer count;
        Map<String, Integer> featureCategoryCounts;
        double logLikelihood;
        for(String category : classifierModel.logPriors.keySet()) {
            for(Map.Entry<String, Map<String, Integer>> entry : datasetFeatureStats.featureCategoryJointCount.entrySet()) {
                feature = entry.getKey();
                featureCategoryCounts = entry.getValue();
                
                count = featureCategoryCounts.get(category);
                if(count==null) {
                    count = 0;
                }
                
                logLikelihood = Math.log((count+1.0)/(featureOccurrencesInCategory.get(category)+classifierModel.numOfFeatures));
                if(classifierModel.logLikelihoods.containsKey(feature)==false) {
                    classifierModel.logLikelihoods.put(feature, new HashMap<String, Double>());
                }
                classifierModel.logLikelihoods.get(feature).put(category, logLikelihood);
            }
        }
        featureOccurrencesInCategory=null;
    }
    
    /**
     * Wrapper method of train() which enables the estimation of the prior 
     * probabilities based on the sample.
     * 
     * @param trainingDataset 
     */
    public void train(Map<String, String[]> trainingDataset) {
        train(trainingDataset, null);
    }
    
    /**
     * Predicts the category of a text by using an already trained classifier
     * and returns its category.
     * 
     * @param text
     * @return 
     * @throws IllegalArgumentException
     */
    public String classify(String text) throws IllegalArgumentException {
        if(classifierModel == null) {
            throw new IllegalArgumentException("Classifier Model missing: Make sure you train first a classifier before you use it.");
        }
        
        //Tokenizes the text and creates a new document
        Document doc = TextTokenizer.tokenize(text);
        
        String category;
        String feature;
        Integer occurrences;
        Double logprob;
        
        String maxScoreCategory = null;
        Double maxScore=Double.NEGATIVE_INFINITY;
        
        //Map<String, Double> predictionScores = new HashMap<>();
        for(Map.Entry<String, Double> entry1 : classifierModel.logPriors.entrySet()) {
            category = entry1.getKey();
            logprob = entry1.getValue(); //intialize the scores with the priors
            
            //for each feature of the document
            for(Map.Entry<String, Integer> entry2 : doc.features.entrySet()) {
                feature = entry2.getKey();
                
                if(!classifierModel.logLikelihoods.containsKey(feature)) {
                    continue; //if the feature does not exist in the knowledge base skip it
                }
              //get its occurrences in text
                occurrences = entry2.getValue(); 
                //multiply loglikelihood score with occurrences
                logprob += occurrences*classifierModel.logLikelihoods.get(feature).get(category);
            }
           // predictionScores.put(category, logprob); 
            
            if(logprob>maxScore) {
                maxScore=logprob;
                maxScoreCategory=category;
            }
        }
        //return the category with highest score
        return maxScoreCategory;
    }
}
