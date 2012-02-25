package org.kani.i18n;

/**
 * @see https://vaadin.com/wiki/-/wiki/Main/ThreadLocal%20Pattern
 */
public class MessageSourceHolder {
	
	private static final ThreadLocal<MessageSource> messageSourceContextHolder = new InheritableThreadLocal<MessageSource>();	
	
	public static MessageSource getMessageSource() {
		return messageSourceContextHolder.get();
	}
	
	public static void setMessageSource(MessageSource MessageSource) {
		messageSourceContextHolder.set(MessageSource);
	}
	
	public static void reset() {
		messageSourceContextHolder.remove();
	}
	
	public static String getMessage(String code, Object... args) {
		if (getMessageSource() == null) {
			return code;
		}
		
		return getMessageSource().getMessage(code, args);
	}

}
