package org.kani.i18n;

import java.util.Locale;

/**
 * @see https://vaadin.com/wiki/-/wiki/Main/ThreadLocal%20Pattern
 */
public class LocaleHolder {
	
	private static final ThreadLocal<Locale> localeContextHolder = new InheritableThreadLocal<Locale>();	
	
	public static Locale getLocale() {
		return localeContextHolder.get();
	}
	
	public static void setLocale(Locale locale) {
		localeContextHolder.set(locale);
	}
	
	public static void reset() {
		localeContextHolder.remove();
	}
	
}
