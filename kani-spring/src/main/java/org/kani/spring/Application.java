package org.kani.spring;

import org.kani.spring.i18n.MessageSourceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

@SuppressWarnings("serial")
public class Application extends org.kani.Application {
	
	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		MessageSourceAdapter messageSourceAdapter = new MessageSourceAdapter(this, messageSource);
		super.setMessageSource(messageSourceAdapter);
	}

}
