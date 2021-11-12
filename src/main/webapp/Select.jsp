<%@ page import="fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.database.DatabaseSingleton" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.apache.commons.text.StringEscapeUtils" %>
<%@ page import="java.util.UUID" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String tableName = StringEscapeUtils.escapeHtml4(request.getParameterMap().get("table")[0]);
    Connection connection = DatabaseSingleton.connection;
    PreparedStatement selectTableStatement = connection.prepareStatement("SELECT * FROM " + tableName + ";");

    ResultSet selectResultSet = selectTableStatement.executeQuery();
    ResultSetMetaData selectResultSetMetadata = selectResultSet.getMetaData();

    DatabaseMetaData md = connection.getMetaData();
    ResultSet tablesResultSet = md.getTables(null, getServletContext().getInitParameter("db.username"), "%", new String[]{"TABLE"}); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <script
            src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
            crossorigin="anonymous"
    ></script>
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
            crossorigin="anonymous"
    />
</head>
<body class="bg-dark">
<div class="container">
    <div class="row">
        <div class="col-2">
            <ul class="list-group">
                <% while (tablesResultSet.next()) {
                    String rsTableName = StringEscapeUtils.escapeHtml4(tablesResultSet.getString(3));
                    boolean isCurrentTable = rsTableName.equals(tableName);%>
                <li class="list-group-item <%=isCurrentTable?"active":""%>">
                    <a href="?table=<%=rsTableName%>"
                       class="<%=isCurrentTable?"link-light":"link-primary"%>"><%=rsTableName%>
                    </a>
                </li>
                <% } %>
            </ul>
        </div>
        <div class="col-10">
            <table class="table table-dark table-striped">
                <thead>
                <tr>
                    <%for (int i = 1; i <= selectResultSetMetadata.getColumnCount(); i++) {%>
                    <th scope="col"><%=StringEscapeUtils.escapeHtml4(selectResultSetMetadata.getColumnName(i))%>
                    </th>
                    <%}%>
                </tr>
                </thead>
                <tbody>
                <% while (selectResultSet.next()) {
                    UUID rowUUID = UUID.randomUUID();%>
                <tr>
                    <form id="id-<%=rowUUID%>-delete" action="./delete" method="post">
                        <input type="hidden" name="table" value="<%=tableName%>"/>
                        <%
                            for (int i = 1; i <= selectResultSetMetadata.getColumnCount(); i++) {
                                String columnName = StringEscapeUtils.escapeHtml4(selectResultSetMetadata.getColumnName(i));
                                String value = StringEscapeUtils.escapeHtml4(selectResultSet.getString(i));
                                if (value != null) {
                        %>
                        <input type="hidden" name="col_<%=columnName%>" value="<%=value%>"/>
                        <% }
                        }%>
                    </form>
                    <form id="id-<%=rowUUID%>-update" action="./update" method="post">
                        <input type="hidden" name="table" value="<%=tableName%>"/>
                        <%
                            for (int i = 1; i <= selectResultSetMetadata.getColumnCount(); i++) {
                                String columnName = StringEscapeUtils.escapeHtml4(selectResultSetMetadata.getColumnName(i));
                                String value = StringEscapeUtils.escapeHtml4(selectResultSet.getString(i));
                        %>
                        <td>
                            <%if (value != null) {%>
                            <input type="hidden" name="old_col_<%=columnName%>"
                                   value="<%=value%>"/>
                            <%
                                }
                            %>
                            <input id="id-<%=rowUUID%>-<%=columnName%>"
                                   type="<%=selectResultSetMetadata.getColumnTypeName(i)%>" name="col_<%=columnName%>"
                                   value="<%=value==null?"":value%>"/>
                            <%if (selectResultSetMetadata.isNullable(i) == ResultSetMetaData.columnNullable) {%>
                            <input type="checkbox" toggle="#id-<%=rowUUID%>-<%=columnName%>"
                                   <%=value == null ? "" : "checked"%>/>
                            <%}%>
                        </td>
                        <%}%>
                    </form>
                    <td>
                        <input form="id-<%=rowUUID%>-update" type="submit" value="Modifier"/>
                        <input form="id-<%=rowUUID%>-delete" type="submit" value="Supprimer"/>
                    </td>
                </tr>
                <%}%>
                <form id="id-insert" action="./insert" method="post">
                    <input type="hidden" name="table" value="<%=tableName%>"/>
                    <tr>
                        <%
                            for (int i = 1; i <= selectResultSetMetadata.getColumnCount(); i++) {
                                String columnName = StringEscapeUtils.escapeHtml4(selectResultSetMetadata.getColumnName(i));%>
                        <td>
                            <input id="id-insert-<%=columnName%>" type="<%=selectResultSetMetadata.getColumnTypeName(i)%>"
                                   name="col_<%=columnName%>"/>
                            <%if (selectResultSetMetadata.isNullable(i) == ResultSetMetaData.columnNullable) {%>
                            <input type="checkbox" toggle="#id-insert-<%=columnName%>" checked/>
                            <%}%>
                        </td>
                        <%}%>
                        <td>
                            <input type="submit" value="Ajouter"/>
                        </td>
                    </tr>
                </form>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script defer>
    document.querySelectorAll("input[toggle]").forEach(function (toggler) {
        try {
            const toggled = document.querySelector(toggler.getAttribute("toggle"));

            function toggle() {
                toggled.disabled = !toggler.checked;
            }

            toggler.addEventListener("change", toggle);
            toggle();
        } catch (error) {
            console.error(error);
        }
    });
</script>
<div
        id="toaster"
        class="position-fixed bottom-0 end-0 p-3"
        style="z-index: 11"
></div>
<script defer>
    const parameters = new URL(window.location.href).searchParams;
    if (parameters.has("message")) {
        const message = parameters.get("message");
        const errortype = parameters.get("errortype");
        let toastTheme;
        switch (errortype) {
            case "success":
                toastTheme = {
                    "bg-color": "success",
                    "text-color": "light",
                };
                break;
            case "error":
                toastTheme = {
                    "bg-color": "danger",
                    "text-color": "light",
                };
                break;
            case "warning":
                toastTheme = {
                    "bg-color": "warning",
                    "text-color": "light",
                };
                break;
            default:
                toastTheme = {
                    "bg-color": "light",
                    "text-color": "dark",
                };
                break;
        }
        const toast = document.createElement("div");
        toast.innerHTML = `
        <div class="toast bg-\${toastTheme["bg-color"]} text-\${toastTheme["text-color"]}" role="alert" aria-live="assertive" aria-atomic="true" style="display: block">
            <div class="toast-header bg-\${toastTheme["bg-color"]} text-\${toastTheme["text-color"]}">
                <strong class="me-auto">\${errortype}</strong>
            </div>
                <div class="toast-body">\${message}</div>
        </div>`;
        document.getElementById("toaster").appendChild(toast);
        setTimeout(() => (toast.style.display = "none"), 30000);
    }
</script>
</body>
</html>