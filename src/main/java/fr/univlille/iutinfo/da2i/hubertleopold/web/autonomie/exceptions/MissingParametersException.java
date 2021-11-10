package fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.exceptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponseWrapper;
import java.util.Arrays;

public class MissingParametersException extends ServletException {

    public static final int ERROR_CODE = HttpServletResponseWrapper.SC_BAD_REQUEST;

    public MissingParametersException(String... missingArguments) {
        super("Missing next parameters: "+ Arrays.deepToString(missingArguments));
    }

}
