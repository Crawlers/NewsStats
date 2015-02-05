package com.cse10.gui;

import com.cse10.database.DatabaseHandler;

/**
 * Created by TharinduWijewardane on 2015-01-25.
 */
public class TempSetNullEntityGruopLabel {

    public static void main(String[] args) {
        DatabaseHandler.executeUpdate("UPDATE `crime_entity_group` SET label = NULL");
    }

}
