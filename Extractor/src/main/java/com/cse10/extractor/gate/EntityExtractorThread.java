package com.cse10.extractor.gate;

/**
 * Created by Isuru on 1/26/2015.
 */
public class EntityExtractorThread implements Runnable {
    @Override
    public void run() {
        try {
            EntityExtractor.startExtraction();
        }catch (InterruptedException e){
            System.out.println("Pressed stop button with : "+e);
        }catch (Exception e){
            System.out.println("Stopped with : " + e);
        }finally {
            EntityExtractor.stopExtraction();
        }
    }
}
