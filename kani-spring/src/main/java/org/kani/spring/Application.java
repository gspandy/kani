package org.kani.spring;

import org.kani.spring.i18n.MessageSourceAdapter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;

@SuppressWarnings("serial")
public class Application extends org.kani.Application implements ApplicationContextAware {
	
	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		MessageSourceAdapter messageSourceAdapter = new MessageSourceAdapter(this, messageSource);
		super.setMessageSource(messageSourceAdapter);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ViewFactory viewFactory = new ViewFactory(applicationContext);
		this.setViewFactory(viewFactory);
	}

}
