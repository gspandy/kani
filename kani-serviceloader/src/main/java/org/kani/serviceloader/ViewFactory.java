package org.kani.serviceloader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.kani.annotations.View;
import org.reflections.Reflections;

public class ViewFactory implements org.kani.ViewFactory {

	public Collection<Object> createAll(String applicationId) {
		try {
		Set<Class<?>> classes = new Reflections(applicationId).getTypesAnnotatedWith(View.class);
		Collection<Object> views = new ArrayList<Object>();
		for (Class<?> clazz : classes) {
			Object view = clazz.newInstance();
			views.add(view);
		}
		return views;
		} catch (Exception e) {
			throw new IllegalStateException("Could not instantiate views.", e);
		}
	}
}
