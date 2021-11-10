package fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.servlets;

import fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.database.DatabaseSingleton;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class Select extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String tableName = req.getParameterMap().get("table")[0];
            Connection connection = DatabaseSingleton.getSingleton().getConnection();
            PreparedStatement selectTableStatement = connection.prepareStatement("SELECT * FROM " + tableName + ";");

            ResultSet selectResultSet = selectTableStatement.executeQuery();
            ResultSetMetaData selectResultSetMetadata = selectResultSet.getMetaData();

            DatabaseMetaData md = connection.getMetaData();
            ResultSet tablesResultSet = md.getTables(null, null, "%", new String[]{"TABLE"});

            StringBuilder sb = new StringBuilder();

            sb.append("<!DOCTYPE html>");
            sb.append("<html>");
            sb.append("  <head>");
            sb.append("    <meta charset='utf-8' />");
            sb.append("    <script");
            sb.append("      src='https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js'");
            sb.append("      integrity='sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p'");
            sb.append("      crossorigin='anonymous'");
            sb.append("    ></script>");
            sb.append("    <link");
            sb.append("      href='https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css'");
            sb.append("      rel='stylesheet'");
            sb.append("      integrity='sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3'");
            sb.append("      crossorigin='anonymous'");
            sb.append("    />");
            sb.append("  </head>");
            sb.append("  <body class='bg-dark'>");
            sb.append("    <div class='container-fluid'>");
            sb.append("      <div class='row'>");
            sb.append("        <div class='col-2'>");
            sb.append("          <ul class='list-group'>");
            //generate tables list
            while (tablesResultSet.next()) {
                String rsTableName = tablesResultSet.getString(3);
                boolean current = rsTableName.equals(tableName);
                sb.append("            <li class='list-group-item "+(current?"active":"")+"'>");
                sb.append("              <a href='./?table="+rsTableName+"' class='"+(current?"link-light":"link-primary")+"'>"+rsTableName+"</a>");
                sb.append("            </li>");
            }
            sb.append("          </ul>");
            sb.append("        </div>");
            sb.append("        <div class='col-10'>");
            sb.append("          <table class='table table-dark table-striped'>");
            sb.append("            <thead>");
            sb.append("              <tr>");
            //generate th for columns
            for (int i = 1; i <= selectResultSetMetadata.getColumnCount(); i++)
                sb.append("                <th scope='col'>" + selectResultSetMetadata.getColumnName(i) + "</th>");
            sb.append("                <th scope='col'>Action</th>");
            sb.append("              </tr>");
            sb.append("            </thead>");
            sb.append("            <tbody>");
            int index = 0;
            //for each row
            while (selectResultSet.next()) {
                //generate a hidden form with necessary data for delete
                sb.append("              <form id='" + index + "-delete' action='./delete' method='post'>");
                for (int i = 1; i <= selectResultSetMetadata.getColumnCount(); i++) {
                    String columnName = selectResultSetMetadata.getColumnName(i);
                    String value = selectResultSet.getString(i);
                    if (value != null)
                        sb.append("                <input type='hidden' name='col_" + columnName + "' value='" + value + "'/>");
                }
                sb.append("                <input type='hidden' name='table' value='" + tableName + "'/>");
                sb.append("              </form>");

                //generate a visible form with necessary data for update
                sb.append("              <form action='./update' method='post'>");
                sb.append("                <tr>");
                for (int i = 1; i <= selectResultSetMetadata.getColumnCount(); i++) {
                    String columnName = selectResultSetMetadata.getColumnName(i);
                    boolean nullable = selectResultSetMetadata.isNullable(i) == ResultSetMetaData.columnNullable;
                    String value = selectResultSet.getString(i);
                    sb.append("                  <td>");
                    sb.append("                    <input type='" + selectResultSetMetadata.getColumnTypeName(i) + "' name='col_" + columnName + "' " + (nullable ? "toggled" : "") + " value='" + (value == null ? "" : value) + "'/>");
                    if (value != null)
                        sb.append("                    <input type='hidden' name='prevcol_" + columnName + "' value='" + value + "'/>");
                    if (nullable)
                        sb.append("                    <input type='checkbox' toggler " + (value == null ? "" : "checked") + "/>");
                    sb.append("                  </td>");
                }
                sb.append("                  <td>");
                sb.append("                    <input type='submit' value='Modifier' />");
                sb.append("                    <input form='" + index + "-delete' type='submit' value='Supprimer' />");
                sb.append("                  </td>");
                sb.append("                </tr>");
                sb.append("                <input type='hidden' name='table' value='" + tableName + "'/>");
                sb.append("              </form>");
                index++;
            }
            //generate a visible form with necessary data for insert
            sb.append("              <form action='./insert' method='post'>");
            sb.append("                <tr>");
            for (int i = 1; i <= selectResultSetMetadata.getColumnCount(); i++) {
                String columnName = selectResultSetMetadata.getColumnName(i);
                boolean nullable = selectResultSetMetadata.isNullable(i) == ResultSetMetaData.columnNullable;
                sb.append("                  <td>");
                sb.append("                    <input type='" + selectResultSetMetadata.getColumnTypeName(i) + "' name='col_" + columnName + "' " + (nullable ? "toggled" : "") + "/>");
                if (nullable)
                    sb.append("                    <input type='checkbox' toggler checked/>");
                sb.append("                  </td>");
            }
            sb.append("                  <td>");
            sb.append("                    <input type='submit' value='Ajouter' />");
            sb.append("                  </td>");
            sb.append("                </tr>");
            sb.append("                <input type='hidden' name='table' value='" + tableName + "'/>");
            sb.append("              </form>");
            sb.append("            </tbody>");
            sb.append("          </table>");
            sb.append("        </div>");
            sb.append("      </div>");
            sb.append("    </div>");
            //enable/disable inputs with javascript (if NULL wanted)
            sb.append("    <script defer>");
            sb.append("      document.querySelectorAll('input[toggled]').forEach(function (toggled) {");
            sb.append("        try {");
            sb.append("          const toggler = toggled.parentElement.querySelector('input[toggler]');");
            sb.append("          function toggle() {");
            sb.append("            toggled.disabled = !toggler.checked;");
            sb.append("          }");
            sb.append("          toggler.addEventListener('change', toggle);");
            sb.append("          toggle();");
            sb.append("        } catch (error) {");
            sb.append("          console.error(error);");
            sb.append("        }");
            sb.append("      });");
            sb.append("    </script>");
            //manage toasts in javascript
            sb.append("    <div");
            sb.append("      id='toaster'");
            sb.append("      class='position-fixed bottom-0 end-0 p-3'");
            sb.append("      style='z-index: 11'");
            sb.append("    ></div>");
            sb.append("    <script defer>");
            sb.append("      const parameters = new URL(window.location.href).searchParams;");
            sb.append("      if (parameters.has('message')) {");
            sb.append("        const message = parameters.get('message');");
            sb.append("        const errortype = parameters.get('errortype');");
            sb.append("        let toastTheme;");
            sb.append("        switch (errortype) {");
            sb.append("          case 'success':");
            sb.append("            toastTheme = {");
            sb.append("              'bg-color': 'success',");
            sb.append("              'text-color': 'light',");
            sb.append("            };");
            sb.append("            break;");
            sb.append("          case 'error':");
            sb.append("            toastTheme = {");
            sb.append("              'bg-color': 'danger',");
            sb.append("              'text-color': 'light',");
            sb.append("            };");
            sb.append("            break;");
            sb.append("          case 'warning':");
            sb.append("            toastTheme = {");
            sb.append("              'bg-color': 'warning',");
            sb.append("              'text-color': 'light',");
            sb.append("            };");
            sb.append("            break;");
            sb.append("          default:");
            sb.append("            toastTheme = {");
            sb.append("              'bg-color': 'light',");
            sb.append("              'text-color': 'dark',");
            sb.append("            };");
            sb.append("            break;");
            sb.append("        }");
            sb.append("        const toast = document.createElement('div');");
            sb.append("        toast.innerHTML = `            <div              class='toast bg-${toastTheme['bg-color']} text-${toastTheme['text-color']}'              role='alert'              aria-live='assertive'              aria-atomic='true'              style='display: block'            >              <div class='toast-header bg-${toastTheme['bg-color']} text-${toastTheme['text-color']}'>                <strong class='me-auto'>${errortype}</strong>              </div>              <div class='toast-body'>${message}</div>            </div>`;");
            sb.append("        document.getElementById('toaster').appendChild(toast);");
            sb.append("        setTimeout(() => (toast.style.display = 'none'), 30000);");
            sb.append("      }");
            sb.append("    </script>");
            sb.append("  </body>");
            sb.append("</html>");
            sb.append("");

            res.getWriter().println(sb.toString());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
