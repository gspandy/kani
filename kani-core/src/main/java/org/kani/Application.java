package org.kani;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.kani.i18n.MessageSource;
import org.kani.i18n.ResourceBundleMessageSource;

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
public class Application extends com.vaadin.Application {

	private static final String APPLICATION_ID = "kani.application.id";

	Map<String, Object> views = new HashMap<String, Object>();

	private MessageSource messageSource;

	@Override
	public void init() {
		this.initMessageSource();
		this.initViews();
		this.loadProtectedResources();
	}

	/**
	 * TODO: Externalize dependency to implementation.
	 */
	private void initMessageSource() {
		messageSource = new ResourceBundleMessageSource(this);
	}

	private void initViews() {
		ViewFactory viewFactory = getViewFactory();
		Collection<Object> factoryViews = viewFactory.createAll(getApplicationId());
		for (Object view : factoryViews) {
			views.put(view.getClass().getPackage().getName(), view);
		}

	}

	private ViewFactory getViewFactory() {
		ServiceLoader<ViewFactory> viewFactoryLoader = ServiceLoader.load(ViewFactory.class);
		for (ViewFactory viewFactory : viewFactoryLoader) {
			return viewFactory;
		}
		throw new IllegalStateException("No view factory found.");
	}


	public String getApplicationId() {
		return getProperty(APPLICATION_ID);
	}

	private Window main;

	private Window login;

	private VerticalLayout mainLayout;
	private TabSheet tabSheet;
	private Window aboutWindow;

	private HorizontalLayout toolbar;

	private MenuBar.MenuItem actionMenu;

	public void authenticate(String login, String password) throws Exception {
		if ("admin".equals(login) && "admin".equals(password)) {
			//		         User user = new UserImpl(login);
			//		         setUser(user);
			loadProtectedResources();
			return;
		}

		throw new Exception("Login failed!");

	}

	private void loadProtectedResources() {
		String applicationTitle = getApplicationId() + ".title";
		if (messageSource != null) {
			applicationTitle = messageSource.getMessage(getApplicationId() + ".title");
		}
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
			String viewName = viewKey;
			Method createMethod;
			try {
				createMethod = view.getClass().getMethod("create", org.kani.Application.class);
				Component viewContent = (Component) createMethod.invoke(view, this);
				tabSheet.addTab(viewContent, viewName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
		actionMenu.addSeparator();
		//		actionMenu.addItem("Logout", new Command() {
		//			public void menuSelected(MenuItem selectedItem) {
		//				logout();
		//			}
		//		});

		final MenuBar.MenuItem viewMenu = menubar.addItem("Help", null);
		viewMenu.addItem("About...", new Command() {
			public void menuSelected(MenuItem selectedItem) {
				main.addWindow(getAboutDialog());
			}
		});

		return menubar;
	}

	protected void logout() {
		setUser(null);
		removeWindow(main);
		setMainWindow(login);      
	}

	private Layout getHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.setWidth("100%");
		header.setMargin(true);
		header.setSpacing(true);

		CssLayout titleLayout = new CssLayout();
		
		String applicationTitle = getApplicationId() + ".title";
		if (messageSource != null) {
			applicationTitle = messageSource.getMessage(getApplicationId() + ".title");
		}
		H1 title = new H1(applicationTitle);
		titleLayout.addComponent(title);

		String applicationDescription = getApplicationId() + ".description";
		if (messageSource != null) {
			applicationDescription = messageSource.getMessage(getApplicationId() + ".description");
		}
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
			SmallText description = new SmallText("<br>Copyright (c) PE INTERNATIONAL and others.<br>"
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
			layout.setComponentAlignment(close, "right");
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
