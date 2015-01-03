package com.cse10.util;

import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import com.cse10.entities.CrimeEntityGroup;

/**
 * Created by TharinduWijewardane on 2015-01-02.
 */
public class EmptyTables {

    public static void emptyCrimeEntityGroupTable() {

        String tableName = new DatabaseConstants().classToTableName.get(CrimeEntityGroup.class);
        String query = "DELETE FROM " + tableName;
        DatabaseHandler.executeQuery(query);

    }

}
