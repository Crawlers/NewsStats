package com.cse10.util;

import com.cse10.article.*;
import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import com.cse10.entities.CrimeEntityGroup;
import com.cse10.entities.CrimePerson;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by TharinduWijewardane on 2015-02-07.
 */
public class TableCleaner {

    private static Logger logger = Logger.getLogger(TableCleaner.class);

    /**
     * Undo the classification (so that it can be performed from the beginning again)
     *
     * @param deleteCrimeArticles : if the article_crime table should be emptied too
     */
    public static void undoClassifications(boolean deleteCrimeArticles) {

        String tableCeylonToday = DatabaseConstants.classToTableName.get(CeylonTodayArticle.class);
        String tableDailyMirror = DatabaseConstants.classToTableName.get(DailyMirrorArticle.class);
        String tableNewsFirst = DatabaseConstants.classToTableName.get(NewsFirstArticle.class);
        String tableTheIsland = DatabaseConstants.classToTableName.get(TheIslandArticle.class);

        DatabaseHandler.executeUpdate("UPDATE " + tableCeylonToday + " SET label = NULL");
        DatabaseHandler.executeUpdate("UPDATE " + tableDailyMirror + " SET label = NULL");
        DatabaseHandler.executeUpdate("UPDATE " + tableNewsFirst + " SET label = NULL");
        DatabaseHandler.executeUpdate("UPDATE " + tableTheIsland + " SET label = NULL");

        if (deleteCrimeArticles) {
            String tableCrimeArticles = DatabaseConstants.classToTableName.get(CrimeArticle.class);
            DatabaseHandler.executeUpdate("DELETE FROM " + tableCrimeArticles);
        }
    }

    /**
     * Undo the entity extraction (so that it can be performed from the beginning again)
     */
    public static void undoEntityExtraction() {

        DatabaseHandler.deleteAll(CrimePerson.class);
        DatabaseHandler.deleteAll(CrimeEntityGroup.class);

        // set the last extraction performed crime article id to 0
        File configFile = new File("Extractor/src/main/resources/Configuration.txt");
        FileWriter fooWriter = null; // true to append
        try {
            fooWriter = new FileWriter(configFile, false);
            // false to overwrite.
            fooWriter.write("0");
        } catch (IOException e) {
            logger.info("Error: ", e);
        } finally {
            try {
                fooWriter.close();
            } catch (IOException ex) {
                logger.info("Error: ", ex);
            }
        }

    }

    /**
     * Undo the duplicate detection (so that it can be performed from the beginning again)
     */
    public static void undoDuplicateDetection() {

        String tableName = DatabaseConstants.classToTableName.get(CrimeEntityGroup.class);
        DatabaseHandler.executeUpdate("UPDATE " + tableName + " SET label = NULL");
    }

}
