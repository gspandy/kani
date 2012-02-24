package org.kani.serviceloader.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.kani.Application;
import org.kani.i18n.MessageSource;

public class ResourceBundleMessageSource implements MessageSource {

	ResourceBundle resourceBundle;
	private Application application;
	private String baseName;

	public ResourceBundleMessageSource(final Application application) {
		this.application = application;
		baseName = application.getApplicationId().replace(".", "/") + "/messages";
	}

	public String getMessage(String code, Object... args) {
		try {
			resourceBundle = ResourceBundle.getBundle(baseName, application.getLocale());
		} catch (Exception e) {
			e.printStackTrace();
		}


		if (resourceBundle == null) {
			return code;
		}

		String message = resourceBundle.getString(code);
		if (message != null) {
			MessageFormat.format(message, args);
		}
		return message;
	}

}
