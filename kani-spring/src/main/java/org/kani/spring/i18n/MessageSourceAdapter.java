package org.kani.spring.i18n;

import java.util.logging.Logger;

import org.kani.i18n.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.vaadin.Application;

public class MessageSourceAdapter implements MessageSource {
	
    private final static Logger logger = Logger.getLogger(MessageSourceAdapter.class.getName());
	
	private Application application;
	
	private org.springframework.context.MessageSource messageSource;

	public MessageSourceAdapter(final Application application, final org.springframework.context.MessageSource messageSource) {
		this.application = application;
		this.messageSource = messageSource;
	}

	@Override
	public String getMessage(String code, Object... args) {
		try {
			return messageSource.getMessage(code, args, application.getLocale());
		} catch (NoSuchMessageException e) {
			logger.severe(String.format("No message found for code %s.", code));
		}
		return code;
	}

}
