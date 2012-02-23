package org.kani;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.ServiceLoader;

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
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;


@SuppressWarnings("serial")
public class Application extends com.vaadin.Application {

	private static final String APPLICATION_ID = "kani.application.id";

	Map<String, Object> views = new HashMap<String, Object>();

	ResourceBundle b;

	private void initTextResources() {
		try {
			b = ResourceBundle.getBundle("app_messages");
		} catch (Exception e) {
		}
	}



	@Override
	public void init() {
		this.initTextResources();

		this.initViews();

		this.loadProtectedResources();
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

	public void show() {
		try {
			if (b != null) {
				String applicationName = b.getString(getApplicationId() + ".name");
			}
		} catch (Exception e) {
		}

		//		Window mainWindow = new Window("Starter_kani Application");
		//		Label label = new Label("Hello Vaadin user");
		//		mainWindow.addComponent(label);
		//		setMainWindow(mainWindow);

		Window mainWindow = new Window();
		Label label = new Label("Hello Vaadin user" + views.keySet().size());
		mainWindow.addComponent(label);
		setMainWindow(mainWindow);

		final VerticalSplitPanel sp = new VerticalSplitPanel();
		mainWindow.addComponent(sp);
		//
		//		//		f.add(new JLabel(applicationName), BorderLayout.NORTH);
		//
		//
		Panel lp = new Panel();
		sp.addComponent(lp);
		sp.addComponent(new Label(" TEST "));
		//
		for (String key : views.keySet()) {
			final Object view = views.get(key);
			System.out.println("FFFFFFFFFFFound " +key);

			String name = key;
			if (b != null) {
				try {
					name = b.getString(key);
				} catch (Exception e) {
				}
			}

			//				Label label2 = new Label(name);
			//				mainWindow.addComponent(label2);

			//
			lp.addComponent(new Button(name, new Button.ClickListener() {

				public void buttonClick(ClickEvent event) {
					try {
						Method createMethod = view.getClass().getMethod("create");
						Component p = (Component) createMethod.invoke(view);
						sp.addComponent(p);
						return;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}));
			//				//				() {
			//				//
			//				//					public void actionPerformed(ActionEvent arg0) {
			//				//						try {
			//				//							Component p = (Component) m.invoke(o);
			//				//							sp.setRightComponent(p);
			//				//						} catch (Exception e) {
			//				//							// TODO Auto-generated catch block
			//				//							e.printStackTrace();
			//				//						}
			//				//
			//				//					}
			//				//				}));

		}


	}

	private String getApplicationId() {
		return getProperty(APPLICATION_ID);
	}



	//		   private final List<ActionContribution> actionContributions = Collections
	//		         .synchronizedList(new ArrayList<ActionContribution>());
	//		   private final Map<ActionContribution, Button> buttonActionMap = Collections
	//		         .synchronizedMap(new HashMap<ActionContribution, Button>());
	//		   private final Map<ActionContribution, MenuItem> menuActionMap = Collections
	//		         .synchronizedMap(new HashMap<ActionContribution, MenuItem>());

	private Window main;

	private Window login;

	private VerticalLayout mainLayout;
	private TabSheet tabSheet;
	private Window aboutWindow;

	private volatile boolean initialized = false;

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
		main = new Window("PE International OSGi Samples");
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

		//		      for (ActionContribution actionContribution : actionContributions) {
		//		         addActionContribution(actionContribution);
		//		      }

		initialized = true;      
	}

	//		   @Override
	//		   public void init() {
	//		      logger.info("Initializing PE International OSGi samples ...");
	//		      setTheme(Reindeer.THEME_NAME);
	//		      // setTheme(Runo.THEME_NAME);
	//		      // setTheme("demo");
	//		      
	//		      login = new LoginWindow(this);
	//
	//		      setMainWindow(login);
	//		   }

	@SuppressWarnings("serial")
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
		// header.setStyleName(Reindeer.LAYOUT_BLACK);

		CssLayout titleLayout = new CssLayout();
		H1 title = new H1(getApplicationId() + ".title");
		titleLayout.addComponent(title);

		SmallText description = new SmallText(getApplicationId() + ".description");
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

			@SuppressWarnings("serial")
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

	//		   public void bindViewContribution(ViewContribution viewContribution) {
	//		      viewContributions.add(viewContribution);
	//		      if (initialized) {
	//		         tabSheet.addTab(viewContribution.getView(this), viewContribution.getName(),
	//		               new ThemeResource(viewContribution.getIcon()));
	//		      }
	//		   }
	//
	//		   public void unbindViewContribution(ViewContribution viewContribution) {
	//		      viewContributions.remove(viewContribution);
	//		      if (initialized) {
	//		         tabSheet.removeComponent(viewContribution.getView(this));
	//		      }
	//		   }
	//
	//		   public void bindActionContribution(final ActionContribution actionContribution) {
	//		      logger.debug("bindActionContribution()");
	//		      if (initialized) {
	//		         addActionContribution(actionContribution);
	//		      }
	//		      actionContributions.add(actionContribution);
	//		   }
	//
	//		   private void addActionContribution(final ActionContribution actionContribution) {
	//		      final Application application = this;
	//		      Button button = new Button(actionContribution.getText());
	//		      button.setIcon(new ThemeResource(actionContribution.getIcon()));
	//		      button.addListener(new ClickListener() {
	//		         private static final long serialVersionUID = 1L;
	//
	//		         @Override
	//		         public void buttonClick(ClickEvent event) {
	//		            actionContribution.execute(application);
	//		         }
	//		      });
	//		      getToolbar().addComponent(button);
	//		      buttonActionMap.put(actionContribution, button);
	//
	//		      @SuppressWarnings("serial")
	//		      MenuItem menuItem = actionMenu.addItem(actionContribution.getText(), new Command() {
	//		         @Override
	//		         public void menuSelected(MenuItem selectedItem) {
	//		            actionContribution.execute(application);
	//		         }
	//		      });
	//		      menuItem.setIcon(new ThemeResource(actionContribution.getIcon()));
	//		      menuActionMap.put(actionContribution, menuItem);
	//		   }
	//
	//		   public void unbindActionContribution(ActionContribution actionContribution) {
	//		      logger.debug("unbindActionContribution()");
	//		      Button button = buttonActionMap.get(actionContribution);
	//		      toolbar.removeComponent(button);
	//		      buttonActionMap.remove(actionContribution);
	//
	//		      MenuItem menuItem = menuActionMap.get(actionContribution);
	//		      actionMenu.removeChild(menuItem);
	//		      menuActionMap.remove(actionContribution);
	//
	//		      actionContributions.remove(actionContribution);
	//		   }

	@SuppressWarnings("serial")
	class H1 extends Label {
		public H1(String caption) {
			super(caption);
			setSizeUndefined();
			setStyleName(Reindeer.LABEL_H1);
		}
	}

	@SuppressWarnings("serial")
	class H2 extends Label {
		public H2(String caption) {
			super(caption);
			setSizeUndefined();
			setStyleName(Reindeer.LABEL_H2);
		}
	}

	@SuppressWarnings("serial")
	class SmallText extends Label {
		public SmallText(String caption) {
			super(caption);
			setStyleName(Reindeer.LABEL_SMALL);
		}
	}

}
