package com.cse10.analyzer;

import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import com.cse10.database.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class AnalyzerTest {
    private Analyzer analyzer;
    private static String previousDB;

    public AnalyzerTest() throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createSQLQuery("truncate table news_statistics").executeUpdate();
        tx.commit();
        session.close();
        analyzer = new Analyzer();
    }

    @BeforeClass
    public static void setUp() throws Exception {
        previousDB = DatabaseConstants.DB_URL;
        DatabaseConstants.DB_URL = "jdbc:mysql://localhost:3306/newsstats";
    }

    @AfterClass
    public static void tearDown() throws Exception {
        DatabaseConstants.DB_URL = previousDB;
        DatabaseHandler.closeDatabase();
    }

    @Test
    public void testGenerateStats() throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String sql = "SELECT * FROM news_statistics";
        SQLQuery query = session.createSQLQuery(sql);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List results = query.list();
        assertEquals(0,results.size());
        session.close();

        //test if table is empty
        assertEquals(0, results.size());

        analyzer.generateStats("2012-01-01", "2014-12-31");

        session = HibernateUtil.getSessionFactory().openSession();
        sql = "SELECT SUM(crime_count) as count FROM news_statistics";
        query = session.createSQLQuery(sql);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        results = query.list();
        int noOfCrimesOfOutput = ((BigDecimal) ((HashMap) results.get(0)).get("count")).intValue();

        sql = "SELECT count(id) as count FROM crime_entity_group WHERE label = 'unique' AND (YEAR(crime_date) = 2012 OR YEAR(crime_date) = 2013 OR YEAR(crime_date) = 2014)";
        query = session.createSQLQuery(sql);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        results = query.list();
        int noOfCrimesOfInput = ((BigInteger) ((HashMap) results.get(0)).get("count")).intValue();
        session.close();

        //test if crime counts are equal in the input and output
        assertEquals(noOfCrimesOfInput, noOfCrimesOfOutput);
    }
}