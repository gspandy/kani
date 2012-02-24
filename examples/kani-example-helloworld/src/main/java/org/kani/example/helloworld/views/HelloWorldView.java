package org.kani.example.helloworld.views;

import org.kani.Application;
import org.kani.annotations.View;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@View
public class HelloWorldView {

	public Component create(Application application) {
		final VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(new Label("Hello World"));
		return verticalLayout;
	}

}