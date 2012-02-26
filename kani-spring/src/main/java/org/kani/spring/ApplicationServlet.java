/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
