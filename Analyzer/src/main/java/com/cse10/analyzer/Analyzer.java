package com.cse10.analyzer;

import com.cse10.database.HibernateUtil;
import com.cse10.results.NewsStatistic;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Sampath on 1/16/15.
 */
public class Analyzer {
    public void generateStats(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createSQLQuery("truncate table news_statistics").executeUpdate();
        session.createSQLQuery(
                "INSERT INTO news_statistics (crime_type, crime_district, crime_date, crime_year, crime_yearquarter, crime_count) " +
                        "SELECT crime_type,(SELECT district FROM location_district_mapper ldm WHERE ceg.location = ldm.location) , crime_date, YEAR (crime_date), CONCAT(YEAR (crime_date), ' - ', QUARTER(crime_date)), count(id) " +
                        "FROM crime_entity_group ceg " +
                        "WHERE crime_date >= '2012-01-01' AND crime_date <= '2014-12-31' " +
                        "GROUP BY crime_type, district, crime_date " +
                        "ORDER BY YEAR (crime_date)").executeUpdate();
        session.createSQLQuery("UPDATE news_statistics SET crime_type = 'Other' WHERE crime_type IS NULL OR crime_type = ''").executeUpdate();
        tx.commit();
        session.close();
    }
}