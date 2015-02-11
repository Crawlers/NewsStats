package com.cse10.gate;

import com.cse10.database.DatabaseConstants;
import junit.framework.TestCase;
import org.junit.*;

import com.cse10.classifier.StanfordCoreNLPLemmatizer;
import weka.core.tokenizers.NGramTokenizer;

public class DocumentKeyWordFinderTest {

    private String testContent;
    static String previousDB;

    @BeforeClass
    public static void setUpClass() throws Exception {
        previousDB = DatabaseConstants.DB_URL;
        DatabaseConstants.DB_URL = "jdbc:mysql://localhost:3306/newsstats_test";
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DatabaseConstants.DB_URL = previousDB;
    }


    @Before
    public void setUp() throws Exception {
        NGramTokenizer tokenizer = new NGramTokenizer();
        tokenizer.setNGramMinSize(1);
        tokenizer.setNGramMaxSize(1);
        tokenizer.setDelimiters("\\W");
        String content = "From W. A. Jayasekera, Dodamgaslanda Correspondent A three-year-old in Balawattala, Dodamgaslanda died of suffocation after a part of a tablet given to him got stuck in his throat. The deceased was H. U. G. Januka Dilruk Padmasiri. His mother had taken him to a private dispensary at the Balawattala Junction and obtained treatment for fever and some tablets had been prescribed. The mother administered first aid and took the child to the same doctor, who advised her to take him to hospital. On admission to the hospital the child was pronounced dead.";//From K. K. A. Thilakarathne Angunakolapelessa Correspondent A farmer was killed when a hand tractor, transporting vegetables to the market, collided with a tipper at Batawakumbuka in the Angunakolapelessa police area on Saturday (01) morning. The deceased was identified as K. Amaradasa (58) a father of three from Bedigantota Sooriyawewa. The driver of the tractor M. G. Rushan Maduranga, who was critically injured was admitted to Embilipitiya hospital. The driver of the tipper was arrested by the Angunak-olapelessa police. Investigations are being conducted by OIC Prasanna";//From Ranjan Jayawardane Weligama Correspodent A fishing craft, worth Rs 1.5 million, which had come ashore at the Mirissa fisheries harbour, was fully gutted on Friday. Some fishermen, who saw the incident, had rushed to the scene and extinguished the fire at midnight, but it was too late. Weligama Police are conducting investigations to ascertain whether it was set ablaze to settle a private vendetta.";//Six persons including two children were killed when an overloaded three wheeler they were travelling in collided with the Yal Devi Express train bound for Vavuniya from Colombo, at an unprotected railway crossing in Ambanpola, Kirimetiyawa yesterday morning. There were seven persons in the vehicle, three males and four females. A woman who was in a serious condition was admitted to the Kurunegala hospital. She succumbed to her injuries. Eye witnesses said that the bodies of the victims were beyond recognition. The driver’s identity was established after tracing his identity card. He was identified as Udaya Asiri Wijeratne. The victims were returning after visiting a relative and were on their way to a ‘Devalaya’";//Tamil Nadu Chief Minister J. Jayalalithaa Wednesday urged Prime Minister Manmohan Singh to tell Sri Lanka to immediately release the 40 Indian fishermen from jails there. In an open letter to Manmohan Singh, she said 22 of the fishermen were from Nagapattinam in Tamil Nadu and 18 from Karaikal in Puducherry. They were reportedly caught by the Sri Lanka Navy on December 3. \"The fishermen and their boats were reportedly taken to the Trincomalee harbour and it is learnt they have now been remanded till Dec 10. The Indian Navy has confirmed the arrests,\" Indian media quoted Jayalalithaa as having said. Expressing her ‘deep pain’ over such incidents, Jayalalithaa said that when Sri Lankan fishermen entered Indian waters, they were
        tokenizer.tokenize(content);
        String words = "";
        StanfordCoreNLPLemmatizer lemmatizer = new StanfordCoreNLPLemmatizer();

        while (tokenizer.hasMoreElements()) {
            String element = (String) tokenizer.nextElement();
            words = words.concat(lemmatizer.stem(element));
            words = words.concat(" ");
        }
        testContent = words;
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSetGateHomeEnvVariableName() throws Exception {

    }

    @Test
    public void testIsKeyWordExist() throws Exception {
        DocumentKeyWordFinder documentKeyWordFinder = new DocumentKeyWordFinder();
        TestCase.assertEquals(true, documentKeyWordFinder.isKeyWordExist(testContent));
    }

    @Test
    public void testTestFunctionality() throws Exception {
        DocumentKeyWordFinder documentKeyWordFinder = new DocumentKeyWordFinder();
        TestCase.assertEquals(true, documentKeyWordFinder.testFunctionality());
    }
}