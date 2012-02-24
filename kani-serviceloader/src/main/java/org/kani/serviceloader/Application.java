package org.kani.serviceloader;

import java.util.ServiceLoader;

import org.kani.ViewFactory;
import org.kani.serviceloader.i18n.ResourceBundleMessageSource;

@SuppressWarnings("serial")
public class Application extends org.kani.Application {

	private static final String APPLICATION_ID = "kani.application.id";

	@Override
	protected void setup() {
		// Fetch Application Id from init params in web.xml
		setApplicationId(getProperty(APPLICATION_ID));

		ServiceLoader<ViewFactory> viewFactoryLoader = ServiceLoader.load(ViewFactory.class);
		for (ViewFactory viewFactory : viewFactoryLoader) {
			setViewFactory(viewFactory);
			break;
		}
		if (getViewFactory() == null) {
			throw new IllegalStateException("No view factory found.");
		}

		setMessageSource(new ResourceBundleMessageSource(this));
	}

}
