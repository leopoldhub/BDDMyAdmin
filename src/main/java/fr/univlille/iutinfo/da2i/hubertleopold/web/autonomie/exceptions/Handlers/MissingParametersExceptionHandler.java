package fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.exceptions.Handlers;

import fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.exceptions.MissingParametersException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.RequestDispatcher.ERROR_MESSAGE;

@WebServlet("/missing-parameter-exception")
public class MissingParametersExceptionHandler extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setStatus(MissingParametersException.ERROR_CODE);

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("</head>");
        sb.append("<title>["+MissingParametersException.ERROR_CODE+"] MissingParametersException</title>");
        sb.append("<body>");
        sb.append("<h1>"+MissingParametersException.ERROR_CODE+"</h1>");
        sb.append(req.getAttribute(ERROR_MESSAGE));
        sb.append("</body>");
        sb.append("</html>");

        res.getWriter().println(sb.toString());
    }

}
