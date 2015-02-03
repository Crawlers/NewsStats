package com.cse10.gate;

import com.cse10.classifier.StanfordCoreNLPLemmatizer;
import gate.*;
import gate.annotation.AnnotationImpl;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import weka.core.tokenizers.NGramTokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * use to filter articles by keywords
 * I have updated gate gazetter lists with crime keyword list
 * Created by chamath on 12/21/2014.
 */
public class DocumentKeyWordFinder {
    private Corpus corpus;
    private CorpusPipeLine cp;
    private List annotationsRequired;
    private String gateHomeEnvVariableName;
    private boolean isGateHomeConfigured;
    private boolean isCorpusPipeLineConfigured;

    public DocumentKeyWordFinder(){
        isGateHomeConfigured=false;
        isCorpusPipeLineConfigured=false;
        gateHomeEnvVariableName="GATE_HOME";
    }

    public void setGateHomeEnvVariableName(String gateHomeEnvVariableName){
        this.gateHomeEnvVariableName=gateHomeEnvVariableName;
    }

    /**
     * configure home path of  gate
     */
    private void configureGateHome(){
        if(!isGateHomeConfigured) {
            //set gate home
            String homePath = "\\home";
            File gateHome;

            if (Gate.getGateHome() == null) {
                homePath = System.getenv(gateHomeEnvVariableName);
                if (homePath == null) {
                    //if environment variable is not set then prompt user to enter the path
                    System.out.print("Enter GATE Home path : ");
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(System.in));
                    try {
                        homePath = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Home Path= " + homePath);
                //check whether the provided gate path is correct
                File pathCheck = new File(homePath + "\\gate.xml");
                if (pathCheck.exists()) {
                    gateHome = new File(homePath);
                    Gate.setGateHome(gateHome);
                    System.out.println("GATE Home Configured : " + Gate.getGateHome());
                } else {
                    System.out.println("GATE Home Path Incorrect");
                    System.exit(0);
                }
            }
            isGateHomeConfigured=true;
        }
    }

    /**
     * configure Gate corpus pipe line
     */
    private void configureCorpusPipeLine(){
        if(!isCorpusPipeLineConfigured) {
            try {
                configureGateHome();
                //initialize data
                Gate.init();

            } catch (GateException ex) {
                Logger.getLogger(DocumentContentFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
            annotationsRequired = new ArrayList();
            ArrayList<String> annotationType = new ArrayList();
            annotationType.add("Lookup");

            ListIterator iter = annotationType.listIterator();
            while (iter.hasNext()) {
                String annotation = (String) iter.next();
                annotationsRequired.add(annotation);
            }
            try {
                corpus = Factory.newCorpus("StandAloneAnnie corpus"); // create corpus
            } catch (ResourceInstantiationException ex) {
                Logger.getLogger(DocumentContentFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
            cp = new CorpusPipeLine();
            cp.configure(false);
            isCorpusPipeLineConfigured=true;
        }
    }

    /**
     * check given keywords exist in articles
     *
     * @param content
     * @return
     */
    public boolean isKeyWordExist(String content) {
        configureCorpusPipeLine();
        Document doc;
        boolean exist = false;
        if (content.length()>0) {
            try {
                doc = Factory.newDocument(content); // create new gate document
                corpus.add(doc);
                cp.setCorpus(corpus);
                try {
                    cp.execute();
                } catch (ExecutionException ex) {
                    Logger.getLogger(DocumentContentFilter.class.getName()).log(Level.SEVERE, null, ex);
                }
                corpus.clear();
                String docXMLString = null;
                Set annotationsToWrite = new HashSet();

                /**
                 * if we want to just write out specific annotation types, we must
                 * extract the annotations into a Set *
                 */
                if (annotationsRequired != null) {
                    /**
                     * Create a temporary Set to hold the annotations we wish to
                     * write out we only extract annotations from the default
                     * (unnamed) AnnotationSet in this example *
                     */
                    AnnotationSet defaultAnnots = doc.getAnnotations();
                    Iterator annotTypesIt = annotationsRequired.iterator();
                    while (annotTypesIt.hasNext()) {
                        /**
                         * extract all the annotations of each requested type and
                         * add them to the temporary set *
                         */
                        AnnotationSet annotsOfThisType =
                                defaultAnnots.get((String) annotTypesIt.next());
                        if (annotsOfThisType != null) {
                            annotationsToWrite.addAll(annotsOfThisType);
                        }
                    }
                }

                // Release the document, as it is no longer needed
                Factory.deleteResource(doc);

                Iterator annotIt = annotationsToWrite.iterator();
                while (annotIt.hasNext()) {
                    /**
                     * extract all the annotations of each requested type and add
                     * them to the temporary set *
                     */
                    AnnotationImpl CurrentAnnot = (AnnotationImpl) annotIt.next();
                    if (CurrentAnnot.getType().equalsIgnoreCase("Lookup") && CurrentAnnot.getFeatures().get("majorType").equals("crime")) {
                        exist = true;
                    }
                }

            } catch (ResourceInstantiationException ex) {
            }
        }
        return exist;
    }

    /**
     * test the functionality of the class within class
     * @return
     */
    public boolean testFunctionality(){
        DocumentKeyWordFinder documentKeyWordFinder=new DocumentKeyWordFinder();
        NGramTokenizer tokenizer=new NGramTokenizer();
        tokenizer.setNGramMinSize(1);
        tokenizer.setNGramMaxSize(1);
        tokenizer.setDelimiters("\\W");
        String content="From W. A. Jayasekera, Dodamgaslanda Correspondent A three-year-old in Balawattala, Dodamgaslanda died of suffocation after a part of a tablet given to him got stuck in his throat. The deceased was H. U. G. Januka Dilruk Padmasiri. His mother had taken him to a private dispensary at the Balawattala Junction and obtained treatment for fever and some tablets had been prescribed. The mother administered first aid and took the child to the same doctor, who advised her to take him to hospital. On admission to the hospital the child was pronounced dead.";//From K. K. A. Thilakarathne Angunakolapelessa Correspondent A farmer was killed when a hand tractor, transporting vegetables to the market, collided with a tipper at Batawakumbuka in the Angunakolapelessa police area on Saturday (01) morning. The deceased was identified as K. Amaradasa (58) a father of three from Bedigantota Sooriyawewa. The driver of the tractor M. G. Rushan Maduranga, who was critically injured was admitted to Embilipitiya hospital. The driver of the tipper was arrested by the Angunak-olapelessa police. Investigations are being conducted by OIC Prasanna";//From Ranjan Jayawardane Weligama Correspodent A fishing craft, worth Rs 1.5 million, which had come ashore at the Mirissa fisheries harbour, was fully gutted on Friday. Some fishermen, who saw the incident, had rushed to the scene and extinguished the fire at midnight, but it was too late. Weligama Police are conducting investigations to ascertain whether it was set ablaze to settle a private vendetta.";//Six persons including two children were killed when an overloaded three wheeler they were travelling in collided with the Yal Devi Express train bound for Vavuniya from Colombo, at an unprotected railway crossing in Ambanpola, Kirimetiyawa yesterday morning. There were seven persons in the vehicle, three males and four females. A woman who was in a serious condition was admitted to the Kurunegala hospital. She succumbed to her injuries. Eye witnesses said that the bodies of the victims were beyond recognition. The driver’s identity was established after tracing his identity card. He was identified as Udaya Asiri Wijeratne. The victims were returning after visiting a relative and were on their way to a ‘Devalaya’";//Tamil Nadu Chief Minister J. Jayalalithaa Wednesday urged Prime Minister Manmohan Singh to tell Sri Lanka to immediately release the 40 Indian fishermen from jails there. In an open letter to Manmohan Singh, she said 22 of the fishermen were from Nagapattinam in Tamil Nadu and 18 from Karaikal in Puducherry. They were reportedly caught by the Sri Lanka Navy on December 3. \"The fishermen and their boats were reportedly taken to the Trincomalee harbour and it is learnt they have now been remanded till Dec 10. The Indian Navy has confirmed the arrests,\" Indian media quoted Jayalalithaa as having said. Expressing her ‘deep pain’ over such incidents, Jayalalithaa said that when Sri Lankan fishermen entered Indian waters, they were
        tokenizer.tokenize(content);
        String words="";
        StanfordCoreNLPLemmatizer lemmatizer=new StanfordCoreNLPLemmatizer();

        while (tokenizer.hasMoreElements()) {
            String element = (String) tokenizer.nextElement();
            words = words.concat(lemmatizer.stem(element));
            words = words.concat(" ");
        }
        System.out.println("Words="+words);
        boolean isKeyWordExist= documentKeyWordFinder.isKeyWordExist(words);
        return isKeyWordExist;
    }

    public static void main(String[] args) {
        System.out.println(new DocumentKeyWordFinder().testFunctionality());
    }
}
