package org.kani.spring;

import java.util.Collection;
import java.util.Map;

import org.kani.annotations.View;
import org.springframework.context.ApplicationContext;

public class ViewFactory implements org.kani.ViewFactory {

	private ApplicationContext applicationContext;

	public ViewFactory(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public Collection<Object> createAll(String applicationId) {
		Map<String, Object> viewBeans = applicationContext.getBeansWithAnnotation(View.class);
		return viewBeans.values();
		
	}

}
