package org.kani;

import java.util.Collection;

public interface ViewFactory {
	
	Collection<Object> createAll(String applicationId);

}
