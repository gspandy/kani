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

		setMessageSource(new ResourceBundleMessageSource(this.getApplicationId()));
	}

}
