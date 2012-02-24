package org.kani.spring;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;

@SuppressWarnings("serial")
public class ApplicationServlet extends AbstractApplicationServlet {
	
	private static final String APPLICATION_BEAN_NAME = "kaniAppliction";

	private WebApplicationContext applicationContext;
	
	private Class<? extends Application> applicationClass;

	@SuppressWarnings("unchecked")
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);

		applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext());
		if (!applicationContext.containsBean(APPLICATION_BEAN_NAME)) {
			throw new IllegalStateException("No application bean with name " + APPLICATION_BEAN_NAME + " found.");
		}
		
		if (!applicationContext.isTypeMatch(APPLICATION_BEAN_NAME, Application.class)) {
			Object applicationBean = applicationContext.getBean(APPLICATION_BEAN_NAME);
			throw new IllegalStateException(String.format("Application bean has type %s but should be a subtype of %s.", applicationBean.getClass().getName(), Application.class.getName()));
		}
		applicationClass = (Class<? extends Application>) applicationContext.getType(APPLICATION_BEAN_NAME);
	}

	@Override
	protected Class<? extends Application> getApplicationClass() throws ClassNotFoundException {
		return applicationClass;
	}

	@Override
	protected Application getNewApplication(HttpServletRequest request) {
		return (Application) applicationContext.getBean(APPLICATION_BEAN_NAME);
	}
}
