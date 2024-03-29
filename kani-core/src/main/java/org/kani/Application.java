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

package org.kani;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kani.i18n.LocaleHolder;
import org.kani.i18n.MessageSource;
import org.kani.i18n.MessageSourceHolder;

import com.vaadin.service.ApplicationContext.TransactionListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;


@SuppressWarnings("serial")
public class Application extends com.vaadin.Application implements TransactionListener {
	
    private final static Logger logger = Logger.getLogger(Application.class.getName());
    
	private Map<String, Object> views = new HashMap<String, Object>();

	private MessageSource messageSource;
	
	private ViewFactory viewFactory;

	private String applicationId;

	@Override
	public void init() {
		this.setup();
		
		if (getContext() != null) {
			getContext().addTransactionListener(this);
		}
		LocaleHolder.setLocale(getLocale());
		MessageSourceHolder.setMessageSource(getMessageSource());

		this.initViews();
		this.loadProtectedResources();
	}

	/**
	 * Shall be overridden by subclasses.
	 */
	protected void setup() {}

	private void initViews() {
		if (viewFactory == null) {
			throw new IllegalStateException("No view factory provided.");
		}
		
		Collection<Object> factoryViews = viewFactory.createAll(getApplicationId());
		for (Object view : factoryViews) {
			views.put(view.getClass().getName(), view);
		}
	}

	protected final MessageSource getMessageSource() {
		return messageSource;
	}

	protected final void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	protected final ViewFactory getViewFactory() {
		return viewFactory;
	}

	protected final void setViewFactory(ViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}

	public String getApplicationId() {
		return applicationId;
	}
	
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	private Window main;

	private VerticalLayout mainLayout;
	private TabSheet tabSheet;
	private Window aboutWindow;

	private HorizontalLayout toolbar;

	private MenuBar.MenuItem actionMenu;

	private void loadProtectedResources() {
		String applicationTitle = getMessage("title");
		main = new Window(applicationTitle);
		mainLayout = (VerticalLayout) main.getContent();
		mainLayout.setMargin(false);
		mainLayout.setStyleName("blue");
		setMainWindow(main);

		mainLayout.setSizeFull();
		mainLayout.addComponent(getMenu());

		HorizontalLayout header = new HorizontalLayout();

		header.addComponent(getHeader());
		header.addComponent(getToolbar());
		mainLayout.addComponent(header);

		CssLayout margin = new CssLayout();
		margin.setMargin(false, true, true, true);
		margin.setSizeFull();
		tabSheet = new TabSheet();
		tabSheet.setSizeFull();
		margin.addComponent(tabSheet);
		mainLayout.addComponent(margin);
		mainLayout.setExpandRatio(margin, 1);

		for (String viewKey : views.keySet()) {
			Object view = views.get(viewKey);
			String viewName = getMessage(viewKey, "title");
			Method createMethod;
			try {
				createMethod = view.getClass().getMethod("create", org.kani.Application.class);
				Component viewContent = (Component) createMethod.invoke(view, this);

				String viewIconPath = getMessage(viewKey, "icon");
				Resource viewIcon = new ClassResource(viewIconPath, this);
				
				tabSheet.addTab(viewContent, viewName, viewIcon);
			} catch (Exception e) {
				logger.log(Level.SEVERE, String.format("Could not initialize view %s", viewKey), e);
			}
		}
	}
	
	public String getMessage(final String codePrefix) {
		return getMessage(getApplicationId(), codePrefix);
	}
	
	public String getMessage(final String baseName, final String codePrefix) {
		String message = baseName + "." + codePrefix;
		if (messageSource != null) {
			message = messageSource.getMessage(baseName + "." + codePrefix);
		}
		return message;
	}
	
    public void transactionStart(com.vaadin.Application application, Object transactionData) {
    	LocaleHolder.setLocale(application.getLocale());
    	MessageSourceHolder.setMessageSource(getMessageSource());
    }
    
    public void transactionEnd(com.vaadin.Application application, Object transactionData) {
    	LocaleHolder.reset();
    	MessageSourceHolder.reset();
    }

	private MenuBar getMenu() {
		MenuBar menubar = new MenuBar();
		menubar.setWidth("100%");
		actionMenu = menubar.addItem("Action", null);

		actionMenu.addItem("Built-in Action...", new Command() {
			public void menuSelected(MenuItem selectedItem) {
				main.showNotification("Built-in Action executed!");
			}
		});
		
		final MenuBar.MenuItem viewMenu = menubar.addItem("Help", null);
		viewMenu.addItem("About...", new Command() {
			public void menuSelected(MenuItem selectedItem) {
				main.addWindow(getAboutDialog());
			}
		});

		return menubar;
	}

	private Layout getHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.setWidth("100%");
		header.setMargin(true);
		header.setSpacing(true);

		CssLayout titleLayout = new CssLayout();
		
		String applicationTitle = getMessage("title");
		H1 title = new H1(applicationTitle);
		titleLayout.addComponent(title);

		String applicationDescription = getMessage("description");
		SmallText description = new SmallText(applicationDescription);
		description.setSizeUndefined();
		titleLayout.addComponent(description);
		titleLayout.addComponent(description);

		header.addComponent(titleLayout);

		return header;
	}

	private Layout getToolbar() {
		if (toolbar == null) {
			toolbar = new HorizontalLayout();
			toolbar.setMargin(true);
			toolbar.setSpacing(true);
		}
		return toolbar;
	}

	private Window getAboutDialog() {
		if (aboutWindow == null) {
			aboutWindow = new Window("About...");
			aboutWindow.setModal(true);
			aboutWindow.setWidth("400px");

			VerticalLayout layout = (VerticalLayout) aboutWindow.getContent();
			layout.setMargin(true);
			layout.setSpacing(true);
			layout.setStyleName("blue");

			CssLayout titleLayout = new CssLayout();
			H2 title = new H2("Dynamic Vaadin OSGi Demo");
			titleLayout.addComponent(title);
			SmallText description = new SmallText("<br>Copyright ?? and others.<br>"
					+ "The icons are from the Silk icon set by Mark James<br>"
					+ "<a href=\"http://www.famfamfam.com/lab/icons/silk/\">http://www.famfamfam.com/lab/icons/silk/</a>");
			description.setSizeUndefined();
			description.setContentMode(Label.CONTENT_XHTML);

			titleLayout.addComponent(description);
			aboutWindow.addComponent(titleLayout);

			Button close = new Button("Close", new Button.ClickListener() {
				public void buttonClick(ClickEvent event) {
					(aboutWindow.getParent()).removeWindow(aboutWindow);
				}

			});
			layout.addComponent(close);
			layout.setComponentAlignment(close, Alignment.MIDDLE_RIGHT);
		}
		return aboutWindow;
	}

	class H1 extends Label {
		public H1(String caption) {
			super(caption);
			setSizeUndefined();
			setStyleName(Reindeer.LABEL_H1);
		}
	}

	class H2 extends Label {
		public H2(String caption) {
			super(caption);
			setSizeUndefined();
			setStyleName(Reindeer.LABEL_H2);
		}
	}

	class SmallText extends Label {
		public SmallText(String caption) {
			super(caption);
			setStyleName(Reindeer.LABEL_SMALL);
		}
	}

}
