package org.kani.spring.i18n;

import org.kani.i18n.MessageSource;

import com.vaadin.Application;

public class MessageSourceAdapter implements MessageSource {
	
	private Application application;
	
	private org.springframework.context.MessageSource messageSource;

	public MessageSourceAdapter(final Application application, final org.springframework.context.MessageSource messageSource) {
		this.application = application;
		this.messageSource = messageSource;
	}

	@Override
	public String getMessage(String code, Object... args) {
		return messageSource.getMessage(code, args, application.getLocale());
	}

}
