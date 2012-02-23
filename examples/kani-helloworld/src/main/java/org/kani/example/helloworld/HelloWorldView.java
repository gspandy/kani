package org.kani.example.helloworld;

import org.kani.annotations.View;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

@View
public class HelloWorldView {
	
	public Component create() {
		return new Label("Hello World");
	}

}
