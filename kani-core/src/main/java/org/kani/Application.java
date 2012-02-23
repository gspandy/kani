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
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;

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

		this.show();
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

	//	public static void start(String applicationId) {
	//		Application a = new Application(applicationId);
	//		a.show();
	//	}

}
