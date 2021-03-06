package fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.servlets;

import fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.database.DatabaseSingleton;
import fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.utils.BDDUtils;
import fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.utils.ParameterUtils;
import fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.utils.UriUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Update extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String stringRequest = null;

        String tableName = req.getParameterMap().get("table")[0];
        try {
            Connection connection = DatabaseSingleton.connection;

            Set<Map.Entry<String, String[]>> columnsEntries = ParameterUtils.getColumnsEntries(req.getParameterMap());

            Set<Map.Entry<String, String[]>> oldColumnsEntries = ParameterUtils.getOldColumnsEntries(req.getParameterMap());

            String set = columnsEntries.stream().map(Map.Entry::getKey).map(parameterKey -> parameterKey + "=?").collect(Collectors.joining(" , "));

            String where = oldColumnsEntries.stream().map(Map.Entry::getKey).map(parameterKey -> parameterKey + "=?").collect(Collectors.joining(" AND "));

            PreparedStatement updateStatement = connection.prepareStatement(
                    String.format("UPDATE %s SET %s WHERE %s;",
                            req.getParameterMap().get("table")[0],
                            set,
                            where));

            Map<String, Integer> columnTypes = BDDUtils.getTableColumnsTypes(connection, tableName);

            int statementIndex = 1;
            for (Map.Entry<String, String[]> columnsEntry : columnsEntries)
                updateStatement.setObject(
                        statementIndex++,
                        columnsEntry.getValue().length > 0 ? columnsEntry.getValue()[0] : "",
                        columnTypes.get(columnsEntry.getKey()));

            for (Map.Entry<String, String[]> oldColumnsEntry : oldColumnsEntries)
                updateStatement.setObject(
                        statementIndex++,
                        oldColumnsEntry.getValue().length > 0 ? oldColumnsEntry.getValue()[0] : "",
                        columnTypes.get(oldColumnsEntry.getKey()));

            stringRequest = updateStatement.toString();

            updateStatement.executeUpdate();

            res.sendRedirect(
                    UriUtils.buildResponseUri("./select", "table=" + tableName, stringRequest, "success")
                            .toString());

        } catch (SQLException e) {
            try {
                res.sendRedirect(
                        UriUtils.buildResponseUri("./select", "table=" + tableName, "[" + stringRequest + "] " + e.getMessage(), "error")
                                .toString());
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
