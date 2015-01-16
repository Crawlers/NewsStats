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
 * Created by root on 1/16/15.
 */
public class StatGenerator {
    public void generateStats(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.createSQLQuery("truncate table news_statistics").executeUpdate();
        Query query = session.createSQLQuery(
                "SELECT crime_type,(SELECT district FROM location_district_mapper ldm WHERE ceg.location = ldm.location) , crime_date, YEAR (crime_date), CONCAT(YEAR (crime_date), ' - ', QUARTER(crime_date)), count(id) " +
                        "FROM crime_entity_group ceg " +
                        "WHERE crime_date >= '2012-01-01' AND crime_date <= '2014-12-31' " +
                        "GROUP BY crime_type, district, crime_date " +
                        "ORDER BY YEAR ('crime_date')");
        List<Object[]> entities = query.list();


        for(Object[] row : entities){
            Transaction tx = session.beginTransaction();
            NewsStatistic stat = new NewsStatistic();
            stat.setCrimeType((row[0] != null)?row[0].toString():"Other");
            stat.setCrimeDistrict((row[1] != null)?row[1].toString():"Other");

            DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            try {
                Date date = format.parse((row[2] != null)?row[2].toString():null);
                stat.setCrimeDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            stat.setCrimeYear((row[3] != null)?row[3].toString():null);
            stat.setCrimeYearQuarter((row[4] != null)?row[4].toString():null);
            stat.setCrimeCount(Integer.parseInt(row[5].toString()));
            session.save(stat);

            tx.commit();
        }
    }
}