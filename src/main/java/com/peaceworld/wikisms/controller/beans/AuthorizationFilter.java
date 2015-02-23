package com.peaceworld.wikisms.controller.beans;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebFilter("*")
public class AuthorizationFilter implements Filter {

    /**
     * Default constructor. 
     */
    public AuthorizationFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
        UserBean userBean = (UserBean) req.getSession().getAttribute("userBean");
        
        if (userBean != null && userBean.isLogedIn()) {
            // User is logged in, so just continue request.
        	if(req.getRequestURI().contains("login"))
        		res.sendRedirect(req.getContextPath() + "/userpanel");
        	else
        		chain.doFilter(request, response);
        } else {
        	// User is not logged in, so redirect to index.
        	if(req.getRequestURI().contains("userpanel"))
        		res.sendRedirect(req.getContextPath() + "/login");
        	else
        		chain.doFilter(request, response);
        }
        
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
