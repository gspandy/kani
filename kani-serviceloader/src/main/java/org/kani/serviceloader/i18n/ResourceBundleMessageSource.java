package org.kani.serviceloader.i18n;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.kani.i18n.LocaleHolder;
import org.kani.i18n.MessageSource;

@SuppressWarnings("serial")
public class ResourceBundleMessageSource implements MessageSource, Serializable {

	private final static Logger logger = Logger.getLogger(ResourceBundleMessageSource.class.getName());

	ResourceBundle resourceBundle;

	private String baseName;

	public ResourceBundleMessageSource(final String applicationId) {
		baseName = applicationId.replace(".", "/") + "/messages";
	}

	public String getMessage(String code, Object... args) {
		try {
			resourceBundle = ResourceBundle.getBundle(baseName, LocaleHolder.getLocale());

			String message = resourceBundle.getString(code);
			if (message != null) {
				MessageFormat.format(message, args);
			}
			return message;
		} catch (Exception e) {
			logger.warning(String.format("No message found for key %s.", code));
			return code;
		}
	}

}
