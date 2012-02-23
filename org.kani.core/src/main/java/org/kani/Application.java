//---------------------------------------------------------
// $Id$ 
// 
// (c) 2011 Cellent Finance Solutions AG 
//          Calwer Strasse 33 
//          70173 Stuttgart 
//          www.cellent-fs.de 
//--------------------------------------------------------- 
package org.kani;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.kani.annotations.View;
import org.reflections.Reflections;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;

public class Application extends com.vaadin.Application {

	Map<String, Object> groups = new HashMap<String, Object>();

	ResourceBundle b;

	private String applicationId;

	public Application() {
		this.applicationId = "org.kani";
	}

	private void initTextResources() {
		try {
			b = ResourceBundle.getBundle("app_messages");
		} catch (Exception e) {
		}
	}

	private void instantiateGroups() {
		Set<Class<?>> classes = new Reflections(applicationId)
		.getTypesAnnotatedWith(View.class);

		for (Class<?> clazz : classes) {
			try {
				String fullName = clazz.getName();
				Object groupInstance = clazz.newInstance();

				groups.put(fullName, groupInstance);

			} catch (Exception e) {
			}
		}

	}

	@Override
	public void init() {
		this.initTextResources();

		this.instantiateGroups();

		this.show();
	}

	public void show() {
		this.initTextResources();

		this.instantiateGroups();

		String applicationName = applicationId;
		try {
			if (b != null) {
				applicationName = b.getString(applicationId + ".name");
			}
		} catch (Exception e) {
		}

		//		Window mainWindow = new Window("Starter_kani Application");
		//		Label label = new Label("Hello Vaadin user");
		//		mainWindow.addComponent(label);
		//		setMainWindow(mainWindow);

		Window mainWindow = new Window();
				Label label = new Label("Hello Vaadin user" + groups.keySet().size());
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
		for (String key : groups.keySet()) {
			final Object o = groups.get(key);
			System.out.println("FFFFFFFFFFFound " +key);

			try {
				final Method m = o.getClass().getMethod("createContent");

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
							Component p = (Component) m.invoke(o);
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
			} catch (Exception e) {
				e.printStackTrace();
			}

		}


	}

	//	public static void start(String applicationId) {
	//		Application a = new Application(applicationId);
	//		a.show();
	//	}

}
