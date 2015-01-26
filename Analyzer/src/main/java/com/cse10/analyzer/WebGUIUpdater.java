package com.cse10.analyzer;

/**
 * Created by sampath on 1/26/15.
 */

import com.cse10.database.HibernateUtil;
import com.google.gson.Gson;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import com.mongodb.util.JSON;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import static java.util.concurrent.TimeUnit.SECONDS;

public class WebGUIUpdater {
    MongoClient mongoClient;
    String dbName;


    public WebGUIUpdater(String user, String password, String dbName, String host, int port){
        this.dbName = dbName;
        MongoCredential credential = MongoCredential.createMongoCRCredential(user, dbName, password.toCharArray());
        try {
            ServerAddress address = new ServerAddress(host,port);
            mongoClient = new MongoClient(address, Arrays.asList(credential));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void update(String sqlTable, String mongoCollection){
        DB db = mongoClient.getDB(dbName);
        DBCollection collection = db.getCollection(mongoCollection);
        BasicDBObject mQuery = new BasicDBObject();
        collection.remove(mQuery);

        Session session = HibernateUtil.getSessionFactory().openSession();
        String sql = "SELECT * FROM "+sqlTable;
        SQLQuery sQuery = session.createSQLQuery(sql);
        sQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List results = sQuery.list();
        session.close();
//        String json = JSONArray.toJSONString(results);
//        json = json.replaceFirst("\\[", "}");
//        int ind = json.lastIndexOf("]");
//        json = new StringBuilder(json).replace(ind, ind+1,"}").toString();
        DBObject obj = new BasicDBList();
        BulkWriteOperation builder = collection.initializeOrderedBulkOperation();
        for (int i=0; i<results.size(); i++){
            String json = new Gson().toJson(results.get(i));
            DBObject dbObject = (DBObject) JSON.parse(json);
            builder.insert(dbObject);
        }
        BulkWriteResult result = builder.execute();
        System.out.println("updating "+sqlTable+" to the collection "+ mongoCollection +" completed");
    }

/*
    protected String getJSONFromResultSet(ResultSet rs) {
        //Map json = new HashMap();
        List list = new ArrayList();
        if(rs!=null)
        {
            try {
                ResultSetMetaData metaData = rs.getMetaData();
                while(rs.next())
                {
                    Map<String,Object> columnMap = new HashMap<String, Object>();
                    for(int columnIndex=1;columnIndex<=metaData.getColumnCount();columnIndex++)
                    {
                        if(rs.getString(metaData.getColumnName(columnIndex))!=null)
                            columnMap.put(metaData.getColumnLabel(columnIndex),     rs.getString(metaData.getColumnName(columnIndex)));
                        else
                            columnMap.put(metaData.getColumnLabel(columnIndex), "");
                    }
                    list.add(columnMap);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //json.put(keyName, list);
        }
        return JSONValue.toJSONString(list);
    }*/
}
