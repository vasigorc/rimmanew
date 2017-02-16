/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vgorcinschi.rimmanew.filters;

import com.vgorcinschi.rimmanew.cdi.LoginBean;
import com.vgorcinschi.rimmanew.entities.Credential;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author vgorcinschi
 */
public class AuthenticationFilter implements Filter {

    private static final boolean debug = true;
    private Credential credential = null;
    private String requestedUrl = null;

    @Inject
    private LoginBean lb;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    private final org.apache.logging.log4j.Logger log = LogManager.getLogger();

    public AuthenticationFilter() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            //obtain the requested url for logging
            requestedUrl = req.getRequestURL().toString();
            String queryString = req.getQueryString();
            /*
             check if the user is authenticated
             used for logging and the doFilter() method
             checking if 1. the session is active
             2. the login bean is instantiated
             3. the Credential is not null
             */
            String from = "unauthorized user";
            if (lb != null) {
                credential = lb.getCredential();
                if (credential != null) {
                    from = credential.getUsername() + " in group '"
                            + credential.getGroup().getGroupName() + "'";
                }
            }
            log.debug("New request for the protected resource at " + requestedUrl + "?" + queryString + " "
                    + "from " + from + ", client/proxy IP: " + req.getRemoteAddr());
        }
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {

    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        doBeforeProcessing(request, response);

        //needed for redirects below
        String contextPath = ((HttpServletRequest) request).getContextPath();
        if (credential != null) {
            if (!requestedUrl.toLowerCase().contains(credential.getGroup().getGroupName().toLowerCase())) {
                /*
                 This is the case when the user is valid but they are trying to acces
                 an unauthorized page - group name is part of the url
                 We are redirecting to a special page with 'forbidden' message
                 */
                ((HttpServletResponse) response).sendRedirect(contextPath + "/403.xhtml");
                log.error("Atuhorization breach attempt." + "User " + credential.getUsername() + " in group '" + credential.getGroup()
                        .getGroupName() + "' requested a forbidden URL: " + requestedUrl);
            } else {
                chain.doFilter(request, response);
                log.debug("User " + credential.getUsername() + " in group '" + credential.getGroup()
                        .getGroupName() + "' passed to " + requestedUrl);
            }
        } else {
            ((HttpServletResponse) response).sendRedirect(contextPath + "/login.xhtml");
            log.warn("Unauthorized user requested a forbidden URL: " + requestedUrl);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     *
     * @return
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }

    /**
     * Init method for this filter
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("AuthenticationFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AuthenticationFilter()");
        }
        StringBuffer sb = new StringBuffer("AuthenticationFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
