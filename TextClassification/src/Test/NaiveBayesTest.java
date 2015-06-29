package Test;

import TextPreprocessing.Stemmer;
import TextPreprocessing.Stopwords;
import TrainingAlgorithm.*;
import ObjectModel.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.MediaSize.NA;

import org.junit.Test;
/**
 *
 * @author BUI DUONG
 */
public class NaiveBayesTest {
    public static final String PREDIFINE_CLASS_ENGLISH="English";
    public static final String PREDIFINE_CLASS_FRENCH="French";
    public static final String PREDIFINE_CLASS_VIETNAMESE="Vietnamese";
    /**
     * Reads the all lines from a file and places it a String array
     * 
     * @param url
     * @return
     * @throws IOException 
     */
    public static String[] readLines(URL url) throws IOException {

        Reader fileReader = new InputStreamReader(url.openStream(), Charset.forName("UTF-8"));
        List<String> lines;
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            lines = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines.toArray(new String[lines.size()]);
    }
    /**
     * Reads a file and places it a String
     * @param url
     * @return
     * @throws IOException 
     */
   public static String readFile(URL url) throws IOException {
       Reader fileReader = new InputStreamReader(url.openStream(), Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(fileReader);
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    /**
     * Main method
     * 
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        /**
         * Phase 1: Training
         */
        //map of dataset files
        Map<String, URL> trainingFiles = new HashMap<>();
        trainingFiles.put(NaiveBayesTest.PREDIFINE_CLASS_ENGLISH, NaiveBayesTest.class.getResource("/datasets/training.language.en.txt"));
        trainingFiles.put(NaiveBayesTest.PREDIFINE_CLASS_FRENCH, NaiveBayesTest.class.getResource("/datasets/training.language.fr.txt"));
        trainingFiles.put(NaiveBayesTest.PREDIFINE_CLASS_VIETNAMESE, NaiveBayesTest.class.getResource("/datasets/training.language.vi.txt"));

        //loading examples in memory
        Map<String, String[]> trainingExamples = new HashMap<>();
        for(Map.Entry<String, URL> entry : trainingFiles.entrySet()) {
            trainingExamples.put(entry.getKey(), readLines(entry.getValue()));
        }
        

        /*stop words dataset*/
        System.out.println("Loading stopwords...");
        Map<String, URL> stopwordsData = new HashMap<>();
        stopwordsData.put("VietnameseStopwords", NaiveBayesTest.class.getResource("/datasets/training.language.vi.stopwords.txt"));
        stopwordsData.put("FrenchStopwords", NaiveBayesTest.class.getResource("/datasets/training.language.fr.stopwords.txt"));
        for(Map.Entry<String, URL> entry : stopwordsData.entrySet()) {
            Stopwords.addStopword(readLines(entry.getValue()));
        }
        
        //train classifier
        NaiveBayes nb = new NaiveBayes();
        //nb.setChisquareCriticalValue(0); //0.01 pvalue
        System.out.println("Phase 1: Training to get Classifier Model...");
        nb.train(trainingExamples);
        //get trained classifier model
        ClassifierModel model = nb.getClassifierModel();
        model.printDesciption(false);
        //clear data
        nb = null;
        trainingExamples = null;
        //Evaluate the classifier model 
        //not yet implemented
        /**
         * Phase 2: Classifying and Evaluating the Model by Accurate rate
         */
        System.out.println("Phase 2: Classifying and Evaluating the Model (i selected Accuracy rate");
        //Use classifier model to classify
        nb = new NaiveBayes(model);
        ModelEvaluator evaluator=new ModelEvaluator(model);

//        String exampleEn = "Netflix is the world’s leading subscription service for watching TV episodes and movies on your phone. This Netflix mobile application delivers the best experience anywhere, anytime. Get the free app as a part of your Netflix membership and you can instantly watch thousands of TV episodes & movies on your phone. If you are not a Netflix member sign up for Netflix and start enjoying immediately on your phone with our one-month free trial. How does Netflix work? • Netflix membership gives you access to unlimited TV shows and movies for one low monthly price. • With the Netflix app you can instantly watch as many TV episodes & movies as you want, as often as you want, anytime you want. • You can Browse a growing selection of thousands of titles, and new episodes that are added regularly. • Search for titles and watch immediately on your phone or on an ever expanding list of supported devices. • Rate your favorite shows and movies and tell us what you like so Netflix can help suggest the best titles for you. Note: Video out is supported on all generations of iPad, iPhone 4 and later generations and iPod (4th generation) and later generations. For assistance, please visit help.netflix.com.";
//        String outputEn = nb.classify(exampleEn);
//        System.out.format("The text \"%s\" was classified as \"%s\".%n", exampleEn, outputEn);
//        
//        String exampleFr = "AlphaBetty : un nouveau jeu de mots passionnant par les créateurs de Candy Crush Saga ! Mettez vos talents à l'épreuve dans un tout nouveau jeu de mots qui vous emmènera à l'aventure aux quatre coins du monde des souris ! Rejoignez le professeur Alpha, Betty et leur fidèle ami Barney dans une quête épique à la recherche de nouveaux mots pour terminer l'Encyclopédie ultime ! Parcourez des terres exotiques dans ce tout nouveau jeu de mots et de réflexion, à travers 100 niveaux scouic-tastiques ! En chemin, collectionnez des personnages spéciaux pour vous aider, mais attention aux souricières et aux lianes ! Jouez en solo ou avec vos amis pour voir qui décrochera le meilleur score dans le jeu idéal pour faire étinceler vos talents pour les mots ! Tout est prêt ? Que l'aventure commence ! AlphaBetty Saga est un jeu entièrement gratuit. Toutefois, certains objets en jeu comme les déplacements ou les vies supplémentaires sont payants. Vous pouvez désactiver la fonction de paiement en désactivant les achats intégrés dans les réglages de votre appareil. AlphaBetty Saga vous propose : ● Des graphismes gais et colorés qui vous transporteront aux quatre coins du monde des souris ; ● Trois explorateurs intrépides dans une aventure épique ; ● Des boosters puissants pour vous aider à venir à bout des niveaux les plus complexes ; ● Des boosters et des personnages originaux dotés de capacités spéciales pour vous donner un coup de main en chemin ; ● Un jeu simple et divertissant, mais pas si facile à maîtriser ; ● Plus de 100 niveaux scouic-tastiques pour s'amuser avec les mots ; ● Des classements pour suivre l'avancée de vos amis et adversaires ! ● Une synchronisation facile du jeu d'un appareil à l'autre et le déblocage des fonctionnalités du jeu complet en se connectant à Internet. Déjà fan d'AlphaBetty Saga ? Cliquez J'aime sur Facebook ou suivez-nous sur Twitter pour toutes les dernières nouvelles : https://twitter.com/AlphabettySaga https://www.facebook.com/alphabettysaga  Enfin et surtout, un grand MERCI à tous les joueurs d'AlphaBetty Saga !";
//        String outputFr = nb.classify(exampleFr);
//        System.out.format("The text \"%s\" was classified as \"%s\".%n", exampleFr, outputFr);
//                
//        String exampleVi = " Xin chào, tôi là một chàng trai, tôi là người việt ";
//        String outputVi = nb.classify(exampleVi);
//        System.out.format("The text \"%s\" was classified as \"%s\".%n", exampleVi, outputVi);
        
        //real test
        System.out.println("Testing real data...");
        //load resrouce for english dataset
        Map<String, URL> testDataResources = new HashMap<>();
        testDataResources.put("testDataResourcesEn", NaiveBayesTest.class.getResource("/AppstoreDataset/en/"));
        testDataResources.put("testDataResourcesFr", NaiveBayesTest.class.getResource("/AppstoreDataset/fr/"));
       testDataResources.put("testDataResourcesVi", NaiveBayesTest.class.getResource("/AppstoreDataset/vi/"));

        Map<String, URL> testData = new HashMap<>();
        for(Map.Entry<String, URL> resources : testDataResources.entrySet()) {   
           String[] allfiles=readLines(resources.getValue());
           for(String file : allfiles){
               if(file.contains("txt")){
               String folder=getLastBitFromUrl(resources.getValue().getPath());
               testData.put(file, NaiveBayesTest.class.getResource("/AppstoreDataset/"+folder+"/"+file));
               String predefineClass=NaiveBayesTest.PREDIFINE_CLASS_ENGLISH;
               if(folder.contains("fr"))
                   predefineClass=NaiveBayesTest.PREDIFINE_CLASS_FRENCH;
               if(folder.contains("vi"))
                   predefineClass=NaiveBayesTest.PREDIFINE_CLASS_VIETNAMESE;
               if(folder.contains("en"))
                   predefineClass=NaiveBayesTest.PREDIFINE_CLASS_ENGLISH;
               //set the info
               int newvalue=evaluator.classAndNumOfDocuments.get(predefineClass)==null?0:evaluator.classAndNumOfDocuments.get(predefineClass);
                evaluator.classAndNumOfDocuments.put(predefineClass,newvalue+1);               
               }
           }
        }
        for(Map.Entry<String, URL> doc : testData.entrySet()) {
            String input =readFile(doc.getValue());
            String output = nb.classify(input);
            int newValue=evaluator.classAndNumOfClassifiedDocuments.get(output)==null?0:evaluator.classAndNumOfClassifiedDocuments.get(output);
            evaluator.classAndNumOfClassifiedDocuments.put(output, newValue+1);
          System.out.format("The text in \"%s\" was classified as \"%s\".%n", doc.getKey(), output);
          String lang=doc.getKey().split("_")[1].replace(".txt", "");
          if(!output.toLowerCase().contains(lang))
          {
              System.out.println("detect file classify wrong "+doc.getKey());
              evaluator.b++;
          }else
          {
              /*the number of document objects belong to the classifying class and they are assigned to
              the class by classifier model (correct assignment)*/
              evaluator.a++;
          }
        }
        evaluator.printDescription();
    }
    public static String getLastBitFromUrl(final String url){
        return url.replaceFirst(".*/([^/?]+).*", "$1");
    }
    @Test
    public void testSteamer()
    {
        Stemmer stem=new Stemmer();
       System.out.print(stem.stem("does"));
        
    }
    @Test
    public void testRevmovingStopword()
    {
        //System.out.print(Stopwords.stemString("hacked"));
        System.out.print(Stopwords.removeStemmedStopWords("hello, i am boy, i am a teacher"));
    }
    @Test 
    public void testLoadStopwords()
    {
        //stopwords datasets
        Map<String, URL> stopwordsData = new HashMap<>();
        stopwordsData.put("VietnameseStopwords", NaiveBayesTest.class.getResource("/datasets/training.language.vi.stopwords.txt"));
        stopwordsData.put("FrenchStopwords", NaiveBayesTest.class.getResource("/datasets/training.language.fr.stopwords.txt"));
        //loading examples in memory
        try {

        System.out.println("Load stopwords");
        for(Map.Entry<String, URL> entry : stopwordsData.entrySet()) {
            Stopwords.addStopword(readLines(entry.getValue()));
            System.out.println(entry.getKey()+""+ readLines(entry.getValue()));
       }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
