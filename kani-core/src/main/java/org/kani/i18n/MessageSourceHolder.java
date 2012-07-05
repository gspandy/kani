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

package org.kani.i18n;

/**
 * Holds a {@link MessageSource} by binding it to the current thread.
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
