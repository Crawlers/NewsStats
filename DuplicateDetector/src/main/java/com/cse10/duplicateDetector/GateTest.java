package com.cse10.duplicateDetector;

import com.cse10.classifier.StanfordCoreNLPLemmatizer;
import com.cse10.gate.DocumentKeyWordFinder;
import weka.core.tokenizers.NGramTokenizer;

/**
 * TODO remove this class
 * Created by hp on 1/25/2015.
 */
public class GateTest {

    public static void main(String[] args) {
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
        boolean keyWordExist= documentKeyWordFinder.isKeyWordExist(words);//"'A Colombo bound bus from Andana in Kahawatta ran off the road at Koskelle in Kahawatta and went down a precipice this morning, causing injuries to 30 passengers and the driver of the bus. Police said six of the injured in critical condition were transferred to the Ratnapura General Hospital. The driver of the bus M. Seneviratne said he lost control of the bus due to a technical defect. He said the residents of the area who rushed to the scene rescued the injured including him from a precipice more than 300 feet deep. “When the bus that ran off the road and was rolling down the precipice, I had no alternative but to hold the steering wheel fast. All of the passengers excluding one were injured. The bus that had developed a similar technical defect in Avissawella the previous day and it was repaired,” he said. The Traffic Division of the Kahawatta Police are conducting further inquiries.");//A Colombo bound bus from Andana in Kahawatta ran off the road at Koskelle in Kahawatta and went down a precipice this morning, causing injuries to 30 passengers and the driver of the bus. Police said six of the injured in critical condition were transferred to the Ratnapura General Hospital. The driver of the bus M. Seneviratne said he lost control of the bus due to a technical defect. He said the residents of the area who rushed to the scene rescued the injured including him from a precipice more than 300 feet deep. “When the bus that ran off the road and was rolling down the precipice, I had no alternative but to hold the steering wheel fast. All of the passengers excluding one were injured. The bus that had developed a similar technical defect in Avissawella the previous day and it was repaired,” he said. The Traffic Division of the Kahawatta Police are conducting further inquiries.");
        System.out.println(keyWordExist);
    }
}
