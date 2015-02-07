package com.cse10.crawler;

import com.cse10.database.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sampath liyanage on 8/31/14.
 * edited by Tharindu on 2014-10-29.
 * This class gives resuming support for the crawler
 */
public class DateHandler {

    private static Logger logger = Logger.getLogger(DateHandler.class);

    public static Date getFromDateToResume(Date startingDate, String tableName) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        String q = "select max(created_date) from " + tableName;
        List results = session.createSQLQuery(q).list();
        Iterator iterator = results.iterator();
        Date latestDateCrawled = (Date) iterator.next();

        if (latestDateCrawled == null) { // if database is empty
            return startingDate;
        }

        /* because Hiru News is crawled month at a time */
        if (tableName == "article_hiru_news") {
            Calendar cal = Calendar.getInstance();
            cal.setTime(latestDateCrawled);
            cal.set(Calendar.DATE, 1); // set the first date of the given month
            latestDateCrawled = cal.getTime();
        }

        /* because New York Times is crawled month at a time */
        if (tableName == "article_new_york_times") {
            Calendar cal = Calendar.getInstance();
            cal.setTime(latestDateCrawled);
            cal.set(Calendar.DATE, 1); // set the first date of the given month
            latestDateCrawled = cal.getTime();
        }

        if (latestDateCrawled.compareTo(startingDate) < 0) { // if starting date > latest date
            return startingDate;
        }

        /* to delete latest date (because it maybe unfinished) */
        Session session1 = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction1 = session1.beginTransaction();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        q = "delete from " + tableName + " where created_date >= '" + sdf.format(latestDateCrawled) + "'"; // logically only the equivalence is significant here (except hiru news)
        session1.createSQLQuery(q).executeUpdate();
        transaction1.commit();
        logger.info("DELETED unfinished date");

        return latestDateCrawled;
    }
}
