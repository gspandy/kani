package org.kani.i18n;

public interface MessageSource {
	
	String getMessage(String code, Object... args);

}
