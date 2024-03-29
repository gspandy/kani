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
package org.kani.example.spring.views;

import org.kani.Application;
import org.kani.annotations.View;
import org.kani.ui.KaniLabel;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
@View
public class HelloWorldView {

	public Component create(final Application application) {
		final VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(new KaniLabel("helloworld.label.title"));
		return verticalLayout;
	}

}
