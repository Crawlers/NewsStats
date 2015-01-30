package com.cse10.extractor.gate;

/**
 * Created by Isuru on 1/26/2015.
 */
public class EntityExtractorTask implements Runnable {
    // entity extractor which runs extraction pipeline to extract entities
    private EntityExtractor eExtrator;

    EntityExtractorTask(){
        eExtrator = new EntityExtractor();
    }

    public EntityExtractor getEntityExtrator() {
        return eExtrator;
    }

    @Override
    public void run() {
        try {
            eExtrator.startExtraction();
        }catch (InterruptedException e){
            System.out.println("Pressed stop button with : "+e);
        }catch (Exception e){
            System.out.println("Stopped with : " + e);
        }finally {
            eExtrator.stopExtraction();
        }
    }
}
