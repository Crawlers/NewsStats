package com.cse10.extractor.gate;

import org.apache.log4j.Logger;

/**
 * Created by Isuru on 1/26/2015.
 */
public class EntityExtractorTask implements Runnable {
    // entity extractor which runs extraction pipeline to extract entities
    private EntityExtractor eExtrator;

    // declare logger
    private Logger logger;

    public EntityExtractorTask(){
        eExtrator = new EntityExtractor();
        logger = Logger.getLogger(this.getClass());
    }

    public EntityExtractor getEntityExtrator() {
        return eExtrator;
    }

    @Override
    public void run() {
        try {
            // starting extraction process
            eExtrator.startExtraction();
        }catch (InterruptedException e){
            logger.info("Pressed stop button with : ", e);
        }catch (Exception e){
            logger.info("Stopped with : ", e);
        }finally {
            // stop extraction process
            eExtrator.stopExtraction();
        }
    }
}
