package fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.argumentfilters;

import fr.univlille.iutinfo.da2i.hubertleopold.web.autonomie.exceptions.MissingParametersException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public abstract class RequiredArgumentsFilter implements Filter {

    private String[] arguments;

    public RequiredArgumentsFilter(String... arguments) {
        this.arguments = arguments;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        for(String argument:arguments)
            if (req.getParameterMap().get(argument) == null || req.getParameterMap().get(argument)[0] == null || req.getParameterMap().get(argument)[0].length() == 0)
                throw new MissingParametersException(argument);

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
