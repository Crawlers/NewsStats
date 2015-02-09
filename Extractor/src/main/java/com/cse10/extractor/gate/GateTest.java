package com.cse10.extractor.gate;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Isuru Jayaweera
 * Date: 01/02/15
 * Test whether all the required entities are extracted and properly stored in the tables.
 */

public class GateTest {

    // initialize logger
    private static Logger logger = Logger.getLogger(GateTest.class);

    public static boolean doTest() throws Exception {
        boolean testSuccess = false;

        /*DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        ArrayList<CrimeEntityGroup> entityGroupList = (ArrayList<CrimeEntityGroup>) DatabaseHandler.fetchCrimeEntityGroupsByIdRange(1,10);
        int i = 0;

        for(CrimeEntityGroup entity : entityGroupList){
            i++;

            // check whether all crime details are properly stored
            logger.info("Entity Set : " + i + " -Begins Here-");

            // check type of the crime
            if (entity.getCrimeType() != null) {
                logger.info("Crime Type : " + entity.getCrimeType());
            }

            // check date of the crime
            if (entity.getCrimeDate() != null) {
                logger.info("Crime Date : " + format.format(entity.getCrimeDate()));
            }

            // check location of the crime
            if (entity.getLocationDistrict().getLocation() != null) {
                logger.info("Crime Location : " + entity.getLocationDistrict().getLocation());
            }

            // check district of the crime
            if (entity.getLocationDistrict() != null) {
                logger.info("District : " + entity.getLocationDistrict().getDistrict());
            }

            // check police location related with the crime
            if (entity.getPolice() != null) {
                logger.info("Police Location : " + entity.getPolice());
            }

            // check court location related with the crime
            if (entity.getCourt() != null) {
                logger.info("Court Location : " + entity.getCourt());
            }

            // check people who involved in the crime
            if(entity.getCrimePersonSet() != null && !entity.getCrimePersonSet().isEmpty()){
                Set<CrimePerson> people = entity.getCrimePersonSet();

                System.out.print("Crime People");

                for(CrimePerson person : people){
                    System.out.print(" : " + person.getName());
                }

                logger.info("");
            }

            logger.info("Entity Set : " + i + " -Ends Here-");
            logger.info("");
        }

        logger.info("- Test Finishes Here -");*/

        // Test running in separate thread

        try {
            EntityExtractorTask r = new EntityExtractorTask();
            Thread t = new Thread(r);

            t.start();

            Thread.currentThread().sleep(9000);

            logger.info("Check");
            //t.interrupt();

            t.join();
            testSuccess = true;
        }catch (Exception e){
            logger.info("Exception Occurred : ", e);
        }

        return testSuccess;
    }
}

