package org.kani.spring.i18n;

import java.io.Serializable;
import java.util.logging.Logger;

import org.kani.i18n.LocaleHolder;
import org.kani.i18n.MessageSource;
import org.springframework.context.NoSuchMessageException;

@SuppressWarnings("serial")
public class MessageSourceAdapter implements MessageSource, Serializable {
	
    private final static Logger logger = Logger.getLogger(MessageSourceAdapter.class.getName());
	
	private org.springframework.context.MessageSource messageSource;

	public MessageSourceAdapter(final org.springframework.context.MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public String getMessage(String code, Object... args) {
		try {
			return messageSource.getMessage(code, args, LocaleHolder.getLocale());
		} catch (NoSuchMessageException e) {
			logger.severe(String.format("No message found for code %s.", code));
		}
		return code;
	}

}
