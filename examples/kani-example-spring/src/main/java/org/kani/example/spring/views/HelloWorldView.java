package org.kani.example.spring.views;

import org.kani.Application;
import org.kani.annotations.View;
import org.kani.i18n.MessageSourceHolder;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
@View
public class HelloWorldView {

	public Component create(Application application) {
		final VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(new Label(MessageSourceHolder.getMessage("helloworld.label.title")));
		return verticalLayout;
	}

}
