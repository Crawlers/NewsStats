package com.cse10.analyzer;

import com.cse10.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by Sampath on 1/16/15.
 */
public class Analyzer{

    /*
     * generating statistics
     */
    public void generateStats(String from, String to){

        //creating the db connection
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        //deleting current contents in the table
        session.createSQLQuery("truncate table news_statistics").executeUpdate();

        //executing the query
        session.createSQLQuery(
                "INSERT INTO news_statistics (crime_type, crime_district, crime_date, crime_year, crime_yearquarter, crime_count) " +
                        "SELECT crime_type,(SELECT district FROM location_district_mapper ldm WHERE ceg.location = ldm.location) dist, crime_date, YEAR (crime_date), CONCAT(YEAR (crime_date), ' - ', QUARTER(crime_date)), count(id) " +
                        "FROM crime_entity_group ceg " +
                        "WHERE crime_date >= '"+from+"' AND crime_date <= '"+to+"' AND label = 'unique'" +
                        "GROUP BY crime_type, dist, crime_date " +
                        "ORDER BY YEAR (crime_date)").executeUpdate();

        session.createSQLQuery("UPDATE news_statistics SET crime_type = 'Other' WHERE crime_type IS NULL OR crime_type = ''").executeUpdate();
        tx.commit();
        session.close();
    }
}