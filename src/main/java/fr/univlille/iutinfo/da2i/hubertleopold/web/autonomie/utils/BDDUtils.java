package fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BDDUtils {

    public static final String COLUMN_PARAMETER_IDENTIFIER = "col_";

    public static final String PREVIOUS_COLUMN_PARAMETER_IDENTIFIER = "prevcol_";

    public static Map<String, Integer> getTableColumnsTypes(Connection connection, String tableName) throws SQLException {
        ResultSet columns = connection.getMetaData().getColumns(null, null, tableName, null);
        Map<String, Integer> columnTypes = new HashMap<>();
        while (columns.next())
            columnTypes.put(columns.getString("COLUMN_NAME"), columns.getInt("DATA_TYPE"));
        return columnTypes;
    }

}
