package org.kani.test;

import org.kani.annotations.View;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

@View
public class HelloWorldItem {
	
	public Component createContent() {
		return new Label("Hello World");
	}

}
