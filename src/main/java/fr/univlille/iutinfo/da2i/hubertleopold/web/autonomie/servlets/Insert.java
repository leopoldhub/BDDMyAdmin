package fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.servlets;

import fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.database.DatabaseSingleton;
import fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.utils.BDDUtils;
import fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.utils.ParameterUtils;
import fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.utils.UriUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Insert extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        String stringRequest = null;

        String tableName = req.getParameterMap().get("table")[0];
        try {
            Connection connection = DatabaseSingleton.connection;

            Set<Map.Entry<String, String[]>> columnsEntries = ParameterUtils.getColumnsEntries(req.getParameterMap());

            System.out.println(req.getParameterMap().size());
            System.out.println(columnsEntries.size());

            String columns = columnsEntries.stream().map(Map.Entry::getKey).collect(Collectors.joining(", "));
            String values = columnsEntries.stream().map(parameterEntry -> "?").collect(Collectors.joining(", "));

            PreparedStatement selectTableStatement = connection.prepareStatement(
                    String.format("INSERT INTO %s (%s) VALUES (%s);",
                            req.getParameterMap().get("table")[0],
                            columns,
                            values));

            Map<String, Integer> columnTypes = BDDUtils.getTableColumnsTypes(connection, tableName);

            System.out.println(columnTypes);

            int statementIndex = 1;
            for (Map.Entry<String, String[]> columnEntry : columnsEntries) {
                System.out.println(columnEntry.getKey()+" => "+columnTypes.get(columnEntry.getKey()));
                selectTableStatement.setObject(
                        statementIndex++,
                        columnEntry.getValue().length > 0 ? columnEntry.getValue()[0] : "",
                        columnTypes.get(columnEntry.getKey()));
            }

            stringRequest = selectTableStatement.toString();

            selectTableStatement.executeUpdate();

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
